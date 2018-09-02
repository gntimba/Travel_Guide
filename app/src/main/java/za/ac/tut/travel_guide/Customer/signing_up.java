package za.ac.tut.travel_guide.Customer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.View;
import android.app.DialogFragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.Validate;

import static za.ac.tut.travel_guide.registerActivity.encodeTobase64;


public class signing_up extends AppCompatActivity{

    ImageButton btn;
    int year_x, month_x, day_x;
    static final int DILOG_ID = 0;

    //onSignUp variable declaration
    EditText email_addrs, password, password2,firstname , lastname, secA;
    Spinner gender, secQ;
    ImageView imageView;
    TextView dob;

    String e_address, passwd, fname, lname, gendr,sQ,sA, dateOB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signing_up);

        email_addrs = (EditText)findViewById(R.id.et_email_address) ;
        password = (EditText)findViewById(R.id.et_password) ;
        password2 = (EditText)findViewById(R.id.et_c_password) ;
        firstname = (EditText)findViewById(R.id.et_fname) ;
        lastname = (EditText)findViewById(R.id.et_lname) ;
        dob = (TextView)findViewById(R.id.et_dob) ;
        imageView =(ImageView) findViewById(R.id.imageCust);
        gender = (Spinner)findViewById(R.id.sp_sex) ;
        secA = (EditText)findViewById(R.id.et_security_a) ;
        secQ = (Spinner)findViewById(R.id.sp_security_q) ;
        ImageButton btnDatePcker =  (ImageButton) findViewById(R.id.btnDob) ;



        //generate spinner items for security questions
        Spinner mySp = (Spinner) findViewById(R.id.sp_security_q);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(signing_up.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySp.setAdapter(myAdapter);

        //generate spinner items for sex
        Spinner mySpSex = (Spinner) findViewById(R.id.sp_sex);
        myAdapter = new ArrayAdapter<String>(signing_up.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sex));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpSex.setAdapter(myAdapter);

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        Button btnSubmitt = (Button) findViewById(R.id.btnSubmit) ;
        // btnSubmitt.setEnabled(false);
        btnSubmitt.setTextColor(Color.RED);


    }
    public void ViewPic(View view){
        Intent imageDownload = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imageDownload.putExtra("crop", "true");
        imageDownload.putExtra("aspectX", 1);
        imageDownload.putExtra("aspectY", 1);
        imageDownload.putExtra("outputX", 200);
        imageDownload.putExtra("outputY", 200);
        imageDownload.putExtra("return-data", true);
        startActivityForResult(imageDownload, 2);}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK && data != null)
        {
            Bundle extras = data.getExtras();
            Bitmap image = extras.getParcelable("data");
            imageView.setImageBitmap(image);
        }
    }




    public void btnDatePicker(View view)
    {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"Date Picker");

    }

    public void btnSubmit(View view){

        if(!Validate.emailIsValid(email_addrs.getText().toString()))
        {
            email_addrs.setError("Invalid Email Address");
        }

        String p1  =password.getText().toString(), p2  = password2.getText().toString();
        if(!(p1.equals(p2)))
        {
            password2.setError("Passwords do not match");
        }

        if(dob.getText().toString().isEmpty())
        {
            dob.setError("Please select date of birth");

        }



        if(!Validate.fNameIsValid(firstname.getText().toString()))
        {
            firstname.setError("Invalid first name");
        }

        if(!Validate.lNameIsValid(lastname.getText().toString()))
        {
            lastname.setError("Invalid last name");
        }

        if(secA.getText().toString().length() < 2)
        {
            secA.setError("Invalid security answer");
        }

        if(Validate.emailIsValid(email_addrs.getText().toString())
                && p1.equals(p2)
                && Validate.fNameIsValid(firstname.getText().toString())
                && Validate.lNameIsValid(lastname.getText().toString())
                && secA.getText().toString().length() >= 2
                && !dob.getText().toString().isEmpty())
        {
            String e_address = email_addrs.getText().toString();
            String passwd = password.getText().toString();
            String fname = firstname.getText().toString();
            String lname = lastname.getText().toString();
            String gendr = gender.getSelectedItem().toString();
            String sQ = secQ.getSelectedItem().toString();
            String sA = secA.getText().toString();
            String dateOB = dob.getText().toString();
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            String imageData = encodeTobase64(bitmap);
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute(type, e_address, passwd, fname, lname, gendr, dateOB, sQ, sA,imageData);
            //finish();
        }
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}