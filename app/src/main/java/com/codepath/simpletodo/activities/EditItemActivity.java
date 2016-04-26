package com.codepath.simpletodo.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.Spinner;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Activity for editing a todo item.
 */
public class EditItemActivity extends AppCompatActivity implements OnClickListener {

    private ToDoItem item;
    private int pos;

    private DatePickerDialog dueDatePickerDialog;
    private EditText etDueDate;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    /**
     * Initialize the form.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item = (ToDoItem) getIntent().getSerializableExtra("item");
        pos = getIntent().getIntExtra("pos", 0);

        EditText etName = (EditText) findViewById(R.id.etName);
        etName.setText(item.getName());

        etDueDate = (EditText) findViewById(R.id.etDueDate);
        etDueDate.setInputType(InputType.TYPE_NULL);
        etDueDate.requestFocus();

        etDueDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(item.getDueDate());

        dueDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDueDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etDueDate.setText(dateFormatter.format(item.getDueDate()));

        EditText etNotes = (EditText) findViewById(R.id.etNotes);
        etNotes.setText(item.getNotes());

        Spinner spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        spinnerPriority.setSelection(item.getPriority().getVal());

        Spinner spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spinnerStatus.setSelection(item.getStatus().getVal());
    }

    @Override
    public void onClick(View view) {
        if(view == etDueDate) {
            dueDatePickerDialog.show();
        }
    }

    /**
     * Finish the activity and send the edited value back to the parent activity.
     */
    public void onEditItem(View v) {
        EditText etEditItem = (EditText) findViewById(R.id.etName);
        EditText etNotes = (EditText) findViewById(R.id.etNotes);
        Spinner spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        Spinner spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);

        Intent data = new Intent();
        item.setName(etEditItem.getText().toString());

        try {
            item.setDueDate(dateFormatter.parse(etDueDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        item.setNotes(etNotes.getText().toString());
        item.setPriority(ToDoItem.Priority.getPriority(spinnerPriority.getSelectedItemPosition()));
        item.setStatus(ToDoItem.Status.getStatus(spinnerStatus.getSelectedItemPosition()));

        data.putExtra("item", item);
        data.putExtra("pos", pos);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
