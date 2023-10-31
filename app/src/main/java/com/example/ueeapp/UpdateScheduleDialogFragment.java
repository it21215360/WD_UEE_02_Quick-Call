package com.example.ueeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class UpdateScheduleDialogFragment extends DialogFragment {

    public interface OnUpdateScheduleListener {
        void onUpdateSchedule(ScheduleData scheduleData);
    }

    public UpdateScheduleDialogFragment(ScheduleData schedule, List<ScheduleData> scheduleList, ScheduleAdapter scheduleAdapter) {

    }

    public UpdateScheduleDialogFragment(ScheduleData schedule, List<ScheduleData> scheduleList) {

    }

    /*
    public interface OnUpdateScheduleListener {
        void onUpdateSchedule(String updatedTitle, String updatedTime, String updatedAddress);
    } */
    private EditText editTitle, editAddress;
    private EditText editTime, editCus;
    private MaterialTextView editDate;

    private ScheduleData schedule;
    private List<ScheduleData> scheduleList;
    private ScheduleAdapter scheduleAdapter;

    public UpdateScheduleDialogFragment(ScheduleData schedule) {
        this.schedule = schedule;
        //this.scheduleList = scheduleList;
        //this.scheduleAdapter = scheduleAdapter;
    }

    private void openUpdateDialog(FragmentActivity activity, ScheduleData scheduleToEdit) {
        // Create a new instance of UpdateScheduleDialogFragment with the schedule object
        UpdateScheduleDialogFragment updateScheduleDialogFragment = new UpdateScheduleDialogFragment(scheduleToEdit);

        // Pass the ScheduleData object to the dialog
        Bundle args = new Bundle();
        args.putSerializable("schedule", scheduleToEdit);
        updateScheduleDialogFragment.setArguments(args);

        // Show the dialog fragment
        updateScheduleDialogFragment.show(getParentFragmentManager(), "update_schedule_dialog");
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_schedule_layout, container, false);

        editTitle = view.findViewById(R.id.updatetitle);
        editTime = view.findViewById(R.id.updatetime);
        editAddress = view.findViewById(R.id.updateaddress);
        editDate = view.findViewById(R.id.pickerdaterd);
        editCus = view.findViewById(R.id.cusIDrd);

        Bundle args = getArguments();
        if (args != null) {
            ScheduleData scheduleData = (ScheduleData) args.getSerializable("schedule");
            if (scheduleData != null) {
                // Now you can access the properties of scheduleData safely
                String title = scheduleData.getTitle();
                String time = scheduleData.getTime();
                String address = scheduleData.getAddress();
                String date = scheduleData.getDate();

                editTitle.setText(title);
                editTime.setText(time);
                editAddress.setText(address);
                editDate.setText(date); // Set the date value
            }
        }


        //mod
        // Retrieve the ScheduleData object from the arguments
        ScheduleData scheduleData = (ScheduleData) getArguments().getSerializable("schedule");

        // Set hints and initial values in input fields
        editTitle.setHint(scheduleData.getTitle());
        editTime.setHint(scheduleData.getTime());
        editAddress.setHint(scheduleData.getAddress());
        editDate.setText(scheduleData.getDate()); // Set the date value
        editCus.setHint(scheduleData.getCusId());
        // ...

        Button btnUpdate = view.findViewById(R.id.schdlupdt);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTitle = editTitle.getText().toString();
                String updatedTime = editTime.getText().toString();
                String updatedAddress = editAddress.getText().toString();

                if (schedule != null) { // Check if schedule is not null
                    // Create an updated ScheduleData object
                    ScheduleData updatedSchedule = new ScheduleData(updatedTitle, updatedTime, "", updatedAddress, schedule.getDate());

                    if (getTargetFragment() instanceof OnUpdateScheduleListener) {
                        ((OnUpdateScheduleListener) getTargetFragment()).onUpdateSchedule(updatedSchedule);

                        // Show a Toast message when the update is successful
                        Toast.makeText(requireContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where schedule is null (e.g., display an error message or log an error)
                    // You may want to dismiss the dialog in this case
                    dismiss();
                }
            }
        });



        AppCompatImageView closeButton1 = view.findViewById(R.id.closeButton1);
        closeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog when the 'closeButton1' is clicked
            }
        });

        return view;
    }
}
