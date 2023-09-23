package org.lab7.task;

import org.lab7.Main;

/**
 * Command for clearing the whole collection
 */
public class ClearTask implements Task {
    @Override
    public void execute(String[] args) {
        Main.getConnectionManager().clear();
        System.out.println("The collection has been cleared");
    }

    @Override
    public String getDesctiption() {
        return "clean up the collection";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[0];
    }
}
