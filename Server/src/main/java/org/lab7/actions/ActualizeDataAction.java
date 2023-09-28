package org.lab7.actions;

import org.lab7.Main;
import org.lab7.collection.data.User;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

public class ActualizeDataAction implements Action {
    @Override
    public ServerCommand execute(byte[] args, User caller) {
        Main.getStorageManager().updateCollection();
        return new ServerCommand(ServerCommandType.ACTUALIZE, null);
    }
}
