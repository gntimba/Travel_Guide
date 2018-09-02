package za.ac.tut.travel_guide;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import za.ac.tut.travel_guide.util.email;

import static za.ac.tut.travel_guide.IP.getIp;

public class to_do_list_main extends AppCompatActivity {


    private String TAG = to_do_list_main.class.getSimpleName();
    ArrayList<String> selectedItems = new ArrayList<>();
    String name, cost;
   // String place_id = "1";
    int count = 0;
    ListView lst;
    Button but;
    TextView warning,totas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_main);
        but = (Button) findViewById(R.id.btnListSubmit);
        warning = (TextView) findViewById(R.id.txtAct);
        totas = (TextView) findViewById(R.id.total);
        lst = (ListView) findViewById(R.id.listCheck);
        lst.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
            String url = getIp() + "getList.php";
            String jsonStr = sh.makeServiceCall(url, email.getPlace_id());

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
                            cost += c.getString("cost");
                        } else {
                            name += "," + c.getString("name");
                            cost += "," + c.getString("cost");
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
                cost = cost.substring(4);
                String[] namw = name.split(",");
                String[] cos = cost.split(",");
                String[] items = new String[count];


                for (int x = 0; x < count; x++) {
                    items[x] = namw[x] + " :R" + cos[x];
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(to_do_list_main.this, R.layout.custom_to_do_list, R.id.chkTxt, items);
                //warning.setText(items[0]);
                // Set the adapter to the ListView
                lst.setAdapter(adapter);
            } else {
                but.setVisibility(View.INVISIBLE);
                totas.setVisibility(View.INVISIBLE);
                //
            }
            // Close the progressdialog
            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem=((TextView)view).getText().toString();
                    double total=0.00;
                    if(selectedItems.contains(selectedItem)){
                        selectedItems.remove(selectedItem);
                    }else
                        selectedItems.add(selectedItem);

                    for(String item:selectedItems){
                        String[] spli=item.split(":");
                        for(int x=0;x<=spli.length-2;x++){
                            String[] tot=spli[spli.length-1].split("R");
                            total+=Double.parseDouble(tot[1]);

                        }
                        totas.setText("R"+total);

                    }
                }


            });
        }
    }
    public void OnSelectedItems(View v){
        String items="";

       // double total=0.0;
        String type="activity";
        for(String item:selectedItems){
            items="";
            String[] spli=item.split(":");
            for(int x=0;x<=spli.length-2;x++){
                items+=spli[x]+" ";
                //total+=Double.parseDouble(tot[1]);
            }
            backWorker backWorker = new backWorker(this);
            backWorker.execute(type, email.getPlace_id(), items,email.getEmail());
        }
       // backWorker backWorker = new backWorker(this);
      //  backWorker.execute(type, place_id, items,email);

       // Toast.makeText(to_do_list_main.this, items, Toast.LENGTH_SHORT).show();

    }
}
