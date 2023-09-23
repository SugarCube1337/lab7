package org.lab7.actions;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.collection.data.Route;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

/**
 * The `GetAction` class represents an action for retrieving a specific route by its ID from the collection.
 */
public class GetAction implements Action {
    /**
     * Executes the get action to retrieve a route by its ID.
     *
     * @param args The arguments containing the ID of the route to retrieve.
     * @return A `ServerCommand` containing the retrieved route or an error message if the route was not found.
     */
    @Override
    public ServerCommand execute(byte[] args) {
        Route existed;
        try {
            existed = Main.getStorageManager().get(Utils.fromByteArray(args));
            if (existed == null)
                throw new IllegalArgumentException();
        } catch (Exception ex) {
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Object with the specified id was not found!"));
        }
        return new ServerCommand(ServerCommandType.GET, Utils.serializeObject(existed));
    }
}
