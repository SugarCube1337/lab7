package org.lab7.udp;


import org.lab7.Utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

/**
 * Manages the client-side communication with the server using UDP.
 */
public class Client {
    private DatagramChannel channel = null;
    private Selector selector = null;
    public static final int PACKET_SIZE = 64 - 8;
    ;
    //public static final int PACKET_SIZE = 5*1024-8;

    /**
     * Initializes the UDP client, sets up the channel, and registers with a selector.
     * Also, prompts the user to input the server port.
     *
     * @throws IOException if an I/O error occurs
     */
    public Client() throws IOException {
        channel = DatagramChannel.open();
        channel.configureBlocking(false);
        int bufferSize = 1024 * 1024; // 1 MB
        //увеличение размеров для одного пакета
        channel.setOption(StandardSocketOptions.SO_RCVBUF, bufferSize);
        channel.setOption(StandardSocketOptions.SO_SNDBUF, bufferSize);

        Scanner scanner = new Scanner(System.in);
        int port;
        while (true) {
            System.out.println("Enter the port:");
            try {
                port = Integer.parseInt(scanner.nextLine());
                if (available(port)) {
                    channel.connect(new InetSocketAddress("localhost", port));
                    break;
                }
            } catch (Exception e) {
                System.out.println("The port doesn't fit");
            }
        }
        selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
    }

    /**
     * Sends a message to the server using UDP, breaking the message into packets.
     *
     * @param message The message to be sent
     * @return The response received from the server
     * @throws IOException if an I/O error occurs during communication
     */
    public byte[] sendMsg(byte[] message) throws IOException {
        List<byte[]> chunks = splitByteArray(message);
        sendChunkCount(chunks.size());
        sendChunks(chunks);
        return receiveAndAssembleResponse();
    }


    /**
     * Sends the total number of chunks to the server as the first information.
     *
     * @param chunkCount The number of message chunks
     * @throws IOException if an I/O error occurs during communication
     */
    private void sendChunkCount(int chunkCount) throws IOException {
        List<byte[]> firstInfo = new ArrayList<>();
        firstInfo.add(new byte[]{1, 1, 1, 1}); // Indicates that this is the chunk count information
        firstInfo.add(Utils.intToBytes(chunkCount)); // Send the number of chunks
        ByteBuffer bufferSize = ByteBuffer.wrap(Utils.joinByteArrays(firstInfo));
        channel.write(bufferSize);
    }

    /**
     * Sends individual chunks to the server along with their indices.
     *
     * @param chunks The list of message chunks
     * @throws IOException if an I/O error occurs during communication
     */
    private void sendChunks(List<byte[]> chunks) throws IOException {
        for (int i = 0; i < chunks.size(); i++) {
            List<byte[]> chunkInfo = new ArrayList<>();
            chunkInfo.add(new byte[]{0, 0, 0, 0});
            chunkInfo.add(Utils.intToBytes(i));
            chunkInfo.add(chunks.get(i));
            ByteBuffer buffer = ByteBuffer.wrap(Utils.joinByteArrays(chunkInfo));
            channel.write(buffer);
        }
    }

    /**
     * Receives and assembles the response from the server.
     *
     * @return The response received from the server
     * @throws IOException if an I/O error occurs during communication
     */
    private byte[] receiveAndAssembleResponse() throws IOException {
        ChunksData currentWorker = new ChunksData(1);
        while (currentWorker.isActual()) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                if (currentWorker.isReady())
                    continue;
                SelectionKey key = it.next();
                it.remove();
                if (key.isReadable()) {
                    ByteBuffer helpBuffer = ByteBuffer.allocate(PACKET_SIZE + 8);
                    channel.receive(helpBuffer);
                    byte[] received = helpBuffer.array();
                    byte validation = Utils.checkFirst4Bytes(received);
                    if (validation == 1) {
                        byte[] size = Arrays.copyOfRange(received, 4, 8);
                        currentWorker = new ChunksData(Utils.fromByteArray(size));
                    } else if (validation == 0) {
                        int index = Utils.fromByteArray(Arrays.copyOfRange(received, 4, 8));
                        byte[] data = Arrays.copyOfRange(received, 8, received.length);
                        currentWorker.addChunk(index, data);
                        if (currentWorker.isReady()) {
                            return currentWorker.getFullResponse();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    public static boolean available(int port) {
        if (port < 1023 || port > 65534) {
            return false;
        }
        return true;
    }

    /**
     * Concatenates a list of byte arrays into a single byte array.
     *
     * @param chunks A list of byte arrays to be concatenated.
     * @return A single byte array containing the concatenated data.
     */
    public static byte[] joinByteArrays(List<byte[]> chunks) {
        int totalLength = 0;
        for (byte[] chunk : chunks) {
            totalLength += chunk.length;
        }

        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, result, offset, chunk.length);
            offset += chunk.length;
        }

        return result;
    }

    /**
     * Splits a byte array into smaller chunks, each with a maximum size of PACKET_SIZE.
     *
     * @param source The source byte array to be split.
     * @return A list of smaller byte arrays, each not exceeding PACKET_SIZE in size.
     */

    public static List<byte[]> splitByteArray(byte[] source) {
        int maxChunkSize = PACKET_SIZE; //B если меняем тут, то меняем и в connect manager server: приходящие кб
        if (source.length <= maxChunkSize) {
            return Collections.singletonList(source);
        }
        List<byte[]> chunks = new ArrayList<>();
        for (int i = 0; i <= (int) Math.ceil(source.length / PACKET_SIZE); i++) {
            chunks.add(Arrays.copyOfRange(source, i * PACKET_SIZE, (i + 1) * PACKET_SIZE));
        }
        return chunks;
    }
}