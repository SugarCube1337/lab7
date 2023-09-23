package org.lab7.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.lab7.Utils;

import org.lab7.Main;
import org.lab7.collection.data.Route;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;

/**
 * Command for showing current objects in collection
 */
public class ShowAction implements Action {
    private final int PAGE_SIZE = 5;// The number of routes to display per page

    /**
     * Executes the show action.
     *
     * @param args The page number as a byte array.
     * @return A `ServerCommand` containing the routes to display and additional information.
     */
    @Override
    public ServerCommand execute(byte[] args) {
        // Get the collection of routes from the storage manager
        HashSet<Route> routeSet = Main.getStorageManager().getAll();
        int size = routeSet.size();

        if (size == 0)// Check if the collection is empty, and return an error if it is
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("The collection is empty"));

        int page = Utils.fromByteArray(args) - 1;
        int maxPage = (int) Math.ceil((float) size / (float) PAGE_SIZE);

        if (page + 1 > maxPage)// Check if the specified page number is valid
            return new ServerCommand(ServerCommandType.ERROR, Utils.serializeObject("Maximum page number - " + maxPage));

        List<Object> response = new ArrayList<>();
        response.add(maxPage);// Add the total number of pages to the response

        List<Route> routeList = new ArrayList<>(routeSet);
        int startIndex = page * PAGE_SIZE;
        int endIndex = Math.min((page + 1) * PAGE_SIZE, size);

        // Get a sublist of routes to display on the current page
        List<Route> parsed = new ArrayList<>(routeList.subList(startIndex, endIndex)); // Создаем копию подсписка
        response.add(parsed);

        // Create and return a server command with the response
        return new ServerCommand(ServerCommandType.SHOW, Utils.serializeObject(response));
    }
}

