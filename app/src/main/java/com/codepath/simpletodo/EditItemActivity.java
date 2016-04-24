package com.codepath.simpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Activity for editing a todo item.
 */
public class EditItemActivity extends AppCompatActivity {

    private String item;
    private int pos;

    /**
     * Initialize the form.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        item = getIntent().getStringExtra("item");
        pos = getIntent().getIntExtra("pos", 0);

        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(item);
    }

    /**
     * Finish the activity and send the edited value back to the parent activity.
     */
    public void onEditItem(View v) {
        EditText etEditItem = (EditText) findViewById(R.id.etEditItem);

        Intent data = new Intent();
        data.putExtra("item", etEditItem.getText().toString());
        data.putExtra("pos", pos);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
