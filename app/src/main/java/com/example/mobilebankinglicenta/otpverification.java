package com.example.mobilebankinglicenta;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otpverification extends AppCompatActivity {

    EditText otpET1;
    EditText otpET2;
    EditText otpET3;
    EditText otpET4;
    EditText otpET5;
    EditText otpET6;
    TextView resendBtn;
    TextView tvNumarTel;

    Button continuebtn;

    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken forceResendingToken;

    PhoneAuthOptions ops;

    private boolean resendEnabled = false;

    private int resendTime = 60;
    private int selectedETPosition = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);



        otpET1 = findViewById(R.id.optET1);
        otpET2 = findViewById(R.id.optET2);
        otpET3 = findViewById(R.id.optET3);
        otpET4 = findViewById(R.id.optET4);
        otpET5 = findViewById(R.id.optET5);
        otpET6 = findViewById(R.id.optET6);

        otpET1.addTextChangedListener(textWatcher);
        otpET2.addTextChangedListener(textWatcher);
        otpET3.addTextChangedListener(textWatcher);
        otpET4.addTextChangedListener(textWatcher);
        otpET5.addTextChangedListener(textWatcher);
        otpET6.addTextChangedListener(textWatcher);


        resendBtn = findViewById(R.id.tv_retrimite);
        tvNumarTel = findViewById(R.id.tv_numarTelefon);
        tvNumarTel.setText(UtilitatiSingleton.phoneNumber);

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendVerificationCode("+40734434359",forceResendingToken);
            }
        });


        continuebtn = findViewById(R.id.continueBtn);
        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code=(otpET1.getText().toString()+
                        otpET2.getText().toString()+
                        otpET3.getText().toString()+
                        otpET4.getText().toString()+
                        otpET5.getText().toString()+
                        otpET6.getText().toString()).trim();
                verifyPhoneNumberWithCode(mVerificationId,code);
            }
        });
        ops = PhoneAuthOptions.newBuilder().setPhoneNumber(UtilitatiSingleton.phoneNumber).setTimeout(Integer.toUnsignedLong(60), TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        forceResendingToken=token;
                        mVerificationId=verificationId;
                        startCountDownTimer();
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(ops);

        showKeyboard(otpET1);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(otpverification.this, ecran_principal.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(otpverification.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void startCountDownTimer(){
        resendEnabled = false;
        resendBtn.setTextColor(Color.parseColor("#089cd4"));

        new CountDownTimer(resendTime * 1000, 100){

            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Retrimite (" + (millisUntilFinished/1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resendBtn.setText("Retrimite");
                //aici era R.color.primary
            }
        }.start();
    }
    private void showKeyboard(EditText otpET){

        otpET.requestFocus();

        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);

    }
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length() > 0){
                if(selectedETPosition == 0){
                    selectedETPosition = 1;
                    showKeyboard(otpET2);
                }
                else if(selectedETPosition == 1){
                    selectedETPosition = 2;
                    showKeyboard(otpET3);
                }
                else if(selectedETPosition == 2){
                    selectedETPosition = 3;
                    showKeyboard(otpET4);
                }
                else if(selectedETPosition == 3){
                    selectedETPosition = 4;
                    showKeyboard(otpET5);
                }
                else if(selectedETPosition == 4){
                    selectedETPosition = 5;
                    showKeyboard(otpET6);
                }
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_DEL){
            if(selectedETPosition == 5){
                selectedETPosition = 4;
                showKeyboard(otpET5);
            }
            else if(selectedETPosition == 4){
                selectedETPosition = 3;
                showKeyboard(otpET4);
            }
            else if(selectedETPosition == 3){
                selectedETPosition = 2;
                showKeyboard(otpET3);
            }
            else if(selectedETPosition == 2){
                selectedETPosition = 1;
                showKeyboard(otpET2);
            }
            else if(selectedETPosition == 1){
                selectedETPosition = 0;
                showKeyboard(otpET1);
            }

            return true;
        }
        else{
            return super.onKeyUp(keyCode, event);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {

        ops = PhoneAuthOptions.newBuilder().setPhoneNumber(UtilitatiSingleton.phoneNumber).setTimeout(Integer.toUnsignedLong(60), TimeUnit.SECONDS)
                .setActivity(this).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        forceResendingToken=token;
                        mVerificationId=verificationId;
                        startCountDownTimer();
                    }
                }).setForceResendingToken(forceResendingToken).build();

        PhoneAuthProvider.verifyPhoneNumber(ops);
    }
}