package org.lab7.actions;

import java.io.IOException;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

/**
 * Command for reordering collection
 */
public class ReorderAction implements Action {
    @Override
    public ServerCommand execute(byte[] args) {
        Main.getStorageManager().reverse();
        try {
            Main.getStorageManager().save(Main.getStorageManager().getFilename());
        } catch (IOException ex) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Failed to save file"));
        }
        return new ServerCommand(ServerCommandType.REORDER, null);
    }
}