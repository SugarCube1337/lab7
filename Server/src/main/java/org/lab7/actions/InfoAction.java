package org.lab7.actions;

import org.lab7.Main;
import org.lab7.Utils;
import org.lab7.collection.data.Route;
import org.lab7.udp.ServerCommand;
import org.lab7.udp.ServerCommandType;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * The `InfoAction` class represents an action for retrieving information about the collection.
 */
public class InfoAction implements Action {
    /**
     * Executes the info action.
     *
     * @param args The arguments (not used in this action).
     * @return A `ServerCommand` containing information about the collection.
     */
    @Override
    public ServerCommand execute(byte[] args) {
        HashSet<Route> elements = Main.getStorageManager().getAll();
        String elementType = "Unknown";

        if (!elements.isEmpty()) {
            // Получаем первый элемент множества
            Route firstElement = elements.iterator().next();
            elementType = firstElement.getClass().getName();
        }

        LocalDate initializationDate = Main.getStorageManager().getInitializationDate();
        int numberOfElements = elements.size();

        List<String> result = new ArrayList<>();
        result.add("Type of elements: " + elementType);
        result.add("Initialization date: " + initializationDate);
        result.add("Number of elements: " + numberOfElements);

        return new ServerCommand(ServerCommandType.GET_INFO, Utils.serializeObject(result));
    }
}
