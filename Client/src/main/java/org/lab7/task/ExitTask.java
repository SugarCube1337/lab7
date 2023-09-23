package org.lab7.task;

public class ExitTask implements Task {
    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
    @Override
    public String getDesctiption() {
        return "exit the program";
    }
    @Override
    public String[] getArgumentNames() {
        return new String[0];
    }
}


