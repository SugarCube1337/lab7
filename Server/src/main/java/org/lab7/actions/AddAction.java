package org.lab7.actions;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.collection.data.Coordinates;
import org.lab7.collection.data.Location;
import org.lab7.collection.data.Route;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

import java.io.IOException;

/**
 * Command for adding a new object to the collection
 */
public class AddAction implements Action {
    /**
     * Executes the add action to add a new route to the collection.
     *
     * @param args The arguments containing the serialized route data to add.
     * @return A `ServerCommand` indicating the result of the add action (success or error).
     */
    @Override
    public ServerCommand execute(byte[] args) {
        Route parsed = null;
        try {
            // Deserialize the route object from the received byte array
            parsed = (Route) Utils.deserializeObject(args);
        } catch (Exception ex) {
            ex.printStackTrace(); // Print the exception stack trace for debugging
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Incorrect object data received"));
        }

        // Create a new route with the parsed data
        Route created = new Route();
        created.setName(parsed.getName());
        Coordinates createdCoords = new Coordinates(parsed.getCoordinates().getX(), parsed.getCoordinates().getY());
        created.setCoordinates(createdCoords);
        created.setDistance(parsed.getDistance());

        Location location = new Location(parsed.getLocation().getX(), parsed.getLocation().getY(), parsed.getLocation().getName());
        created.setLocation(location);

        // Add the created route to the storage manager
        Main.getStorageManager().add(created);
        try {
            // Save the updated collection to the file
            Main.getStorageManager().save(Main.getStorageManager().getFilename());
        } catch (IOException ex) {
            ex.printStackTrace(); // Print the exception stack trace for debugging
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Failed to save file"));
        }
        // Return a server command indicating the success of the add action
        return new ServerCommand(ServerCommandType.ADD, new byte[]{1});
    }
}
