package org.lab7;

import org.lab7.actions.Action;
import org.lab7.udp.ChunksData;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;
import org.lab7.actions.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

import static org.lab7.Utils.*;

/**
 * The `ConnectionManager` class handles communication with clients over UDP.
 * It manages server actions and processes incoming data from clients.
 */
public class ConnectionManager {
    public static final int PACKET_SIZE = 64 - 8;
    //    public static final int PACKET_SIZE = 5*1024-8;
    private DatagramChannel channel;
    private Selector selector;
    private SocketAddress clientAddress;

    private Map<String, Task> commands = new LinkedHashMap<>();
    private Map<String, ChunksData> clientChunks = new HashMap<>();
    private Map<ServerCommandType, Action> actions = new LinkedHashMap<>();

    /**
     * Initializes the `ConnectionManager` by registering server actions.
     */
    public ConnectionManager() {
        registerAction(ServerCommandType.GET_INFO, new InfoAction());
        registerAction(ServerCommandType.SHOW, new ShowAction());
        registerAction(ServerCommandType.ADD, new AddAction());
        registerAction(ServerCommandType.UPDATE, new UpdateAction());
        registerAction(ServerCommandType.REMOVE_BY_ID, new RemoveByIdAction());
        registerAction(ServerCommandType.CLEAR, new ClearAction());
        registerAction(ServerCommandType.REMOVE_LAST, new RemoveLastAction());
        registerAction(ServerCommandType.REMOVE_GREATER, new RemoveGreaterAction());
        registerAction(ServerCommandType.REORDER, new ReorderAction());
        registerAction(ServerCommandType.GET_MAX_DISTANCE, new MaxByDistanceAction());
        registerAction(ServerCommandType.GET, new GetAction());
        registerAction(ServerCommandType.SAVE, new ServerSaveCommand());
    }


    /**
     * This method represents the main server loop.
     * It configures the channel, gets the port from user input, and handles client requests.
     *
     * @throws IOException If there is an I/O error.
     */
    public void run() throws IOException {
        configureChannel();
        int port = getPortFromUserInput();
        setupServer(port);

        Scanner scanner = new Scanner(System.in);

        Thread dataProcessingThread = new Thread(() -> {
            while (true) {
                try {
                    handleClientRequests();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        dataProcessingThread.start();

        while (true) {
            System.out.print("> ");
            String userInput = scanner.nextLine();

            if (userInput.trim().equalsIgnoreCase("save")) {
                ServerSaveCommand saveCommand = new ServerSaveCommand();
                ServerCommand result = saveCommand.execute(null);

                if (result.type == ServerCommandType.ERROR) {
                    System.out.println("Error: " + Utils.deserializeObject(result.data));
                } else {
                    System.out.println("Server data saved successfully.");
                }
            } else {
                executeInput(userInput);
            }
        }
    }

    /**
     * Executes a command based on user input.
     *
     * @param input Command with arguments
     */
    public final void executeInput(String input) {
        String[] command = input.split(" ", 2);
        if (!commands.containsKey(command[0]))
            System.out.println("Unknown command: '" + command[0] + "'");
        else
            commands.get(command[0]).execute(command.length > 1 ? command[1].split(" ") : new String[0]);
    }

    /**
     * Configures the DatagramChannel for non-blocking mode and sets socket buffer sizes.
     *
     * @throws IOException If there is an I/O error.
     */

    private void configureChannel() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        int bufferSize = 1024 * 1024; // 1 MB
        channel.setOption(StandardSocketOptions.SO_RCVBUF, bufferSize);
        channel.setOption(StandardSocketOptions.SO_SNDBUF, bufferSize);
    }

    /**
     * Reads the port number from user input and ensures it is available.
     *
     * @return The valid port number entered by the user.
     */
    private int getPortFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        int port;
        while (true) {
            System.out.println("Enter the port: ");
            try {
                port = Integer.parseInt(scanner.nextLine());
                if (available(port)) {
                    return port;
                }
            } catch (Exception e) {
                System.out.println("The port doesn't fit");
            }
        }
    }

    /**
     * Sets up the server by binding the channel to the given port and registering it for reading.
     *
     * @param port The port number to bind to.
     * @throws IOException If there is an I/O error.
     */

    private void setupServer(int port) throws IOException {
        selector = Selector.open();
        channel.bind(new InetSocketAddress(port));
        channel.register(selector, SelectionKey.OP_READ);
        System.out.println("The server's awake.");
    }

    /**
     * Handles incoming client requests by selecting readable keys and processing them.
     *
     * @throws IOException If there is an I/O error.
     */

    private void handleClientRequests() throws IOException {
        selector.select();
        Iterator<SelectionKey> it = selector.selectedKeys().iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();

            if (key.isReadable()) {
                handleReadableKey();
            }
        }
    }

    /**
     * Handles the processing of a readable key, including data validation and chunk handling.
     *
     * @throws IOException If there is an I/O error.
     */

    private void handleReadableKey() throws IOException {
        ByteBuffer bufferHelp = ByteBuffer.allocate(PACKET_SIZE + 8);
        clientAddress = channel.receive(bufferHelp);
        byte[] received = bufferHelp.array();
        System.out.println("The chunk was received with a length of " + received.length + " bytes from the client " + clientAddress.toString());

        byte validation = Utils.checkFirst4Bytes(received);
        if (validation == 1) {
            handleValidation1(received);
        } else if (validation == 0) {
            handleValidation0(received);
        }
    }

    /**
     * Handles the processing of data with validation code 1.
     *
     * @param received The received data.
     */

    private void handleValidation1(byte[] received) {
        byte[] size = Arrays.copyOfRange(received, 4, 8);
        ChunksData chunksData = new ChunksData(Utils.fromByteArray(size));
        clientChunks.put(clientAddress.toString(), chunksData);
    }

    /**
     * Handles the processing of data with validation code 0.
     *
     * @param received The received data.
     * @throws IOException If there is an I/O error.
     */
    private void handleValidation0(byte[] received) throws IOException {
        if (!clientChunks.containsKey(clientAddress.toString()))
            return;
        int index = Utils.fromByteArray(Arrays.copyOfRange(received, 4, 8));
        byte[] data = Arrays.copyOfRange(received, 8, received.length);
        clientChunks.get(clientAddress.toString()).addChunk(index, data);

        if (clientChunks.get(clientAddress.toString()).isReady()) {
            handleClientRequestComplete();
        }
    }

    /**
     * Handles a complete client request, executes it, and sends the response back to the client.
     *
     * @throws IOException If there is an I/O error.
     */

    private void handleClientRequestComplete() throws IOException {
        byte[] request = clientChunks.get(clientAddress.toString()).getFullResponse();
        System.out.println("Received a complete client request with a length of " + request.length + " bytes.");
        ServerCommand message = executeInput((ServerCommand) deserializeObject(request));
        System.out.println("Sending a reply " + message.type + " to the client " + clientAddress.toString());
        List<byte[]> chunks = splitByteArray(serializeObject(message));
        sendChunksToClient(chunks);
        clientChunks.remove(clientAddress.toString());
    }

    /**
     * Sends chunks of data to the client.
     *
     * @param chunks The list of data chunks to send.
     * @throws IOException If there is an I/O error.
     */
    private void sendChunksToClient(List<byte[]> chunks) throws IOException {
        List<byte[]> firstInfo = new ArrayList<>();
        firstInfo.add(new byte[]{1, 1, 1, 1});
        firstInfo.add(intToBytes(chunks.size()));
        ByteBuffer firstInfoSize = ByteBuffer.wrap(joinByteArrays(firstInfo));
        channel.send(firstInfoSize, clientAddress);
        System.out.println("Send a chunk of length " + firstInfoSize.array().length + " bytes to the client " + clientAddress.toString());

        for (int i = 0; i < chunks.size(); i++) {
            List<byte[]> chunkInfo = new ArrayList<>();
            chunkInfo.add(new byte[]{0, 0, 0, 0});
            chunkInfo.add(intToBytes(i));
            chunkInfo.add(chunks.get(i));
            ByteBuffer responseBuffer = ByteBuffer.wrap(Utils.joinByteArrays(chunkInfo));
            channel.send(responseBuffer, clientAddress);
            System.out.println("Send a chunk of length " + responseBuffer.array().length + " bytes to the client " + clientAddress.toString());
        }
    }

    /**
     * Execute action
     *
     * @param action Command with arguments
     * @return Result
     */
    public final ServerCommand executeInput(ServerCommand action) {
        if (!actions.containsKey(action.type))
            return new ServerCommand(ServerCommandType.ERROR, "Unknown command for the server".getBytes());
        return actions.get(action.type).execute(action.data);
    }

    /**
     * Checks if a port is available for binding.
     *
     * @param port The port to check.
     * @return `true` if the port is available; otherwise, `false`.
     */
    public static boolean available(int port) {
        if (port < 1023 || port > 65534) {
            return false;
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    /**
     * Splits a byte array into smaller chunks.
     *
     * @param source The byte array to split.
     * @return A list of byte arrays representing the chunks.
     */
    public static List<byte[]> splitByteArray(byte[] source) {
        int maxChunkSize = PACKET_SIZE;
        if (source.length <= maxChunkSize) {
            return Collections.singletonList(source);
        }
        int numChunks = (int) Math.ceil((double) source.length / maxChunkSize);
        List<byte[]> chunks = new ArrayList<>(numChunks);

        int offset = 0;
        for (int i = 0; i < numChunks; i++) {
            int chunkSize = Math.min(maxChunkSize, source.length - offset);
            byte[] chunk = Arrays.copyOfRange(source, offset, offset + chunkSize);
            chunks.add(chunk);
            offset += chunkSize;
        }

        return chunks;
    }

    public Action getAction(ServerCommandType type) {
        return actions.get(type);
    }

    public final void registerAction(ServerCommandType type, Action action) {
        actions.put(type, action);
    }
}



