package org.lab7.task;

import org.lab7.Main;

/**
 * Represents a task for updating collection data by calling the actualize method
 * of the connection manager and printing a success message.
 */
public class ActualizeTask implements Task {
    @Override
    public void execute(String[] args) {
        Main.getConnectionManager().actualize();
        System.out.println("Data updated");
    }
    @Override
    public String getDesctiption() {
        return "actualize collection data";
    }
    @Override
    public String[] getArgumentNames() {
        return new String[]{};
    }
}
