package org.lab7.task;

/**
 * Interface that represents task for command
 */
public interface Task {
    /**
     * Method called when a command executed
     * @param args Given arguments
     */
    public void execute(String[] args);
    /**
     * Getting a description of command for command list
     * @return Description in command list
     */
    public String getDesctiption();
    /**
     * Getting a required arguments for command
     * @return Argument names
     */
    public String[] getArgumentNames();
}
