package com.codepath.simpletodo.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.codepath.simpletodo.R;
import com.codepath.simpletodo.models.ToDoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment for creating or editing a todo item.
 */
public class CreateEditToDoItemDialogFragment extends DialogFragment implements OnClickListener {

    private ToDoItem item;
    private int pos;

    private DatePickerDialog dueDatePickerDialog;
    private EditText etDueDate;
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private View fragmentView;

    public interface CreateEditToDoItemDialogListener {
        void onFinishDialog(ToDoItem item, int pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_edit_todo, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentView = view;

        item = (ToDoItem) getArguments().getSerializable("item");
        pos = getArguments().getInt("pos", 0);

        ImageButton saveButton = (ImageButton) view.findViewById(R.id.btnSaveItem);
        saveButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etEditItem = (EditText) fragmentView.findViewById(R.id.etName);
                EditText etNotes = (EditText) fragmentView.findViewById(R.id.etNotes);
                Spinner spinnerPriority = (Spinner) fragmentView.findViewById(R.id.spinnerPriority);
                Spinner spinnerStatus = (Spinner) fragmentView.findViewById(R.id.spinnerStatus);

                item.setName(etEditItem.getText().toString());

                try {
                    item.setDueDate(dateFormatter.parse(etDueDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                item.setNotes(etNotes.getText().toString());
                item.setPriority(ToDoItem.Priority.getPriority(spinnerPriority.getSelectedItemPosition()));
                item.setStatus(ToDoItem.Status.getStatus(spinnerStatus.getSelectedItemPosition()));

                CreateEditToDoItemDialogListener listener = (CreateEditToDoItemDialogListener) getActivity();
                listener.onFinishDialog(item, pos);
                dismiss();
            }
        });

        ImageButton cancelButton = (ImageButton) view.findViewById(R.id.btnCancelItem);
        cancelButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        EditText etName = (EditText) view.findViewById(R.id.etName);
        etName.setText(item.getName());

        etDueDate = (EditText) view.findViewById(R.id.etDueDate);
        etDueDate.setInputType(InputType.TYPE_NULL);
        etDueDate.requestFocus();

        etDueDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.setTime(item.getDueDate());

        dueDatePickerDialog = new DatePickerDialog(view.getContext(), new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etDueDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        etDueDate.setText(dateFormatter.format(item.getDueDate()));

        EditText etNotes = (EditText) view.findViewById(R.id.etNotes);
        etNotes.setText(item.getNotes());

        Spinner spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        spinnerPriority.setSelection(item.getPriority().getVal());

        Spinner spinnerStatus = (Spinner) view.findViewById(R.id.spinnerStatus);
        spinnerStatus.setSelection(item.getStatus().getVal());

    }

    @Override
    public void onClick(View view) {
        if(view == etDueDate) {
            dueDatePickerDialog.show();
        }
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }
}
