package org.lab7.collection.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.lab7.Main;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents a route with a unique ID, name, coordinates, creation date, start and end locations, and distance.
 */
public class Route implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L - 3L;
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private String creationDate;
    private Float distance;
    private Location location;

    private User owner;


    public Route() {
        id = Main.getStorageManager().getMaxId() + 1;
        creationDate = LocalDateTime.now().toString();
    }

    public Route(Integer id, String name, Coordinates coordinates, String creationDate, Float distance, Location location, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.distance = distance;
        this.location = location;
        this.owner = owner;
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

    public String getCreationDate() {
        return creationDate;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        if (distance == 0.0f)
            throw new IllegalArgumentException("The distance value cannot be empty");
        this.distance = distance;
    }


    @Override
    public String toString() {
        return "Route[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", location=" + location.toString() +
                ", distance=" + distance +
                ", owner="    + owner +
                ']';
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

}