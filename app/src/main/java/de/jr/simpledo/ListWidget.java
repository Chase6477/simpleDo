package de.jr.simpledo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.compose.runtime.annotation.FrequentlyChangingValue;

public class ListWidget extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "list_widget_item_extra";
    private static final String STRIKE_ACTION = "list_widget_strike_action";
    private static final String UPDATE_BUTTON = "list_widget_update_button";
    private static final String OPEN_BUTTON = "list_widget_open_button";

    private MainListAdapter adapter;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.list_widget
        );

        Intent adapterIntent = new Intent(context, ListWidgetService.class);
        adapterIntent.putExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId
        );

        adapterIntent.setData(
                Uri.parse(adapterIntent.toUri(Intent.URI_INTENT_SCHEME))
        );

        views.setRemoteAdapter(R.id.widget_list, adapterIntent);
        views.setEmptyView(
                R.id.widget_list,
                R.id.widget_empty_view
        );


        //OnClickListener
        Intent clickIntent = new Intent(context, ListWidget.class);
        setIntentData(clickIntent, appWidgetId);
        clickIntent.setAction(STRIKE_ACTION);
        PendingIntent pendingClickIntent = PendingIntent.getBroadcast(
                context, 0, clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        views.setPendingIntentTemplate(R.id.widget_list, pendingClickIntent);


        //UpdateButton
        Intent updateButtonIntent = new Intent(context, ListWidget.class);
        setIntentData(updateButtonIntent, appWidgetId);
        updateButtonIntent.setAction(UPDATE_BUTTON);
        PendingIntent pendingUpdateButtonIntent = PendingIntent.getBroadcast(
                context, 0, updateButtonIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        views.setOnClickPendingIntent(R.id.button, pendingUpdateButtonIntent);

        //OpenButton
        Intent openAppIntent = new Intent(context, MainActivity.class);

        openAppIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        PendingIntent pendingOpenAppIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT |
                        PendingIntent.FLAG_IMMUTABLE
        );

        views.setOnClickPendingIntent(
                R.id.button2,
                pendingOpenAppIntent
        );


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    private static Intent setIntentData(Intent i, int appWidgetId) {
        i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        i.setData(Uri.parse(i.toUri(Intent.URI_INTENT_SCHEME)));
        return i;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent == null) return;
        if (intent.getAction() == null) return;

        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        );
        int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);

        switch (intent.getAction()) {
            case STRIKE_ACTION:
                TaskData td = new TaskData(context);
                TaskItem ti = td.getEntry(viewIndex);
                ti.toggleCompleted();
                if (!td.setEntry(viewIndex, ti, true)) return;

                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                        appWidgetId,
                        R.id.widget_list
                );
                break;
            case UPDATE_BUTTON:
                AppWidgetManager.getInstance(context).notifyAppWidgetViewDataChanged(
                        appWidgetId,
                        R.id.widget_list
                );
                break;
            case OPEN_BUTTON:

                break;
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            ListWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}