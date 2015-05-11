package com.dudebro.kmurph.convoapp;

public class Person {
    private int _id;
    private String _name;

    public Person() {

    }

    public Person(int id, String name) {
        this._id = id;
        this._name = name;
    }

    public Person(String name, int quantity) {
        this._name = name;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setName(String productname) {
        this._name = productname;
    }

    public String getName() {
        return this._name;
    }
}