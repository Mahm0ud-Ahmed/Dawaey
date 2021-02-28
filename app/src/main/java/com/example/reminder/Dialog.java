package com.example.reminder;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Dialog extends DialogFragment {

    private String type;
    Choice mListener;

    public Dialog(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (type.equals("AlertDialog")) {
            final ArrayList<Integer> list = new ArrayList<>();
            final String[] arr = getResources().getStringArray(R.array.day);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("تكرار الدواء")
                    .setMultiChoiceItems(arr, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if (isChecked) {
                                list.add(which);
                            } else {
                                list.remove(which);
                            }
                            Collections.sort(list);
                        }
                    })
                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.setPositiveButtonListener(list, arr);
                        }
                    })
                    .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.setNegativeButtonListener(list, arr);
                            dialog.dismiss();
                        }
                    });
            return builder.create();


        } else if (type.equals("TimePicker")) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, false);
        }
        return null;
    }

    public interface Choice{
        void setPositiveButtonListener(ArrayList<Integer> currentList, String[] originalList);
        void setNegativeButtonListener(ArrayList<Integer> currentList, String[] originalList);
    }
}
