package org.lab7.task;


import org.lab7.Main;

/**
 * Command for removing last element
 */
public class RemoveLastTask implements Task {
    @Override
    public void execute(String[] args) {
        Main.getConnectionManager().removeLast();
        System.out.println("Last item deleted");
    }

    @Override
    public String getDesctiption() {
        return "delete the last item in the collection";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[0];
    }
}
