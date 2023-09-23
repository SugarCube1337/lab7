package org.lab7.task;

import org.lab7.Main;

/**
 * Command for removing all elements with greater id
 */
public class RemoveGreaterTask implements Task {
    @Override
    public void execute(String[] args) {
        if(args.length < 1) {
            System.out.println("Need to specify id, usage: remove_greater [id]");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch(NumberFormatException ex) {
            System.out.println("Incorrect number entered");
            return;
        }
        Main.getConnectionManager().removeGreater(id);
        System.out.println("Objects removed");
    }
    @Override
    public String getDesctiption() {
        return "remove from the collection all items exceeding the given id";
    }
    @Override
    public String[] getArgumentNames() {
        return new String[]{"id"};
    }
}

