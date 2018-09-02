package za.ac.tut.travel_guide.review;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import za.ac.tut.travel_guide.Customer.ListViewAdapter;
import za.ac.tut.travel_guide.Customer.user_main;
import za.ac.tut.travel_guide.IP;
import za.ac.tut.travel_guide.JSONfunctions;
import za.ac.tut.travel_guide.R;
import za.ac.tut.travel_guide.httpGet;
import za.ac.tut.travel_guide.util.email;

import static za.ac.tut.travel_guide.IP.getIp;

public class reviews_main extends AppCompatActivity {
    private static final String TAG = reviews_main.class.getSimpleName();
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<HashMap<String, String>> arraylist;
    ///String place_id;
    adapterReview adapter;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_main);
        new DownloadJSON().execute();
    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            httpGet sh = new httpGet();
            arraylist = new ArrayList<HashMap<String, String>>();
            // Making a request to url and getting response
            String url = getIp()+"getreviews.php";
            String jsonStr = sh.makeServiceCall(url, email.getPlace_id());

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray custome = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < custome.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject c = custome.getJSONObject(i);
                        map.put("comment", c.getString("comment"));
                        map.put("date", c.getString("date"));
                        map.put("fname", c.getString("fname"));
                        map.put("lname", c.getString("lname"));
                        map.put("rating", c.getString("rating"));
                        arraylist.add(map);

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
        protected void onPostExecute(Void args) {
            // Locate the listview in listview_main.xml
            listview = (ListView) findViewById(R.id.reviewLayout);
            // Pass the results into ListViewAdapter.java
            //  Collections.
           // Collections.sort(arraylist, new user_main.MyCustomComparator());
            adapter = new adapterReview(reviews_main.this, arraylist);

            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
        }
    }
}
