package com.example.kmurph.convoapp;

public class Note {
    private int _id;
    private String _note;

    public Note() {

    }

    public Note(int id, String note) {
        this._id = id;
        this._note = note;
    }

    public Note(String note, int quantity) {
        this._note = note;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setNote(String note) {
        this._note = note;
    }

    public String getNote() {
        return this._note;
    }
}