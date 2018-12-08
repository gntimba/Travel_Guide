package za.ac.tut.travel_guide;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import za.ac.tut.travel_guide.util.email;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import za.ac.tut.travel_guide.util.email;

import static za.ac.tut.travel_guide.IP.getIp;

public class profileView extends AppCompatActivity {
JSONObject jsonObject;
    JSONArray jsonArray;
    TextView nam,em,sa,sq;
    ImageView imageView;
    String email;
    private String TAG = profileView.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        nam=(TextView)findViewById(R.id.txt_name);
        em=(TextView)findViewById(R.id.txt_email);
        sa=(TextView)findViewById(R.id.txt_answer);
        sq=(TextView)findViewById(R.id.txt_question);
        imageView=(ImageView)findViewById(R.id.pp_image);
        new proInfo().execute();

    }
    private static final String baseUrlForImage = getIp()+"images/";
  getPro pro= new getPro();

    private class proInfo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            httpGet sh = new httpGet();
            // Making a request to url and getting response
            String url = getIp()+"profile.php";
            String jsonStr = sh.makeServiceCall(url, getIntent().getExtras().getString("email"));

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String name = c.getString("name");
                        String email = c.getString("client_e_address");
                        String A=c.getString("security_a");
                        String Q=c.getString("security_q");
                        String image=c.getString("image");
                        pro.setImage(image);
                        pro.setEmail(email);
                        pro.setNames(name);
                        pro.setsAn(A);
                        pro.setsQu(Q);
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
            nam.setText(pro.getNames());
            em.setText(pro.getEmail());
            sa.setText(pro.getsAn());
            sq.setText(pro.getsQu());
            String urlForImage = baseUrlForImage + pro.getImage();
            new DownloadImageTask(imageView).execute(urlForImage);
        }
    }





}

