package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.adapters.ToDoItemsAdapter;
import com.codepath.simpletodo.utils.storage.SimpleTodoStorage;
import com.codepath.simpletodo.utils.storage.impl.sqllite.SimpleTodoStorageDbHelper;
import com.codepath.simpletodo.models.ToDoItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The main activity of creating, listing and deleting todo items.
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<ToDoItem> items;
    private ArrayAdapter<ToDoItem> itemsAdapter;
    private ListView lvItems;
    private final int REQUEST_CODE = 20;
    private SimpleTodoStorage storage;

    /**
     * Initialize the MainActivity and read the saved to do list.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvlItems);
        //storage = new FileSimpleTodoStorage(getFilesDir());
        storage = new SimpleTodoStorageDbHelper(getApplicationContext());
        items = storage.read();
        itemsAdapter = new ToDoItemsAdapter(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    /**
     * Set up the click and long click listeners for the todo list.
     */
    private void setupListViewListener() {
        //Delete the item that is long clicked.
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                        View item, int pos, long id) {
                        ToDoItem deletedItem = items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        storage.delete(deletedItem);
                        return true;
                    }
                });

        //Edit the item that is long clicked.
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("item", items.get(pos));
                        i.putExtra("pos", pos);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("item", new ToDoItem());
                i.putExtra("pos", items.size());
                startActivityForResult(i, REQUEST_CODE);

                return true;
            case R.id.quit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Save the edited todo item.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            ToDoItem editedItem  = (ToDoItem) data.getSerializableExtra("item");
            int pos = data.getIntExtra("pos", 0);
            if (pos == items.size()) {
                items.add(editedItem);
            } else {
                items.set(pos, editedItem);
            }
            Collections.sort(items, new ToDoItem.ToDoItemComparator());
            itemsAdapter.notifyDataSetChanged();
            storage.write(editedItem);
        }
    }
}
