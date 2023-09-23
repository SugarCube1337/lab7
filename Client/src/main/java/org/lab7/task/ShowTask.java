package org.lab7.task;

import org.lab7.Main;
import org.lab7.collection.data.Route;
import org.lab7.udp.exception.ServerRuntimeException;

import java.util.ArrayList;
import java.util.List;

/**
 * Command for showing current objects in collection
 */
public class ShowTask implements Task {

    /**
     * Executes the 'show' task to display a page of Route objects from the collection.
     *
     * @param args Command-line arguments containing the page number to display.
     * @throws ServerRuntimeException If an incorrect page number is entered.
     */
    @Override
    public void execute(String[] args) {
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                throw new ServerRuntimeException("An incorrect page number has been entered.");
            }
        }
        if (page < 1)
            throw new ServerRuntimeException("An incorrect page number has been entered.");
        List<Object> response = Main.getConnectionManager().show(page);
        System.out.println("page " + page + " / " + (int) response.get(0));
        for (Route route : (ArrayList<Route>) response.get(1))
            System.out.println(route);
    }

    @Override
    public String getDesctiption() {
        return "display all items in the collection";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[]{"page"};
    }
}
