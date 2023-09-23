package org.lab7.collection.data;


import java.io.Serializable;

import java.util.Scanner;


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

    public void setX(Scanner scan) {
        try {
            x = scan.nextFloat();
        } catch (Exception ex) {
            scan.nextLine();
            x = 0;
        }
    }

    public double getY() {
        return y;
    }

    public void setY(Scanner scan) {
        try {
            y = scan.nextDouble();
        } catch (Exception ex) {
            scan.nextLine();
            y = 0;
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


