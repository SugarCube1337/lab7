package org.lab7.actions;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.collection.data.Route;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

/**
 * Executes the 'max_by_distance' task to find and output the route with the longest distance.
 */
public class MaxByDistanceAction implements Action {
    @Override
    public ServerCommand execute(byte[] args) {
        Route result = null;
        for (Route organization : Main.getStorageManager().getAll()) {
            if (result == null || organization.getDistance() > result.getDistance())
                result = organization;
        }
        return new ServerCommand(ServerCommandType.GET_MAX_DISTANCE, Utils.serializeObject(result));
    }
}
