package za.ac.tut.travel_guide;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.ac.tut.travel_guide.util.email;

import static za.ac.tut.travel_guide.IP.getIp;

public class to_list_view extends AppCompatActivity {
    private String TAG = to_list_view.class.getSimpleName();
    String name,date;
    ListView lst;
    int count=0;
    TextView warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_list_view);
        lst = (ListView) findViewById(R.id.listC);
        warning=(TextView) findViewById(R.id.warn);
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
            // arraylist = new ArrayList<HashMap<String, String>>();
            // Making a request to url and getting response
            String url = getIp() + "to_list.php";
            String jsonStr = sh.makeServiceCall(url,email.getEmail(), email.getPlace_id());

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray custome = jsonObj.getJSONArray("server_response");
                    // looping through All Contacts
                    for (int i = 0; i < custome.length(); i++) {

                        JSONObject c = custome.getJSONObject(i);
                        if (i == 0) {
                            name += c.getString("name");
                            date += c.getString("date");
                        } else {
                            name += "," + c.getString("name");
                            date += "," + c.getString("date");
                        }
                    count++;


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

            if (count >= 1) {
                warning.setVisibility(View.INVISIBLE);
                name = name.substring(4);
                date = date.substring(4);
                String[] namw = name.split(",");
                String[] dat = date.split(",");
                String[] items = new String[count];


                for (int x = 0; x < count; x++) {
                    items[x] = namw[x] + " " + dat[x];
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(to_list_view.this, R.layout.custom_list, R.id.chkTx, items);
                //warning.setText(items[0]);
                // Set the adapter to the ListView
                lst.setAdapter(adapter);
            } else {
                //but.setVisibility(View.INVISIBLE);
               // totas.setVisibility(View.INVISIBLE);
                //
            }
            // Close the progressdialog

        }
    }
}
