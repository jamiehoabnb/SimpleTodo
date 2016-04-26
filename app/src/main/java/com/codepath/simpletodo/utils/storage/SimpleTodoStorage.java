package com.codepath.simpletodo.utils.storage;

import java.util.ArrayList;
import com.codepath.simpletodo.models.ToDoItem;

/**
 * Interface for todo item storage.
 */
public interface SimpleTodoStorage {

    public ArrayList<ToDoItem> read();

    public void write(ToDoItem item);

    public void delete(ToDoItem item);
}
