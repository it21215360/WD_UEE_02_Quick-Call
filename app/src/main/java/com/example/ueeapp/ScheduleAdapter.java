package com.example.ueeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

        import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ScheduleData> scheduleList;
    private Context context;
    private OnDeleteClickListener deleteClickListener;
    private OnUpdateClickListener updateClickListener;
    //private ScheduleAdapter.OnUpdateClickListener updateClickListener;

    public interface OnUpdateClickListener {
        void onUpdateClick(ScheduleData item);
    }

    public void setUpdateClickListener(OnUpdateClickListener listener) {
        this.updateClickListener = listener;
    }

    public ScheduleAdapter(Context context, List<ScheduleData> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scheduler_recycler_view, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        ScheduleData schedule = scheduleList.get(position);

        holder.dateTextView.setText(schedule.getDate());
        holder.titleTextView.setText(schedule.getTitle());
        holder.timeTextView.setText(schedule.getTime());
        holder.cusTextView.setText(schedule.getCusId());

        // Get a reference to the "scdldelete" ImageButton
        ImageButton deleteButton = holder.deleteButton;
        // Get a reference to the "scdlupdate" ImageButton
        ImageButton updateButton = holder.updateButton;

        //Set a click listener for the update button
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;

                    // Pass the ScheduleData item to the dialog
                    openUpdateDialog(schedule, activity);
                }
            }
        });


        // Set a click listener for the delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    ScheduleData itemToDelete = scheduleList.get(adapterPosition);
                    // Notify the fragment to delete the item from the database
                    if (deleteClickListener != null) {
                        deleteClickListener.onDeleteClick(itemToDelete);
                    }
                }
            }
        });
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(ScheduleData item);
    }

    // Set the delete click listener
    public void setDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    private void showUpdateDialog(ScheduleData schedule) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        UpdateScheduleDialogFragment dialogFragment = new UpdateScheduleDialogFragment(schedule, scheduleList);

        Bundle args = new Bundle();
        args.putString("title", schedule.getTitle());
        args.putString("time", schedule.getTime());
        args.putString("address", schedule.getAddress());

        dialogFragment.setArguments(args);

        dialogFragment.show(fragmentManager, "update_schedule_dialog");
    }

    private void openUpdateDialog(ScheduleData schedule, FragmentActivity activity) {
        UpdateScheduleDialogFragment updateScheduleDialogFragment = new UpdateScheduleDialogFragment(schedule, scheduleList);

        // Pass the ScheduleData object to the dialog
        Bundle args = new Bundle();
        args.putSerializable("schedule", schedule);
        updateScheduleDialogFragment.setArguments(args);

        updateScheduleDialogFragment.show(activity.getSupportFragmentManager(), "update_schedule_dialog");
    }

    //del if needed
    public void onDeleteClick(ScheduleData itemToDelete) {
        // Show a confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this item?");

        // Add "Yes" button for confirmation
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, delete the item
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(itemToDelete);
                }
            }
        });

        // Add "No" button for canceling
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled, do nothing
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //----

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView timeTextView;
        public TextView dateTextView;
        public TextView cusTextView;
        public ImageButton deleteButton;
        public ImageButton updateButton;
        AppCompatButton btnUpdate;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.readdate);
            titleTextView = itemView.findViewById(R.id.readtitle);
            timeTextView = itemView.findViewById(R.id.readtime);
            cusTextView = itemView.findViewById(R.id.readcusid);
            deleteButton = itemView.findViewById(R.id.scdldelete);
            updateButton = itemView.findViewById(R.id.scdlupdate);
            btnUpdate = itemView.findViewById(R.id.schdlupdt);

            // Set a click listener for the update button
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (updateClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            updateClickListener.onUpdateClick(scheduleList.get(position));
                        }
                    }
                }
            });
        }
    }
}

