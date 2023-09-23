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
    private DatagramPacket info;
    private final int size;
    private Map<Integer, byte[]> chunks = new ConcurrentSkipListMap<>();
    private long initializationTime = (new Date()).getTime();

    public ChunksData(int size) {
        this.size = size;
    }

    /**
     * Adds a data chunk to the collection.
     *
     * @param index The index of the chunk.
     * @param chunk The data chunk to be added.
     */
    public synchronized void addChunk(int index, byte[] chunk) {
        if (chunks.size() > size) return; // Prevent adding more chunks than the expected size
        chunks.put(index, chunk);
    }

    /**
     * Checks if all expected chunks have been received.
     *
     * @return True if all chunks have been received, otherwise false.
     */
    public boolean isReady() {
        return chunks.size() >= size;
    }

    /**
     * Retrieves the full response by assembling all received chunks.
     *
     * @return The assembled full response as a byte array, or null if not all chunks are received.
     */
    public byte[] getFullResponse() {
        if (!isReady()) return null; // Return null if not all chunks are received
        List<byte[]> result = new ArrayList<>();
        for (Integer key : chunks.keySet())
            result.add(chunks.get(key));
        return joinByteArrays(result); // Assemble and return the full response
    }

    /**
     * Joins a list of byte arrays into a single byte array.
     *
     * @param chunks The list of byte arrays to be joined.
     * @return The joined byte array.
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
     * Checks if the object is still considered "active" based on a time limit.
     *
     * @return True if the object is active, otherwise false.
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
