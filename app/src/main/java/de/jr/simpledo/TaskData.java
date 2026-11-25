package de.jr.simpledo;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TaskData {
    private List<TaskItem> dataList = new ArrayList<>();

    public static final String FILE_NAME= "Data.json";

    private final Context context;


    public TaskData(Context context) {
        this.context = context;
        reloadDataList();
    }


    public boolean reloadDataList() {
        String json = IOUtils.readFile(context, FILE_NAME);
        if (json == null) return false;
        dataList = new Gson().fromJson(json, new TypeToken<ArrayList<TaskItem>>() {}.getType());
        return dataList != null;
    }

    public boolean saveDataList() {
        return IOUtils.writeFile(context, FILE_NAME, new Gson().toJson(dataList));
    }

    public boolean addEntry(TaskItem taskItem, boolean save) {
        dataList.add(taskItem);
        if (!save) return true;
        return saveDataList();
    }

    public boolean removeEntry(int index, boolean save) {
        dataList.remove(index);
        if (!save) return true;
        return saveDataList();
    }

    public TaskItem getEntry(int index) {
        return dataList.get(index);
    }

    public boolean setEntry(int index, TaskItem taskItem, boolean save) {
        dataList.set(index, taskItem);
        if (!save) return true;
        return saveDataList();
    }

    public ArrayList<TaskItem> getListCopy() {
        return new ArrayList<>(dataList);
    }

}
