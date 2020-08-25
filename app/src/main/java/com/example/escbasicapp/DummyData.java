package com.example.escbasicapp;

import java.util.ArrayList;

public class DummyData {

    public static ArrayList<Contact> contacts = new ArrayList<>();

    static {
        contacts.add(new Contact("Pat","010-1234-5678", "destroy@things.com"));
        contacts.add(new Contact("Mat", "010-8765-4321", "build@things.com"));
        contacts.add(new Contact("Narrator", "010-1428-5714", "manage@things.com"));
    }

}
