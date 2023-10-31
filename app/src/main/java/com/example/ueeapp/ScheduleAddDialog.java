package com.example.ueeapp;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;
import java.util.regex.Pattern;

public class ScheduleAddDialog extends AppCompatDialogFragment {

    private EditText title;
    private EditText time;
    private EditText cusId;
    private EditText address;
    private Button save;
    private TextView date;
    private SharedViewModel sharedViewModel;

    //database
    private ScheduleDAO scheduleDAO;
    //private DatabaseReference databaseReference;

    //exp
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate your layout and set up UI elements
        View view = inflater.inflate(R.layout.add_schedule_layout, container, false);
        date = view.findViewById(R.id.pickerdate);

        // Obtain the shared ViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Create a new ScheduleDAO instance
        scheduleDAO = new ScheduleDAO(requireContext());

        // Open the database connection
        scheduleDAO.open();

        // Observe changes to the selected date
        sharedViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String selectedDate) {
                // Update UI elements with the selected date
                date.setText(selectedDate);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Close the database when the fragment is destroyed
        scheduleDAO.close();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_schedule_layout,null);


        builder.setView(view);
        /* Use the custom title view
        //View titleView = inflater.inflate(R.layout.add_schedule_layout, null);
        //builder.setCustomTitle(view); */

        // Handle the close button click
        ImageView closeButton = view.findViewById(R.id.closeButton1);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
            }
        });

        title = view.findViewById(R.id.entertitle);
        time = view.findViewById(R.id.entertime);
        cusId = view.findViewById(R.id.entercusID);
        address = view.findViewById(R.id.enteraddress);
        save = view.findViewById(R.id.schdlsave);
        date = view.findViewById(R.id.pickerdate);


        // Retrieve the selected date from arguments
        String selectedDate = getArguments().getString("selectedDate");

        // Find the pickerdate TextView within the custom title view
        TextView date = view.findViewById(R.id.pickerdate);
        date.setText(selectedDate);


        time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this example
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredTime = s.toString().trim();

                // Check if the entered time is exactly 4 characters
                if (enteredTime.length() == 5) {
                    String hours = enteredTime.substring(0, 2);
                    String minutes = enteredTime.substring(3);

                    try {
                        int hoursValue = Integer.parseInt(hours);
                        int minutesValue = Integer.parseInt(minutes);

                        // Check if hours and minutes are within valid ranges
                        if (hoursValue >= 0 && hoursValue <= 23 && minutesValue >= 0 && minutesValue <= 59) {
                            // The entered time is valid
                        } else {
                            time.setError("Invalid time. Hours must be between 0 and 23, and minutes between 0 and 59.");
                        }
                    } catch (NumberFormatException e) {
                        time.setError("Invalid time format. Please use HHMM.");
                    }
                } else {
                    time.setError("Invalid time format. Please use HHMM.");
                }
            }
        });

        // Set an OnClickListener for the "Save" button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from input fields
                String enteredTitle = title.getText().toString();
                String enteredTime = time.getText().toString();
                String enteredCusId = cusId.getText().toString();
                String enteredAddress = address.getText().toString();
                String selectedDate = date.getText().toString(); // Use the selected date from the dialog

                // Create a unique key for the new entry
                long entryKey = scheduleDAO.insertSchedule(enteredTitle, enteredTime, enteredCusId, enteredAddress, selectedDate);

                if (entryKey != -1) {
                    // Data was inserted successfully
                    Toast.makeText(requireContext(), "Data inserted successfully!", Toast.LENGTH_SHORT).show();
                    //make notification - delete if necessary

                    //****
                } else {
                    // Data insertion failed
                    Toast.makeText(requireContext(), "Data insertion failed.", Toast.LENGTH_SHORT).show();
                }

                dismiss(); // Close the dialog
            }
        });


        // Initialize the Firebase Database reference
        //databaseReference = FirebaseDatabase.getInstance().getReference("Scheduler");

        //builder.setCustomTitle(titleView);
        return builder.create();

    }

    //exp notification
    public void makeNotification(){
        String chanelID= "CHANEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(),chanelID);

        builder.setSmallIcon(R.drawable.baseline_notifications);
        builder.setContentTitle("You have a schedule")
                .setContentText("You have a schedule in an hour")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    /*
    public void save(View view){

        // Get data from input fields
        String enteredTitle = title.getText().toString();
        String enteredTime = time.getText().toString();
        String enteredCusId = cusId.getText().toString();
        String enteredAddress = address.getText().toString();
        String selectedDate = date.getText().toString(); // Use the selected date from the dialog

        // Create a unique key for the new entry
        String entryKey = databaseReference.push().getKey();

        // Create a data object
        ScheduleData scheduleData = new ScheduleData(enteredTitle, enteredTime, enteredCusId, enteredAddress, selectedDate);

        // Add the data to the Firebase database under the unique key
        databaseReference.child(entryKey).setValue(scheduleData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data saved successfully
                            dismiss();
                        } else {
                            // Handle the error, e.g., log it
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        // Close the dialog
        dismiss();
    }*/


}
