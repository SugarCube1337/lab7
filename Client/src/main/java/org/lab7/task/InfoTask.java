package org.lab7.task;

import org.lab7.Main;

/**
 * Command outputs some info about collection
 */
public class InfoTask implements Task {
    @Override
    public void execute(String[] args) {
        for (String info : Main.getConnectionManager().getInfo())
            System.out.println(info);
    }

    @Override
    public String getDesctiption() {
        return "collection information";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[0];
    }
}
