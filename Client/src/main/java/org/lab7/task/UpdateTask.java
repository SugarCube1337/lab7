package org.lab7.task;

import com.google.gson.Gson;
import org.lab7.Main;
import org.lab7.collection.data.Coordinates;
import org.lab7.collection.data.Location;
import org.lab7.collection.data.Route;

import java.util.Scanner;

/**
 * Represents a task for updating a Route object in the collection.
 */
public class UpdateTask implements Task {

    /**
     * Executes the task, allowing the user to update an existing Route object.
     *
     * @param args Command-line arguments passed to the task.
     */
    public void execute(String[] args) {
        if (args.length < 1) {
            System.out.println("The id must be specified, use: update [id]");
            return;
        }
        Route existed;
        try {
            existed = Main.getConnectionManager().get(Integer.parseInt(args[0]));
            if (existed == null)
                throw new IllegalArgumentException();
        } catch (Exception ex) {
            System.out.println("Object with the specified id was not found!");
            return;
        }
        if (args.length > 1) {
            try {
                String jsonInput = String.join(" ", args);
                Gson gson = new Gson();
                Route organization = gson.fromJson(jsonInput, Route.class);

                if (Main.getConnectionManager().add(organization))
                    System.out.println("Subject added.");
            } catch (Exception ex) {
                System.out.println("Incorrect object data entered or JSON reading error!");
            }
        }
        Route created = new Route();
        setValue("Name", () -> created.setName(new Scanner(System.in).nextLine()));
        created.setCoordinates(createCoordinates());
        setValue("Distance", () -> created.setDistance(new Scanner(System.in)));
        created.setLocation(createLocation());
        if (Main.getConnectionManager().add(created))
            System.out.println("Subject added.");
    }

    /**
     * Prompts the user to enter a value for a specified label until a valid value is provided.
     *
     * @param label  The label indicating what value to enter.
     * @param setter A Runnable that sets the value entered by the user.
     */
    private void setValue(String label, Runnable setter) {
        while (true) {
            System.out.print("Enter " + label + ": ");
            try {
                setter.run();
                break;
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private Coordinates createCoordinates() {
        Coordinates created = new Coordinates(0, 0);
        setValue("Coordinates, X", () -> created.setX(new Scanner(System.in)));
        setValue("Coordinates, Y", () -> created.setY(new Scanner(System.in)));
        return created;
    }


    private Location createLocation() {
        Location created = new Location(0, 0, null);
        setValue("Location, X", () -> created.setX(new Scanner(System.in)));
        setValue("Location, Y", () -> created.setY(new Scanner(System.in)));
        setValue("Location, name", () -> created.setName(new Scanner(System.in).nextLine()));
        return created;
    }

    @Override
    public String getDesctiption() {
        return "add a new item to the collection";
    }

    @Override
    public String[] getArgumentNames() {
        return new String[0];
    }
}
