package de.jr.simpledo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.gson.Gson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import de.jr.simpledo.databinding.NewTaskFragmentBinding;

public class NewTaskFragment extends Fragment {

    private NewTaskFragmentBinding binding;

    private LocalDate date = LocalDate.now().plusDays(1);
    private LocalTime time = null;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = NewTaskFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.newTaskSave.setOnClickListener(v -> {
            if (!save(requireContext())) return;
            NavHostFragment.findNavController(this).navigate(R.id.mainFragment);
        });

        binding.newTaskTitleEdit.requestFocus();

        binding.newTaskTitleEdit.post(() -> {
            InputMethodManager imm = (InputMethodManager)
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.showSoftInput(binding.newTaskTitleEdit, InputMethodManager.SHOW_IMPLICIT );
        });


        //Save alert handling
        binding.newTaskQuit.setOnClickListener(v -> cancel());

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        cancel();
                    }
                });

        MaterialToolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (needsCancelConfirm())
                cancel();
            else
                NavHostFragment.findNavController(this).navigateUp();

        });


        //Deadline Logic

        binding.newTaskDeadlineDate.setText(formatDate(date));
        binding.newTaskDeadlineTime.setText("None");

        binding.newTaskDeadlineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton compoundButton, boolean b) {
                if (b)
                    binding.newTaskDeadlineContainer.setVisibility(VISIBLE);
                else
                    binding.newTaskDeadlineContainer.setVisibility(GONE);
            }
        });

        deadLineDatePicker();
        deadLineTimePicker();
    }

    private void cancel() {
        Fragment fragment = this;

        if (needsCancelConfirm())
            new AlertDialog.Builder(requireContext())
                    .setTitle("Unsaved Changes")
                    .setMessage("All data is discarded")
                    .setCancelable(true)
                    .setPositiveButton("Keep editing", null)
                    .setNegativeButton("Discard changes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavHostFragment.findNavController(fragment).navigate(R.id.mainFragment);
                        }
                    }).create().show();
        else
            NavHostFragment.findNavController(fragment).navigate(R.id.mainFragment);
    }


    private boolean needsCancelConfirm() {
        if (binding == null) return false;
        return binding.newTaskTitleEdit.getText().length() > 0
                || binding.newTaskDescriptionEdit.getText().length() > 0
                || binding.newTaskDeadlineSwitch.isChecked();
    }

    private TaskItem createSave() {
        if (binding.newTaskTitleEdit.getText().length() <= 0) return null;

        return new TaskItem(
                binding.newTaskTitleEdit.getText().toString(),
                binding.newTaskDescriptionEdit.getText().toString(),
                binding.newTaskDeadlineSwitch.isChecked(),
                date,
                time,
                binding.newTaskDeadlineMessagesSwitch.isChecked()
        );
    }

    private boolean save(Context context) {
        TaskItem item = createSave();
        if (item == null) return false;

        return (new TaskData(context).addEntry(item, true));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onDestinationChange(NavController nc, NavDestination nd) {

    }

    public String formatTime(LocalTime localTime) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
        return localTime.format(formatter);
    }

    public String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("EEE, dd. MMM yyyy", Locale.getDefault());
        return localDate.format(formatter);
    }

    private void deadLineDatePicker() {
        binding.newTaskDeadlineDate.setOnClickListener(view1 -> {
            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select date")
                            .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                            .build();

            datePicker.addOnPositiveButtonClickListener(selection -> {
                date = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate();
                binding.newTaskDeadlineDate.setText(formatDate(date));
            });
            datePicker.show(getParentFragmentManager(), null);
        });
    }

    private void deadLineTimePicker() {
        binding.newTaskDeadlineTime.setOnClickListener(view1 -> {
            MaterialTimePicker timePicker =
                    new MaterialTimePicker.Builder()
                            .setTitleText("Select date")
                            .setNegativeButtonText("None")
                            .build();

            timePicker.addOnPositiveButtonClickListener(selection -> {
                time = LocalTime.of(timePicker.getHour(), timePicker.getMinute());
                binding.newTaskDeadlineTime.setText(formatTime(time));
            });
            timePicker.addOnNegativeButtonClickListener(v -> {
                time = null;
                binding.newTaskDeadlineTime.setText("None");
            });
            timePicker.show(getParentFragmentManager(), null);
        });
    }
}