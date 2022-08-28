package com.example.mobilebankinglicenta.customDialogs;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;

import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.UtilitatiSingleton;
import com.example.mobilebankinglicenta.adapters.AccountAdapter;
import com.example.mobilebankinglicenta.classes.cards.Card;
import com.example.mobilebankinglicenta.classes.cards.Deposit;
import com.example.mobilebankinglicenta.fragments.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class AddDepositDialog extends DialogFragment {

    public static final String TAG = "AddDepositDialog";

    private TextInputEditText depositName;
    private TextInputEditText depositAmount;
    private AppCompatSpinner depositPeriod;
    AppCompatSpinner depositCont;
    private TextView depositInterestRate;
    private TextView depositMesaj;
    private float rate = 0f;
    private String depositTimeLeft;
    private Button btnClose;
    private Button btnAdd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_deposit_dialog, null);

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        depositMesaj=view.findViewById(R.id.mesajfinal);
        depositName = view.findViewById(R.id.depositName);
        depositAmount = view.findViewById(R.id.depositAmount);
        depositInterestRate = view.findViewById(R.id.depositInterestRate);
        depositPeriod = view.findViewById(R.id.depositPeriod);
        depositCont = view.findViewById(R.id.depositCont);
        AccountAdapter adapter = new AccountAdapter(getContext(), R.layout.accountadapter, Arrays.asList(new Card(), UtilitatiSingleton.card), inflater);
        depositCont.setAdapter(adapter);
//        depositTimeLeft=view.findViewById(R.id.depositTimeLeft);
        btnClose = view.findViewById(R.id.btnClose);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        depositPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                depositTimeLeft = depositPeriod.getSelectedItem().toString();


                float i = (float) 0.0;
                if (!depositAmount.getText().toString().equals("")) {
                    int amount = Integer.parseInt("0"+(depositAmount.getText().toString()));
                    while (amount > 100) {
                        amount /= 10;
                        i += 0.1;
                    }
                }
                float interestRate = (float) ((depositPeriod.getSelectedItemPosition() + 3) / 10.0 + i);
                rate = interestRate;
                if (depositAmount.getText().toString().equals("") || depositPeriod.getSelectedItemPosition() == 0) {
                    depositInterestRate.setText("(introdu suma și alege perioada pentru a vedea procentul de dobândă)");
                    depositInterestRate.setTextColor(Color.rgb(117,117,117));
                    depositMesaj.setVisibility(View.INVISIBLE);
                    depositMesaj.setText("");
                } else {
                    depositInterestRate.setText(String.format("%.1f",interestRate) + "% de la data deschiderii");
                    depositInterestRate.setTextColor(Color.rgb(117, 186, 211));
                    depositMesaj.setVisibility(View.VISIBLE);
                    depositMesaj.setText("Nivelul dobânzii este de: "+String.format("%.1f",interestRate)+"% de la data deschiderii produsului.");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        depositAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                float i = (float) 0.0;
                int amount = Integer.parseInt("0"+(depositAmount.getText().toString()));
                while (amount > 100) {
                    amount /= 10;
                    i += 0.1;
                }
                float interestRate = (float) ((depositPeriod.getSelectedItemPosition() + 3) / 10.0 + i);
                rate = interestRate;
                if (depositAmount.getText().toString().equals("") || depositPeriod.getSelectedItemPosition() == 0) {
                    depositInterestRate.setText("(introdu suma si alege perioada pentru a vedea procentul de dobanda)");
                    depositInterestRate.setTextColor(Color.rgb(117,117,117));
                    depositMesaj.setVisibility(View.INVISIBLE);
                    depositMesaj.setText("");
                } else {
                    depositInterestRate.setText(String.format("%.1f",interestRate) + "% pe an la data deschiderii");
                    depositInterestRate.setTextColor(Color.rgb(117, 186, 211));
                    depositMesaj.setVisibility(View.VISIBLE);
                    depositMesaj.setText("Nivelul dobanzii este de: "+String.format("%.1f",interestRate)+"% pe an la data deschiderii produsului.");
                }
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validari()) {
                    String nume = depositName.getText().toString();
                    int amount = Integer.parseInt("0"+depositAmount.getText().toString());
                    String time = depositTimeLeft;
                    int timeLeft = Integer.parseInt(time.substring(0, time.length() - 5));
                    int period = timeLeft;
                    float interestRate = rate;

                    Deposit newDeposit = new Deposit(amount, interestRate, nume, period, timeLeft);
                    Toast.makeText(getContext(), newDeposit.toString(), Toast.LENGTH_SHORT).show();

                    //adaug depozit local
                    UtilitatiSingleton.depositList.add(newDeposit);
                    HomeFragment.depositAdapter.notifyDataSetChanged();
                    UtilitatiSingleton.card.setBalance((int) (UtilitatiSingleton.card.getBalance() - amount));
                    HomeFragment.myBalanceHome.setText(String.valueOf(UtilitatiSingleton.card.getBalance()) + " RON");

                    HomeFragment.database.collection("Cards/"+UtilitatiSingleton.user.getIDCard()+"/Deposits").document("d"+UtilitatiSingleton.depositList.size())
                            .set(newDeposit)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    HomeFragment.database.document("Cards/"+UtilitatiSingleton.user.getIDCard())
                                            .update("Balance",UtilitatiSingleton.card.getBalance());
                                }
                            });
                    getDialog().dismiss();
                }
            }
        });
        return view;
    }

    public boolean validari() {
        if (depositName.getText().toString().equals("")) {
            depositName.setError("Dati un nume depozitului !");
            return false;
        }
        if (depositAmount.getText().toString().equals("")) {
            depositAmount.setError("Alegeti o suma !");
            return false;
        } else if (Integer.parseInt(depositAmount.getText().toString()) < 0) {
            depositAmount.setError("Suma trebuie sa fie pozitiva !");
            return false;
        } else if (Integer.parseInt(depositAmount.getText().toString()) > UtilitatiSingleton.card.getBalance()) {
            depositAmount.setError("Suma este mai mare decat soldul disponibil !");
            return false;
        }
        return true;
    }
}
