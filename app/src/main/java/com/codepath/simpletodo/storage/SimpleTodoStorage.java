package com.codepath.simpletodo.storage;

import java.io.File;
import java.util.ArrayList;

/**
 * Interface for todo item storage.
 */
public interface SimpleTodoStorage {


    public ArrayList<String> readItems();

    public void writeItems(ArrayList<String> items);
}
