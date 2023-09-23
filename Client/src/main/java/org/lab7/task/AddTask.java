package org.lab7.task;


import org.lab7.Main;
import org.lab7.collection.data.Coordinates;
import org.lab7.collection.data.Location;
import org.lab7.collection.data.Route;

import java.util.Scanner;

/**
 * Command for adding a new object to the collection
 */
public class AddTask implements Task {
    @Override
    public void execute(String[] args) {
        if (args.length > 0) {
            try {
                String[] fields = args[0].split(" ");
                Route route = Route.parseJSON(fields);

                if (Main.getConnectionManager().add(route)) {
                    System.out.println("Subject added.");
                }
            } catch (Exception ex) {
                System.out.println("Incorrect object data has been entered!");
            }
        }

        Route created = new Route();
        setValue("Name", () -> created.setName(new Scanner(System.in).nextLine()));
        Coordinates coordinates = createCoordinates();
        created.setCoordinates(coordinates);
        setValue("Distance", () -> created.setDistance(new Scanner(System.in)));
        Location location = createLocation();
        created.setLocation(location);
        if (Main.getConnectionManager().add(created))
            System.out.println("Subject added.");

    }


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

