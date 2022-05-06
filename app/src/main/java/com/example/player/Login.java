package com.example.player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    TextView signUp;
    Button btn;
    EditText mail, password;
    String m, p;
    int count=0;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp = (TextView) findViewById(R.id.textView2);
        btn = (Button) findViewById(R.id.button);
        mail = (EditText) findViewById(R.id.editTextMail);
        password = (EditText) findViewById(R.id.editTextPassword);
        db=new Database(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m = mail.getText().toString();
                p = password.getText().toString();

                if (count>=3){
                    Toast.makeText(Login.this, "Wrong password or mail 3 times. Please create an account.", Toast.LENGTH_LONG).show();
                    Create(view);
                }
                else{
                    if (m.equals("")||p.equals("")){
                        Toast.makeText(Login.this, "Please enter your mail and password.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Boolean check=db.checkmailpass(m,p);
                        if (check==true) {
                            Toast.makeText(Login.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            Intent play = new Intent(Login.this, MainActivity.class);
                            finish();
                            startActivity(play);
                        }
                        else
                            count=count+1;
                        Toast.makeText(Login.this, "Wrong password or mail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void Create(View v){
        Intent intent =new Intent(Login.this, SignIn.class);
        startActivity(intent);
    }

}