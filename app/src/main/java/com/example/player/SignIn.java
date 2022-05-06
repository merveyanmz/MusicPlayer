package com.example.player;

import androidx.appcompat.app.AppCompatActivity;
import static java.net.Proxy.Type.HTTP;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    protected EditText Name,Surname,Mail,Phone,Password,Password2;
    protected Button btn;
    String name,surname,mail,phone,password,password2;

    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Name =(EditText) findViewById(R.id.editTextName);
        Surname =(EditText) findViewById(R.id.editTextSurname);
        Mail =(EditText) findViewById(R.id.editTextEMail);
        Phone =(EditText) findViewById(R.id.editTextPhone);
        Password =(EditText) findViewById(R.id.editTextTextPassword);
        Password2 =(EditText) findViewById(R.id.Password2);
        btn =(Button) findViewById(R.id.button2);

        db=new Database(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=Name.getText().toString();
                surname=Surname.getText().toString();
                mail=Mail.getText().toString();
                phone=Phone.getText().toString();
                password=Password.getText().toString();
                password2=Password2.getText().toString();


                if( !TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname) && !TextUtils.isEmpty(mail) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) &&!TextUtils.isEmpty(password2)) {

                    if (password.equals(password2)){
                        Boolean check=db.checkmail(mail);
                        if (check==true){
                            Boolean insert=db.addUser(name,surname,mail,phone,password);

                            if (insert==true){
                                Toast.makeText(SignIn.this, "Registered is succesful.", Toast.LENGTH_SHORT).show();

                                String message="Sayın "+name+" "+surname+", " +
                                        phone+" telefon numarası ve "+mail+ " adresi ile üyeliğiniz oluşturulmuştur."+
                                        "İyi eğlenceler...";

                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ mail});
                                email.putExtra(Intent.EXTRA_SUBJECT, "Üyelik Bilgileriniz");
                                email.putExtra(Intent.EXTRA_TEXT, message);
                                //need this to prompts email client only
                                email.setType("message/rfc822");


                                Intent play = new Intent(SignIn.this, Login.class);

                                finish();
                                startActivity(Intent.createChooser(email, "Send mail :"));
                                startActivity(play);
                            }
                            else
                                Toast.makeText(SignIn.this, "Sign in is failed.", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SignIn.this, "This mail is already registered.", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignIn.this, "Passwords are not same.", Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(SignIn.this,"Please enter all fields.",Toast.LENGTH_SHORT).show();
            }
        });

    }
}