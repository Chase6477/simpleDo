package de.jr.simpledo;

import android.text.format.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class TaskItem {
    final private String title;
    final private String description;
    final private boolean deadline;
    final private LocalDate date;
    final private LocalTime time;
    final private boolean message;
    private boolean isCompleted = false;

    public TaskItem(String title, String description, boolean deadline, LocalDate date, LocalTime time, boolean message) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.date = date;
        this.time = time;
        this.message = message;
    }

    public static String getRelativeTimeSpan(LocalDateTime ldt) {
        return DateUtils.getRelativeTimeSpanString(
                ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
        ).toString();
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

    public LocalDateTime getDateTime() {
        if (time == null)
            return date.atStartOfDay();
        return LocalDateTime.of(date, time);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void toggleCompleted() {
        isCompleted = !isCompleted;
    }


}
