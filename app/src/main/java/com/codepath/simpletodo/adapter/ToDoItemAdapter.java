package com.codepath.simpletodo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.model.ToDoItem;

import java.util.ArrayList;

/**
 * ArrayAdapter for ToDoItem.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoItemAdapter(Context context, int id, ArrayList<ToDoItem> todoItems) {
        super(context, id, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem todoItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView priority = (TextView) convertView.findViewById(R.id.priority);

        name.setText(todoItem.getName());
        priority.setText(todoItem.getPriority().name());

        switch (todoItem.getPriority()) {
            case HIGH:
                priority.setTextColor(Color.RED);
                break;
            case MEDIUM:
                priority.setTextColor(Color.YELLOW);
                break;
            case LOW:
                priority.setTextColor(Color.GREEN);
                break;
        }

        return convertView;
    }
}
