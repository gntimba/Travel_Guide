package za.ac.tut.travel_guide.Customer;

/**
 * Created by Tshepo on 9/20/2017.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import za.ac.tut.travel_guide.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT,this,year, month, day);
        DatePicker dp = dpd.getDatePicker();
        //Set the DatePicker minimum date selection to current date

        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 100);
        dp.setMinDate(c.getTimeInMillis());
        //dp.setMinDate(System.currentTimeMillis() - 1000);// Alternate way to get the current day

        //Add 6 days with current date
        //c.add(Calendar.DAY_OF_MONTH,365*100);

        //Set the maximum date to select from DatePickerDialog
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 5);
        dp.setMaxDate(c.getTimeInMillis());
        return dpd;
    }
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Do something with the date chosen by the user
        TextView tv = (TextView) getActivity().findViewById(R.id.et_dob);
        tv.setText("Date changed...");
        tv.setText(tv.getText() + "\nYear: " + year);
        tv.setText(tv.getText() + "\nMonth: " + month);
        tv.setText(tv.getText() + "\nDay of Month: " + day);

        String stringOfDate = year + "-" + month + "-" + day ;
        tv.setText(stringOfDate);
    }
}