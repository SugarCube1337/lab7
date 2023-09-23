package org.lab7.task;

import org.lab7.Main;

import java.io.*;
import java.util.*;

/**
 * The `ExecuteTask` class represents a task that executes a script from a specified file.
 * It processes the commands in the script file, allowing for recursive execution of scripts.
 */
public class ExecuteTask implements Task {
    public static List<String> executedScripts = new ArrayList<>(); // List to keep track of executed scripts to prevent recursion

    /**
     * Executes the 'execute_script' task to run a script from a given file.
     * This method reads the specified script file, processes its commands, and ensures recursion prevention.
     *
     * @param args Command-line arguments, where args[0] should be the file name of the script.
     */
    @Override
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("You need to specify the file name, the usage: execute_script [file_name]");
            return;
        }

        List<String> commands = new ArrayList<>();
        executedScripts.add(args[0]);

        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(args[0]));
            parseScriptFile(reader, commands);
        } catch (IOException ex) {
            System.out.println("Failed to read data from file");
        }

        executeCommands(commands);
    }

    /**
     * Parses the script file and populates the list of commands.
     *
     * @param reader   The InputStreamReader for reading the script file.
     * @param commands The list to store the commands from the script.
     * @throws IOException If there is an I/O error while reading the file.
     */
    private void parseScriptFile(InputStreamReader reader, List<String> commands) throws IOException {
        int c;
        StringBuilder line = new StringBuilder();

        while ((c = reader.read()) != -1) {
            if ((char) c == '\n') {
                String finished = line.toString();
                line = new StringBuilder();
                if (finished.startsWith("execute_script")) {
                    handleExecuteScriptCommand(finished, commands);
                } else {
                    commands.add(finished.replace("\r", "").replace("\n", ""));
                }
            } else {
                line.append((char) c);
            }
        }

        handleRemainingLine(line.toString(), commands);
    }

    /**
     * Handles an "execute_script" command line from the script file.
     *
     * @param command  The "execute_script" command line.
     * @param commands The list to store the commands from the script.
     */
    private void handleExecuteScriptCommand(String command, List<String> commands) {
        boolean pass = true;
        for (String script : new ArrayList<>(executedScripts)) {
            if (command.contains(script)) {
                System.out.println("You cannot run a script that has been run before");
                pass = false;
                break;
            }
        }
        if (pass) {
            executedScripts.add(command.replace("\r", "").replace("\n", ""));
            commands.add(command.replace("\r", "").replace("\n", ""));
        }
    }

    /**
     * Handles the remaining line in the script file.
     *
     * @param line     The remaining line.
     * @param commands The list to store the commands from the script.
     */
    private void handleRemainingLine(String line, List<String> commands) {
        if (line.length() > 0 && line.toString().replace("\r", "").replace("\n", "").length() > 0) {
            if (line.startsWith("execute_script")) {
                handleExecuteScriptCommand(line, commands);
            } else {
                commands.add(line.replace("\r", "").replace("\n", ""));
            }
        }
    }

    /**
     * Executes a list of commands.
     *
     * @param commands The list of commands to execute.
     */
    private void executeCommands(List<String> commands) {
        for (String command : commands) {
            Main.getCommandManager().executeInput(command);
        }
    }

    @Override
    public String getDesctiption() {
        return "execute the script from the given file";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[]{"file_name"};
    }
}




