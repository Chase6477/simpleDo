package de.jr.simpledo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MainListAdapter extends ArrayAdapter<TaskItem> {
    public MainListAdapter(@NonNull Context context, ArrayList<TaskItem> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }


        // get the position of the view from the ArrayAdapter
        TaskItem item = getItem(position);

        if (item == null) {
            Log.e("LIST_ITEM", "List Item: " + position + " is NULL");
            return currentItemView;
        }

        TextView title = currentItemView.findViewById(R.id.list_item_title);
        TextView description = currentItemView.findViewById(R.id.list_item_description);
        TextView deadline = currentItemView.findViewById(R.id.list_item_deadline);
        View box = currentItemView.findViewById(R.id.list_item_gray_box);
        TextView complete_text = currentItemView.findViewById(R.id.list_item_gray_text);
        if (item.isCompleted()) {
            box.setVisibility(VISIBLE);
            complete_text.setVisibility(VISIBLE);
        } else {
            box.setVisibility(GONE);
            complete_text.setVisibility(GONE);
        }


        title.setText(item.getTitle());

        description.setText(item.getDescription());

        deadline.setText(TaskItem.getRelativeTimeSpan(item.getDateTime()));

        return currentItemView;
    }
}
