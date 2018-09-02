/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.travel_guide.Customer;

import android.app.Activity;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Tshepo
 */
public class ValuePasser {
    private static String cont = "";
    
    private static String email = "";
    private static String fname = "";
    private static String lname = "";
    private static String gender = "";
    private static Date date = new Date();
    
    public static void setter(String container)
    {
        cont = container;
    }
    
    public static void display(Activity actname)
    {
        Toast.makeText(actname, cont, Toast.LENGTH_LONG).show();
    }
    
    public static String getter()
    {
        return cont;
    }
    
    // Parsing Add Form contents to Add_Verification
    public static void addSetter(String email_add, String firstname, String lastname, String sex,Date dateOfBirth)
    {
        try {
            email = email_add;
            fname = firstname;
            lname = lastname;
            gender = sex;
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dt = sdf.format(dateOfBirth);
            date = (Date)sdf.parse(dt);
        } catch (ParseException ex) {
            Toast.makeText(null, "Internal Value Parsing Error ", Toast.LENGTH_LONG).show();
        }
    }
    public static String add_getEmail()
    {
        return email;
    }
    public static String add_getFname()
    {
        return fname;
    }
    public static String add_getLname()
    {
        return lname;
    }
    public static String add_getGender()
    {
        return gender;
    }
    public static Date add_getDOB()
    {
        return date;
    }
    

}
