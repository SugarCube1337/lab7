package org.lab7.udp;

import java.io.Serializable;

/**
 * The `ServerCommand` class represents a command that is transmitted between the client and server.
 * It encapsulates the type of the command and associated data.
 */
public class ServerCommand implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L-10;
    public ServerCommandType type;
    public byte[] data;
    /**
     * Creates a new instance of the `ServerCommand` class with the specified type and data.
     *
     * @param type The type of the command.
     * @param data Additional data associated with the command.
     */
    public ServerCommand(ServerCommandType type, byte[] data) {
        this.type = type;
        this.data = data;
    }
}
