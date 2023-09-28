package org.lab7.actions;

import org.lab7.Utils;
import org.lab7.collection.data.User;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

public class AuthAction implements Action {
    @Override
    public ServerCommand execute(byte[] args, User caller) {
        if (caller == null)
            return new ServerCommand(ServerCommandType.ERROR, null);
        return new ServerCommand(ServerCommandType.AUTH, Utils.serializeObject(caller));
    }
}
