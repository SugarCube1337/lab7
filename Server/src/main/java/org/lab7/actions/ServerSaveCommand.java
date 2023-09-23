package org.lab7.actions;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

import java.io.IOException;

public class ServerSaveCommand implements Action {
    public ServerCommand execute(byte[] args) {try {
            Main.getStorageManager().save(Main.getStorageManager().getFilename());
        } catch (IOException ex) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Failed to save file"));
        }
        return new ServerCommand(ServerCommandType.SAVE, null);
    }
}
