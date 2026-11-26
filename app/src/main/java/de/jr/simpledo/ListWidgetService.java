package de.jr.simpledo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

import androidx.annotation.Nullable;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
