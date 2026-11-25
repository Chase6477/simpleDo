package de.jr.simpledo;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.time.LocalDate;
import java.util.ArrayList;

import de.jr.simpledo.databinding.MainFragmentBinding;

public class MainFragment extends Fragment {

    private MainFragmentBinding binding;

    private ArrayList<TaskItem> itemList = new ArrayList<>();
    private MainListAdapter adapter;
    private TaskData taskData;;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        taskData = new TaskData(requireContext());
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        itemList = taskData.getListCopy();

        binding.mainFab.setOnClickListener(v ->
                NavHostFragment.findNavController(MainFragment.this)
                .navigate(R.id.action_mainFragment_to_newTaskFragment));

        adapter = new MainListAdapter(requireContext(), itemList);

        binding.mainList.setAdapter(adapter);

        binding.mainList.setOnItemClickListener(
                (parent, v, position, id) ->
                        listItemClick(position));
        binding.mainList.setOnItemLongClickListener(
                (parent, v, position, id) ->
                        listItemLongClick(position));

    }

    public void listItemClick(int position) {
        TaskItem i = itemList.get(position);
        i.toggleCompleted();
        taskData.setEntry(position, i, true);
        itemList = taskData.getListCopy();
        adapter.notifyDataSetChanged();
    }

    public boolean listItemLongClick(int position) {
        taskData.removeEntry(position, true);
        adapter.remove(itemList.get(position));
        itemList = taskData.getListCopy();
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}