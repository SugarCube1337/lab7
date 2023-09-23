package org.lab7.actions;

import java.io.IOException;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

/**
 * Command for removing all elements with greater id
 */
public class RemoveGreaterAction implements Action {
    @Override
    public ServerCommand execute(byte[] args) {
        try {
            Main.getStorageManager().removeGreater(Utils.fromByteArray(args));
        } catch (Exception ex) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject(ex.getMessage()));
        }
        try {
            Main.getStorageManager().save(Main.getStorageManager().getFilename());
        } catch (IOException ex) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Failed to save file"));
        }
        return new ServerCommand(ServerCommandType.REMOVE_LAST, new byte[]{1});
    }
}
