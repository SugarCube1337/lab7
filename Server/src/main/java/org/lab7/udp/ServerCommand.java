package org.lab7.udp;

import org.lab7.collection.data.UserCredentials;

import java.io.Serializable;

/**
 * The `ServerCommand` class represents a command that is transmitted between the client and server.
 * It encapsulates the type of the command and associated data.
 */
public class ServerCommand implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L - 10;
    public ServerCommandType type;
    public byte[] data;
    /**
     * Constructs a ServerCommand with the specified type and data.
     *
     * @param type The type of the server command.
     * @param data The data associated with the command.
     * @param userCredentials
     */

    public UserCredentials userCredentials;

    public ServerCommand(ServerCommandType type, byte[] data, UserCredentials userCredentials) {
        this.type = type;
        this.data = data;
        this.userCredentials = userCredentials;
    }
}
