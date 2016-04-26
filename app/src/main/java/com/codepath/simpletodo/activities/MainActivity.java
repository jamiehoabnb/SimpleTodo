package com.codepath.simpletodo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.utils.storage.SimpleTodoStorage;
import com.codepath.simpletodo.utils.storage.impl.sqllite.SimpleTodoStorageDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ToDoItem> items;
    TableLayout taskTable;
    private final int REQUEST_CODE = 20;
    private SimpleTodoStorage storage;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMddyy", Locale.US);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskTable =(TableLayout)findViewById(R.id.taskTable);
        storage = new SimpleTodoStorageDbHelper(getApplicationContext());
        items = storage.read();

        fillTaskTable();
    }
    void fillTaskTable() {

        taskTable.removeAllViews();
        TableRow row;
        TextView tvTask, tvPriority, tvDueDate;
        CheckBox tvStatus;
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());

        int index = 0;
        for (ToDoItem item: items) {
            row = new TableRow(this);

            tvTask = new TextView(this);
            tvTask.setTextColor(Color.BLUE);
            tvTask.setId(index);
            tvTask.setClickable(true);
            SpannableString taskStr = new SpannableString(item.getName());
            taskStr.setSpan(new UnderlineSpan(), 0, taskStr.length(), 0);
            tvTask.setText(taskStr);

            tvTask.setOnLongClickListener(
                    new AdapterView.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View item) {
                            ToDoItem deletedItem = items.remove(item.getId());
                            storage.delete(deletedItem);
                            fillTaskTable();
                            return true;
                        }
                    });

            //Edit the item that is long clicked.
            tvTask.setOnClickListener(
                    new AdapterView.OnClickListener() {
                        @Override
                        public void onClick(View item) {
                            Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                            i.putExtra("item", items.get(item.getId()));
                            i.putExtra("pos", item.getId());
                            startActivityForResult(i, REQUEST_CODE);
                        }
                    });

            tvPriority = new TextView(this);
            tvPriority.setTextColor(Color.RED);
            tvDueDate = new TextView(this);
            tvDueDate.setTextColor(Color.RED);
            tvStatus = new CheckBox(this);
            tvStatus.setId(index);

            index++;

            tvStatus.setOnClickListener(
                    new AdapterView.OnClickListener() {
                        @Override
                        public void onClick(View item) {
                            int pos = item.getId();
                            ToDoItem editedItem = items.get(pos);
                            editedItem.setStatus(((CheckBox) item).isChecked() ?
                                    ToDoItem.Status.DONE : ToDoItem.Status.TODO);
                            items.set(pos, editedItem);
                            Collections.sort(items, new ToDoItem.ToDoItemComparator());
                            storage.write(editedItem);
                            fillTaskTable();
                        }
                    });

            tvPriority.setText(item.getPriority().name());
            tvDueDate.setText(dateFormatter.format(item.getDueDate()));

            tvTask.setTypeface(null, 1);
            tvPriority.setTypeface(null, 1);
            tvDueDate.setTypeface(null, 1);

            tvTask.setTextSize(11);
            tvPriority.setTextSize(11);
            tvDueDate.setTextSize(11);

            switch (item.getPriority()) {
                case HIGH:
                    tvPriority.setTextColor(Color.RED);
                    break;
                case MEDIUM:
                    tvPriority.setTextColor(Color.YELLOW);
                    break;
                case LOW:
                    tvPriority.setTextColor(Color.GREEN);
                    break;
            }

            tvStatus.setChecked(item.getStatus() == ToDoItem.Status.DONE);

            tvTask.setWidth(135 * dip);
            tvPriority.setWidth(70 * dip);
            tvDueDate.setWidth(70 * dip);
            row.addView(tvTask);
            row.addView(tvPriority);
            row.addView(tvDueDate);
            row.addView(tvStatus);

            taskTable.addView(row, new TableLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        }
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
            storage.write(editedItem);
            fillTaskTable();
        }
    }
}