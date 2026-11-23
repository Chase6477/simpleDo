package de.jr.simpledo;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskItem {
    final private String title;
    final private String description;
    final private boolean deadline;
    final private LocalDate date;
    final private LocalTime time;
    final private boolean message;

    public TaskItem(String title, String description, boolean deadline, LocalDate date, LocalTime time, boolean message) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public boolean messageEnabled() {
        return message;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean deadlineEnabled() {
        return deadline;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }
}
