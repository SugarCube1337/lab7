package org.lab7.actions;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.collection.data.User;
import org.lab7.collection.data.UserCredentials;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

/**
 * Represents an action for user registration in the server.
 */

public class RegisterAction implements Action {
    @Override
    public ServerCommand execute(byte[] args, User caller) {
        if(caller != null)
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("WTF BRO"));
        UserCredentials credentials = (UserCredentials)Utils.deserializeObject(args);
        if(Main.getStorageManager().createUser(credentials))
            return new ServerCommand(ServerCommandType.REGISTER, null);
        return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("A user with this name already exists"));
    }
}
