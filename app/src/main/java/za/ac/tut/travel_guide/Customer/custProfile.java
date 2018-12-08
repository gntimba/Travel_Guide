package za.ac.tut.travel_guide.Customer;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.ac.tut.travel_guide.DownloadImageTask;
import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.Validate;
import za.ac.tut.travel_guide.backWorker;
import za.ac.tut.travel_guide.httpGet;
import za.ac.tut.travel_guide.util.email;

import static za.ac.tut.travel_guide.IP.getIp;
import static za.ac.tut.travel_guide.registerActivity.encodeTobase64;

/**
 * Created by Thulani on 2017/08/29.
 */

public class custProfile extends AppCompatActivity {
    TextView Created,dob;
    EditText fname,lname;
    ImageView image;

    Button update;
    private String TAG = custProfile.class.getSimpleName();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust);
        fname=(EditText)findViewById(R.id.firstName);
        lname=(EditText)findViewById(R.id.lastName);
        dob=(TextView)findViewById(R.id.dob);
        Created=(TextView)findViewById(R.id.created);
        update=(Button)findViewById(R.id.update);
        image=(ImageView)findViewById(R.id.pic);
        //emails= getIntent().getExtras().getString("email");
        new custInfo().execute();
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
            Bitmap imagex = extras.getParcelable("data");
            image.setImageBitmap(imagex);
        }
    }

    private static final String baseUrlForImage = getIp()+"cust/";
    cust pro= new cust();
    private class custInfo extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            httpGet sh = new httpGet();
            // Making a request to url and getting response
            String url = getIp()+"profileCust.php";
            String jsonStr = sh.makeServiceCall(url, email.getEmail());

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray custome = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < custome.length(); i++) {
                        JSONObject c = custome.getJSONObject(i);
                       pro.setFname(c.getString("fname"));
                        pro.setLname(c.getString("lname"));
                        pro.setDob(c.getString("dob"));
                        pro.setCreated(c.getString("created_date"));
                        pro.setImage(c.getString("image"));
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;



        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lname.setText(pro.getLname());
            fname.setText(pro.getFname());
            dob.setText(pro.getDob());
            Created.setText(pro.getCreated());
            String urlForImage = baseUrlForImage + pro.getImage();
            new DownloadImageTask(image).execute(urlForImage);
        }
    }
    public void btnDatePicker(View view)
    {
        DialogFragment newFragment = new DatePickerFragmentupdate();
        newFragment.show(getFragmentManager(),"Date Picker");

    }
    public void btnUpdate(View view){




        if(dob.getText().toString().isEmpty())
        {
            dob.setError("Please select date of birth");

        }



        if(!Validate.fNameIsValid(fname.getText().toString()))
        {
            fname.setError("Invalid first name");
        }

        if(!Validate.lNameIsValid(lname.getText().toString()))
        {
            lname.setError("Invalid last name");
        }



        if( Validate.fNameIsValid(fname.getText().toString())
                && Validate.lNameIsValid(lname.getText().toString())
                &&  !dob.getText().toString().isEmpty())
        {

            String finame = fname.getText().toString();
            String laname = lname.getText().toString();
            String dateOB = dob.getText().toString();
            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            String imageData = encodeTobase64(bitmap);
            String type = "user_update";
            backWorker backgroundWorker = new backWorker(this);
            backgroundWorker.execute(type, email.getEmail(), finame, laname, dateOB,imageData);
            //finish();
        }
    }


}
