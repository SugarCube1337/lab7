package org.lab7.udp;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Represents a data structure for managing and assembling chunks of data received over a network.
 */
public class ChunksData {


    private DatagramPacket info;// Information about the data (e.g., sender's address)

    private final int size;
    private Map<Integer, byte[]> chunks = new ConcurrentSkipListMap<>();// Map to store received chunks
    private long initializationTime = (new Date()).getTime();// Timestamp of object initialization

    /**
     * Creates a new instance of the `ChunksData` class with the specified size.
     *
     * @param size The expected size of the data (number of chunks).
     */
    public ChunksData(int size) {
        this.size = size;
    }

    /**
     * Adds a chunk of data to the collection.
     *
     * @param index Index of the chunk.
     * @param chunk The data chunk to add.
     */

    public synchronized void addChunk(int index, byte[] chunk) {
        if (chunks.size() > size) return;
        chunks.put(index, chunk);
    }

    /**
     * Checks if all expected chunks have been received and the data is ready.
     *
     * @return `true` if the data is ready; otherwise, `false`.
     */
    public boolean isReady() {
        return chunks.size() >= size;
    }

    /**
     * Retrieves the full assembled response data.
     *
     * @return The assembled response data, or `null` if the data is not yet ready.
     */
    public byte[] getFullResponse() {
        if (!isReady()) return null;
        List<byte[]> result = new ArrayList<>();
        for (Integer key : chunks.keySet())
            result.add(chunks.get(key));
        return joinByteArrays(result);
    }

    /**
     * Joins multiple byte arrays into a single byte array.
     *
     * @param chunks The list of byte arrays to join.
     * @return The combined byte array.
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
     * Checks if the data is still considered "actual" based on a time threshold.
     *
     * @return `true` if the data is considered "actual"; otherwise, `false`.
     */
    public boolean isActual() {
        return initializationTime + 5000 > (new Date()).getTime();
    }

    public DatagramPacket getInfo() {
        return info;
    }

    public void setInfo(DatagramPacket info) {
        this.info = info;
    }

    public int getSize() {
        return size;
    }
}
