package com.codepath.simpletodo.storage;

import java.util.ArrayList;
import com.codepath.simpletodo.model.ToDoItem;

/**
 * Interface for todo item storage.
 */
public interface SimpleTodoStorage {

    public ArrayList<ToDoItem> read();

    public void write(ToDoItem item);

    public void delete(ToDoItem item);
}
