package org.lab7;

import java.io.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Utils class contains utility methods for various tasks such as string manipulation,
 * byte array operations, and object serialization/deserialization.
 */
public class Utils {

    /**
     * Escapes special characters in a string to ensure proper formatting when saved or transmitted.
     *
     * @param originalString The original string to be escaped.
     * @return The escaped string.
     */
    public static String escapeString(String originalString) {
        return originalString.replaceAll("\\\\", "\\\\u005C").replaceAll("\"", "\\\\u0022").replaceAll(",", "\\\\u002C");
    }

    /**
     * Unescapes a previously escaped string to its original form.
     *
     * @param escapedString The escaped string to be unescaped.
     * @return The unescaped string.
     */

    public static String unescapeString(String escapedString) {
        return escapedString.replaceAll("u0022", "\"").replaceAll("u002C", ",").replaceAll("u005C", "\\\\");
    }

    /**
     * Converts an integer into a byte array.
     *
     * @param i The integer to be converted.
     * @return The byte array representation of the integer.
     */
    public static byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }

    /**
     * Serializes an object into a byte array.
     *
     * @param o The object to be serialized.
     * @return The byte array containing the serialized object.
     */
    public static byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] result = null;
        try {
            out = new ObjectOutputStream(bos);
            if (o instanceof List) {
                List<?> list = new ArrayList<>((List<?>) o); // Создаем копию списка
                out.writeObject(list);
            } else {
                out.writeObject(o);
            }
            out.flush();
            result = bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                bos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Deserializes an object from a byte array.
     *
     * @param b The byte array containing the serialized object.
     * @return The deserialized object or null in case of an error.
     */
    public static Object deserializeObject(byte[] b) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(b);
             ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            // Обработка ошибки
            return null; // Или выброс исключения, если нужно
        }
    }

    /**
     * Joins multiple byte arrays into a single byte array.
     *
     * @param chunks The list of byte arrays to be joined.
     * @return The concatenated byte array.
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
     * Checks if the first four bytes in a byte array are equal.
     *
     * @param arr The byte array to be checked.
     * @return The value of the first byte if all four bytes are equal, or 255 if not.
     */
    public static byte checkFirst4Bytes(byte[] arr) {
        if (arr.length < 4) return (byte) 255;
        if (arr[0] == arr[1] && arr[1] == arr[2] && arr[2] == arr[3])
            return arr[0];
        return (byte) 255;
    }

    /**
     * Converts a byte array to an integer.
     *
     * @param bytes The byte array to be converted.
     * @return The integer value represented by the byte array.
     */
    public static int fromByteArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * Hash the password using the SHA-256 algorithm.
     *
     * @param password The password to be hashed.
     * @return Password hash as a string of hexadecimal characters.
     * @throws RuntimeException If an error occurs while hashing the password.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
