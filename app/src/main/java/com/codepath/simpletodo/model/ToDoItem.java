package com.codepath.simpletodo.model;

import java.util.Date;

/**
 * The ToDo item.
 */
public class ToDoItem {

    private Integer id;
    private String name;
    private String notes;
    private Priority priority;
    private Date dueDate;
    private Status status;

    public static enum Status {

        TODO(0), IN_PROGRESS(1), DONE(2);

        private int val;

        Status(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        public static Status getStatus(int val) {
            switch(val) {
                case 0:
                    return TODO;
                case 1:
                    return IN_PROGRESS;
                case 2:
                    return DONE;
            }
            return null;
        }
    }

    public static enum Priority {

        LOW(0), MEDIUM(1), HIGH(2);


        private int val;

        Priority(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        public static Priority getPriority(int val) {
            switch(val) {
                case 0:
                    return LOW;
                case 1:
                    return MEDIUM;
                case 2:
                    return HIGH;
            }
            return null;
        }
    }

    public ToDoItem(Integer id, String name, String notes, Priority priority, Date dueDate, Status status) {
        this.id = id;
        this.name = name;
        this.notes = notes;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", status=" + status +
                '}';
    }
}
