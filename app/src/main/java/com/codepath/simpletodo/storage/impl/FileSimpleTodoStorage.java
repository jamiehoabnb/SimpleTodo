package com.codepath.simpletodo.storage.impl;

import com.codepath.simpletodo.storage.SimpleTodoStorage;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * File storage implementation of FileSimpleTodoStorage.
 */
public class FileSimpleTodoStorage implements SimpleTodoStorage {

    private static final String FILE_NAME = "todo.txt";
    private static File filesDir;

    public FileSimpleTodoStorage(File filesDir) {
        this.filesDir = filesDir;
    }

    public ArrayList<String> readItems() {
        File todoFile = new File(filesDir, FILE_NAME);
        try {
            return new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public void writeItems(ArrayList<String> items) {
        File todoFile = new File(filesDir, FILE_NAME);
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
