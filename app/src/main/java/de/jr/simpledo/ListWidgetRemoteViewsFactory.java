package de.jr.simpledo;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

public class ListWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private final int widgetID;
    private List<TaskItem> list;

    public ListWidgetRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

        if (widgetID == -1)
            Log.e("Widget Error", "There was no widgetID broadcasted via the intent");

        //onDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_widget_item);
        remoteViews.setTextViewText(R.id.widget_list_title, list.get(i).getTitle());
        remoteViews.setTextViewText(R.id.widget_list_deadline,
                TaskItem.getRelativeTimeSpan(list.get(i).getDateTime()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            remoteViews.setColorStateList(R.id.widget_list_deadline, "setTextColor", android.R.color.system_primary_fixed_dim);
            remoteViews.setColorStateList(R.id.widget_list_title, "setTextColor", android.R.color.system_primary_fixed_dim);
        }
        if (list.get(i).isCompleted()) {
            remoteViews.setInt(
                    R.id.widget_list_title,
                    "setPaintFlags",
                    Paint.STRIKE_THRU_TEXT_FLAG
            );
            if (list.get(i).isCompleted()) {
                remoteViews.setInt(
                        R.id.widget_list_deadline,
                        "setPaintFlags",
                        Paint.STRIKE_THRU_TEXT_FLAG
                );
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                remoteViews.setColorStateList(R.id.widget_list_deadline, "setTextColor", android.R.color.system_secondary_dark);
                remoteViews.setColorStateList(R.id.widget_list_title, "setTextColor", android.R.color.system_secondary_dark);
            }
        }

        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onCreate() {
        list = new TaskData(context).getListCopy();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
    }
}
