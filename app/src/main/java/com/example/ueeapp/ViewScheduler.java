package com.example.ueeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ViewScheduler extends Fragment implements UpdateScheduleDialogFragment.OnUpdateScheduleListener{

    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private List<ScheduleData> scheduleList;
    private ScheduleData schedule;

    //notification

    //****


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw = inflater.inflate(R.layout.fragment_view_scheduler, container, false);

        // Initialize the RecyclerView
        recyclerView = vw.findViewById(R.id.rvretrieve);

        // Initialize the list
        scheduleList = new ArrayList<>();

        // Create an instance of the adapter and set it to the RecyclerView
        scheduleAdapter = new ScheduleAdapter(requireContext(), scheduleList);
        recyclerView.setAdapter(scheduleAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Retrieve data from the database and add it to the scheduleList
        ScheduleDAO scheduleDAO = new ScheduleDAO(requireContext());
        scheduleDAO.open();
        scheduleList.clear();
        scheduleList.addAll(scheduleDAO.getAllSchedules1());
        scheduleDAO.close();

        //notification
        // Schedule notifications for each schedule
        for (ScheduleData schedule : scheduleList) {
            scheduleDAO.scheduleNotification(schedule.getId(), schedule.getTime());
        }

        scheduleDAO.close();
        //***

        // Notify the adapter that the data has changed
        scheduleAdapter.notifyDataSetChanged();

        // Set the updateClickListener for the adapter
        scheduleAdapter.setUpdateClickListener(new ScheduleAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(ScheduleData item) {
                // Handle the update operation here
                openUpdateDialog(item); // Call openUpdateDialog when the update button is clicked
            }
        });

        // Set the deleteClickListener for the adapter
        scheduleAdapter.setDeleteClickListener(new ScheduleAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(ScheduleData item) {
                // Handle the delete operation here
                deleteItemFromDatabase(item);
            }
        });

        return vw;
    }

    private void deleteItemFromDatabase(ScheduleData itemToDelete) {
        ScheduleDAO scheduleDAO = new ScheduleDAO(requireContext());
        scheduleDAO.open();
        scheduleDAO.deleteItemFromDatabase(itemToDelete);
        scheduleList.remove(itemToDelete);
        scheduleAdapter.notifyDataSetChanged();
        scheduleDAO.close();
    }

    private void openUpdateDialog(ScheduleData scheduleToEdit) {
        this.schedule = scheduleToEdit; // Set the currently edited schedule

        // Create a new instance of UpdateScheduleDialogFragment
        UpdateScheduleDialogFragment updateScheduleDialogFragment = new UpdateScheduleDialogFragment(scheduleToEdit);

        // Pass the ScheduleData object to the dialog
        Bundle args = new Bundle();
        args.putSerializable("schedule", scheduleToEdit);
        updateScheduleDialogFragment.setArguments(args);

        // Show the dialog fragment
        updateScheduleDialogFragment.show(getParentFragmentManager(), "update_schedule_dialog");
    }


    @Override
    public void onUpdateSchedule(ScheduleData updatedSchedule) {
        // Update the schedule in the database using your DAO
        ScheduleDAO scheduleDAO = new ScheduleDAO(requireContext());
        scheduleDAO.open();
        scheduleDAO.updateSchedule(updatedSchedule);
        scheduleDAO.close();

        // Update the dataset with the updated schedule
        if (schedule != null) {
            int positionToUpdate = scheduleList.indexOf(schedule); // Find the position to update
            if (positionToUpdate != -1) {
                scheduleList.set(positionToUpdate, updatedSchedule);

                // Notify the adapter about the changes
                scheduleAdapter.notifyItemChanged(positionToUpdate);
            }
        }
    }

}