package com.example.mobilebankinglicenta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilebankinglicenta.adapters.TransactionAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PinView extends AppCompatActivity {

    AppCompatButton button;
    TextInputEditText tietCVV;
    TextInputLayout t;
    Dialog dialog2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_view);
        button = findViewById(R.id.verificaCVC);
        tietCVV = findViewById(R.id.pass_edit_text);
        t=findViewById(R.id.tvCVV);
        tietCVV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 3) {
                    button.setEnabled(true);
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_back_primary_15));
                } else {
                    button.setEnabled(false);
                    button.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_back_primary_first));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tietCVV.getText().toString().equals(UtilitatiSingleton.card.getCVV()+"")) {
                    t.setError(null);
                    dialog2 = new Dialog(PinView.this);
                    dialog2.setContentView(R.layout.pindialog);
                    dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                    dialog2.setCancelable(true);
                    dialog2.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

                    Button closeBtn = dialog2.findViewById(R.id.btnCloseCateg2);

                    closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                             dialog2.dismiss();
                            finish();
                        }
                    });

                    TextView pin = dialog2.findViewById(R.id.categSum2);
                    pin.setText(UtilitatiSingleton.card.getPin() + "");


                    dialog2.show();
                }else{
                    t.setError("CVV incorect");
                }
            }
        });
    }
}