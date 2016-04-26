package com.codepath.simpletodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * ArrayAdapter for ToDoItem.
 */
public class ToDoItemsAdapter extends ArrayAdapter<ToDoItem> {
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    public ToDoItemsAdapter(Context context, int id, ArrayList<ToDoItem> todoItems) {
        super(context, id, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem todoItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView dueDate = (TextView) convertView.findViewById(R.id.dueDate);
        TextView priority = (TextView) convertView.findViewById(R.id.priority);

        name.setText(todoItem.getName());
        dueDate.setText(dateFormatter.format(todoItem.getDueDate()));

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
