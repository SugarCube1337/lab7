package org.lab7.udp.exception;

/**
 * The ServerRuntimeException class represents an exception that may occur
 * on the server in the event of an unhandled error.
 */

public class ServerRuntimeException extends RuntimeException {
    public ServerRuntimeException(String message) {
        super(message);
    }
}
