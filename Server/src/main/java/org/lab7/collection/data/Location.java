package org.lab7.collection.data;


import java.io.Serializable;


public class Location implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L - 2L;
    private float x;
    private double y;
    private String name;

    public Location(float x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        try {
            this.x = x;
        } catch(Exception ex) {
            this.x = 0;
        }
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        try {
            this.x = x;
        } catch(Exception ex) {
            this.x = 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("It is necessary to specify the name");
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location[" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ']';
    }

}


