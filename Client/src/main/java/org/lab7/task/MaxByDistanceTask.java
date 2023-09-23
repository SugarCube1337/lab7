package org.lab7.task;

import org.lab7.Main;
import org.lab7.collection.data.Route;

/**
 * Executes the 'max_by_distance' task to find and output the route with the longest distance.
 */
public class MaxByDistanceTask implements Task {
    @Override
    public void execute(String[] args) {
        Route result = Main.getConnectionManager().getMaxByDistance();
        if(result == null)
            System.out.println("The collection is empty");
        else
            System.out.println(result);
    }
    @Override
    public String getDesctiption() {
        return "output the route with the longest distance";
    }
    @Override
    public String[] getArgumentNames() {
        return new String[0];
    }
}
