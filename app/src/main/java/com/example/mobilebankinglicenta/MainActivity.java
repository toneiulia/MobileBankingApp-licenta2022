package com.example.mobilebankinglicenta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageButton imageButton;
    TextInputEditText tietTelefon;
    TextInputEditText tietParola;

    TextInputLayout tilTelefon;
    TextInputLayout tilParola;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageButton=findViewById(R.id.addBtn);
        tietTelefon=findViewById(R.id.pass_edit_text);
        tietParola=findViewById(R.id.name_edit_text);
        tilTelefon=findViewById(R.id.pass_text_input);
        tilParola=findViewById(R.id.name_text_input);

        tietParola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tietParola.setText("");
            }
        });
        tietTelefon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tietTelefon.setText("");
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitatiSingleton.phoneNumber=tietTelefon.getText().toString();
                Intent intent=new Intent(MainActivity.this,otpverification.class);
                startActivity(intent);
            }
        });


    }
}