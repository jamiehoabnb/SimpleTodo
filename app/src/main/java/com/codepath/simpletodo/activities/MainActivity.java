package com.codepath.simpletodo.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.fragments.CreateEditToDoItemDialogFragment;
import com.codepath.simpletodo.models.ToDoItem;
import com.codepath.simpletodo.utils.storage.SimpleTodoStorage;
import com.codepath.simpletodo.utils.storage.impl.sqllite.SimpleTodoStorageDbHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements CreateEditToDoItemDialogFragment.CreateEditToDoItemDialogListener {
    private static ArrayList<ToDoItem> items;
    private TableLayout taskTable;
    private SimpleTodoStorage storage;

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMddyy", Locale.US);

    private static class RowOnTouchListener implements View.OnTouchListener {

        private GestureDetector gestureDetector;

        private RowOnTouchListener(Context context, RowGesterListener listener) {
            gestureDetector = new GestureDetector(context, listener);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    }

    private static class RowGesterListener extends GestureDetector.SimpleOnGestureListener {
        //The task text view.
        private View textView;

        //The task check box.
        private CheckBox checkbox;

        //The table row.
        private TableRow row;

        private ImageButton deleteButton;

        private MainActivity context;
        private FragmentManager fragmentManager;

        public RowGesterListener(View textView, CheckBox checkbox, TableRow row, MainActivity context,
                                 FragmentManager fragmentManager) {
            this.textView = textView;
            this.context = context;
            this.fragmentManager = fragmentManager;
            this.checkbox = checkbox;
            this.row = row;
            this.deleteButton = null;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if(e1.getX() > e2.getX()) {
                    if (deleteButton != null) {
                        return false;
                    }

                    row.removeView(checkbox);
                    deleteButton = new ImageButton(context);
                    deleteButton.setImageResource(R.drawable.delete);
                    deleteButton.setBackgroundColor(context.getResources().getColor(R.color.bg_main));

                    deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToDoItem deletedItem = items.remove(textView.getId());
                            context.storage.delete(deletedItem);
                            context.fillTaskTable();
                        }
                    });

                    row.addView(deleteButton);


                }  else if (e2.getX() > e1.getX()) {

                    if (deleteButton == null) {
                        return false;
                    }

                    row.removeView(deleteButton);
                    deleteButton = null;
                    row.addView(checkbox);
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            CreateEditToDoItemDialogFragment todoFragment = new CreateEditToDoItemDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable("item", items.get(textView.getId()));
            args.putInt("pos", textView.getId());
            todoFragment.setArguments(args);
            todoFragment.show(fragmentManager, "fragment_create_edit_todo");
            return true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        taskTable =(TableLayout)findViewById(R.id.taskTable);
        storage = new SimpleTodoStorageDbHelper(getApplicationContext());
        items = storage.read();

        fillTaskTable();
    }
    void fillTaskTable() {

        taskTable.removeAllViews();

        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());

        TableRow row;
        TextView tvTask, tvPriority, tvDueDate;
        CheckBox cbStatus;

        int index = 0;
        for (ToDoItem item: items) {
            row = new TableRow(this);

            tvTask = new TextView(this);
            tvTask.setTextColor(Color.BLUE);
            tvTask.setId(index);
            tvTask.setClickable(true);
            tvTask.setGravity(Gravity.LEFT);

            SpannableString taskStr = new SpannableString(item.getName());
            taskStr.setSpan(new UnderlineSpan(), 0, taskStr.length(), 0);
            tvTask.setText(taskStr);

            tvPriority = new TextView(this);
            tvPriority.setTextColor(Color.RED);
            tvPriority.setGravity(Gravity.LEFT);

            tvDueDate = new TextView(this);
            tvDueDate.setTextColor(Color.parseColor("#555555"));
            tvDueDate.setGravity(Gravity.LEFT);

            cbStatus = new CheckBox(this);
            cbStatus.setId(index);
            cbStatus.setGravity(Gravity.CENTER);

            tvTask.setOnTouchListener(new RowOnTouchListener(this,
                    new RowGesterListener(tvTask, cbStatus, row, MainActivity.this,
                            getFragmentManager())));

            index++;

            cbStatus.setOnClickListener(
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

            cbStatus.setChecked(item.getStatus() == ToDoItem.Status.DONE);

            tvTask.setWidth(135 * dip);
            tvPriority.setWidth(70 * dip);
            tvDueDate.setWidth(70 * dip);
            row.addView(tvTask);
            row.addView(tvPriority);
            row.addView(tvDueDate);
            row.addView(cbStatus);

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
                FragmentManager fm = getFragmentManager();
                CreateEditToDoItemDialogFragment todoFragment = new CreateEditToDoItemDialogFragment();
                Bundle args = new Bundle();
                args.putSerializable("item", new ToDoItem());
                args.putInt("pos", items.size());
                todoFragment.setArguments(args);
                todoFragment.show(fm, "fragment_create_edit_todo");

                return true;
            case R.id.quit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onFinishDialog(ToDoItem item, int pos) {
        if (pos == items.size()) {
            items.add(item);
        } else {
            items.set(pos, item);
        }
        Collections.sort(items, new ToDoItem.ToDoItemComparator());
        storage.write(item);
        fillTaskTable();
    }
}