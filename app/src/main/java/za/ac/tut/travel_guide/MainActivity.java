package za.ac.tut.travel_guide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import za.ac.tut.travel_guide.Customer.signing_up;
import za.ac.tut.travel_guide.Customer.user_main;


public class MainActivity extends AppCompatActivity {
    EditText user,pass;
    RadioButton admin,client,users;
    SharedPreferences sp;

    // TextView txtPass,txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        if(sp.getBoolean("logged",false)){
            goToMainActivity(sp.getString("email",""));
        }

        user=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.password);
        admin=(RadioButton)findViewById(R.id.rd_admin);
        client=(RadioButton)findViewById(R.id.rd_client);
        users=(RadioButton)findViewById(R.id.users) ;
        users.toggle();
        Button btnReg = (Button)findViewById(R.id.btnLinkToRegisterScreen);
        Button btnC=(Button)findViewById(R.id.btnLinkToRegisterScreenCustomer) ;
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,registerActivity.class);
                startActivity(intent);
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,signing_up.class);
                startActivity(intent);
            }
        });
        // txtPass=(TextView)findViewById(R.id.txtPass);
        //txtEmail=(TextView)findViewById(R.id.txtEmail);

    }



    public void goToMainActivity(String email){
        Intent i = new Intent(this,user_main.class);
        i.putExtra("email", email);
        startActivity(i);
    }
    public void OnLogin(View v) throws IOException, InterruptedException {
        try {
            String username = user.getText().toString();
            String password = pass.getText().toString();
            // Check for empty data in the form

            if (!username.isEmpty() && !password.isEmpty()) {
                String type = "";
                if (client.isChecked()) {
                    type = "login";
                } else if (admin.isChecked()) {
                    type = "admin";
                } else if (users.isChecked()) {
                    type = "user";
                }


                backWorker backWorker = new backWorker(this);
                backWorker.execute(type, username, password);
                user.setText("");
                pass.setText("");

            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }
        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),
                    "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

}
