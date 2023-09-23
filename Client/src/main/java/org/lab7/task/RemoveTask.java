package org.lab7.task;

import org.lab7.Main;

/**
 * Command for removing collection element by id
 */
public class RemoveTask implements Task {
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("You need to specify id, usage: remove_by_id [id]");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            System.out.println("Incorrect number entered");
            return;
        }
        if (Main.getConnectionManager().removeById(id))
            System.out.println("Object removed");
        else
            System.out.println("Object with this id does not exist");
    }

    @Override
    public String getDesctiption() {
        return "remove an item from the collection";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[]{"id"};
    }
}