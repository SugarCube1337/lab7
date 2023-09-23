package org.lab7.collection.data;

import org.lab7.Utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Represents a route with a unique ID, name, coordinates, creation date, start and end locations, and distance.
 */
public class Route implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L - 3L;
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Float distance;
    private Location location;


    public Route() {
        id = -1;
        creationDate = LocalDateTime.now();
    }

    private Route(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, Float distance, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.distance = distance;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("The name value cannot be empty");
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null)
            throw new IllegalArgumentException("The coordinate value cannot be empty");
        this.coordinates = coordinates;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null)
            throw new IllegalArgumentException("The coordinate value cannot be empty");
        this.location = location;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Scanner scan) {
        float routeDistance;
        try {
            routeDistance = scan.nextFloat();
        } catch (Exception ex) {
            scan.nextLine();
            throw new IllegalArgumentException("The value of the route distance must be a number");
        }
        if (routeDistance == 0.0f)
            throw new IllegalArgumentException("The route distance value cannot be empty");
        scan.nextLine();
        this.distance = routeDistance;
    }


    @Override
    public String toString() {
        return "Route[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", distance=" + distance +
                ", location=" + location.toString() +
                ']';
    }

    public static Route parseJSON(String[] fields) {
        int id = -1;
        String name = Utils.unescapeString(fields[1]);
        float x = Float.parseFloat(fields[2]);
        int y = Integer.parseInt(fields[3]);
        float distance = Float.parseFloat(fields[4]);
        Coordinates coordinates = new Coordinates(x, y);
        LocalDateTime creationDate = LocalDateTime.now();

        float xL = Float.parseFloat(fields[5]);
        float yL = Float.parseFloat(fields[6]);
        String nameL = Utils.unescapeString(fields[7]);
        Location location = new Location(xL, yL, nameL);

        return new Route(id, name, coordinates, creationDate, distance, location);
    }
}