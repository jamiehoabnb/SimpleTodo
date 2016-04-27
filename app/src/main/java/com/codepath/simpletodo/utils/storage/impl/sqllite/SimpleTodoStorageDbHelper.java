package com.codepath.simpletodo.utils.storage.impl.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.codepath.simpletodo.utils.storage.SimpleTodoStorage;

import java.util.ArrayList;
import java.util.Date;
import com.codepath.simpletodo.models.ToDoItem;

/**
 * SQLLite storage implementation of SimpleTodoStorage.
 */
public class SimpleTodoStorageDbHelper extends SQLiteOpenHelper implements SimpleTodoStorage {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDo.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ToDoItemTable.TABLE_NAME + " (" +
                    ToDoItemTable._ID + " INTEGER PRIMARY KEY," +
                    ToDoItemTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    ToDoItemTable.COLUMN_NAME_NOTES + TEXT_TYPE + COMMA_SEP +
                    ToDoItemTable.COLUMN_NAME_PRIORITY + INTEGER_TYPE + COMMA_SEP +
                    ToDoItemTable.COLUMN_NAME_DUE_DATE + INTEGER_TYPE + COMMA_SEP +
                    ToDoItemTable.COLUMN_NAME_STATUS + INTEGER_TYPE +
            " )";

    private final String POSTS_SELECT_QUERY =
            String.format("SELECT * FROM %s ORDER BY %s desc, %s", ToDoItemTable.TABLE_NAME,
                    ToDoItemTable.COLUMN_NAME_PRIORITY, ToDoItemTable.COLUMN_NAME_DUE_DATE);

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ToDoItemTable.TABLE_NAME;

    public static abstract class ToDoItemTable implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NOTES = "notes";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_DUE_DATE = "due_date";
        public static final String COLUMN_NAME_STATUS = "status";
    }

    public SimpleTodoStorageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public ArrayList<ToDoItem> read() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ToDoItem> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
            if (cursor.moveToFirst()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(ToDoItemTable._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ToDoItemTable.COLUMN_NAME_NAME));
                    String notes = cursor.getString(cursor.getColumnIndex(ToDoItemTable.COLUMN_NAME_NOTES));
                    int priority = cursor.getInt(cursor.getColumnIndex(ToDoItemTable.COLUMN_NAME_PRIORITY));
                    int status = cursor.getInt(cursor.getColumnIndex(ToDoItemTable.COLUMN_NAME_STATUS));
                    int dueDate = cursor.getInt(cursor.getColumnIndex(ToDoItemTable.COLUMN_NAME_DUE_DATE));
                    items.add(new ToDoItem(id, name, notes, ToDoItem.Priority.getPriority(priority),
                            new Date(((long) dueDate)*1000l), ToDoItem.Status.getStatus(status)));
                } while(cursor.moveToNext());
            }
        } catch (Throwable t) {
            //Table doesn't exist.
            db.execSQL(SQL_CREATE_TABLE);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

    public void write(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            int dueDate = (int) (item.getDueDate().getTime()/1000l);

            values.put(ToDoItemTable.COLUMN_NAME_NAME, item.getName());
            values.put(ToDoItemTable.COLUMN_NAME_NOTES, item.getNotes());
            values.put(ToDoItemTable.COLUMN_NAME_PRIORITY, item.getPriority().getVal());
            values.put(ToDoItemTable.COLUMN_NAME_DUE_DATE, dueDate);
            values.put(ToDoItemTable.COLUMN_NAME_STATUS, item.getStatus().getVal());

            if (item.getId() == null) {
                long newId = db.insertOrThrow(ToDoItemTable.TABLE_NAME, null, values);
                item.setId(newId);
            } else {
                String selection = ToDoItemTable._ID + " = ?";
                String[] selectionArgs = { String.valueOf(item.getId()) };
                db.update(ToDoItemTable.TABLE_NAME, values, selection, selectionArgs);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void delete(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            String selection = ToDoItemTable._ID + " = ?";
            String[] selectionArgs = { String.valueOf(item.getId()) };
            db.delete(ToDoItemTable.TABLE_NAME, selection, selectionArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
