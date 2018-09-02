package za.ac.tut.travel_guide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import za.ac.tut.travel_guide.Customer.signing_up;


public class MainActivity extends AppCompatActivity {
    EditText user,pass;
    RadioButton admin,client,users;
    // TextView txtPass,txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    public void OnLogin(View v) throws IOException, InterruptedException {
        String username =user.getText().toString();
        String password =pass.getText().toString();
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
        }

}
