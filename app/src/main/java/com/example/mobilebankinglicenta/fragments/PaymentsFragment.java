package com.example.mobilebankinglicenta.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.UtilitatiSingleton;
import com.example.mobilebankinglicenta.classes.Provider;
import com.example.mobilebankinglicenta.classes.cards.Transaction;
import com.example.mobilebankinglicenta.ecran_principal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class PaymentsFragment extends Fragment {

    private Spinner spinner;
    private Switch aSwitch;

    private TextInputEditText name;
    private TextInputEditText iban;
    private TextInputEditText description;
    private TextInputEditText sum;
    private boolean isUtility;
    public FirebaseFirestore database = FirebaseFirestore.getInstance();

    private Button sendBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payments,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialAnimations();

        spinner=view.findViewById(R.id.spinner);
        aSwitch=view.findViewById(R.id.switch1);
        name= view.findViewById(R.id.editTextName);
        iban= view.findViewById(R.id.editTextIBAN);
        sum= view.findViewById(R.id.editTextSum);
        description= view.findViewById(R.id.editTextDescription);
        sendBtn=view.findViewById(R.id.btnsend);


        initializeazaSpinner("");
        spinner.setEnabled(false);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   initializeazaSpinner("Alege un furnizor");
                   spinner.setEnabled(true);
                   isUtility=true;
               }else{
                   initializeazaSpinner("");
                   spinner.setEnabled(false);
                   name.setText("");
                   iban.setText("");
                   description.setText("");
                   isUtility=false;

               }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String provider = (String) parent.getItemAtPosition(position);
                for(Provider provider1:UtilitatiSingleton.providersList.values()){
                    if(provider1.getName().equals(provider)){

                        name.setText(provider1.getName());
                        iban.setText(provider1.getIBAN());
                        description.setText(provider1.getDomain());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validari()) {

                    float suma = -Float.parseFloat(sum.getText().toString());
                    String beneficiar = name.getText().toString();
                    String Iban = iban.getText().toString();
                    String descriere = description.getText().toString();
                    String cardFrom = UtilitatiSingleton.user.getIDCard();
                    String currency = "RON";
                    String status = "procesata";
                    Calendar calendar = Calendar.getInstance();
                    int an = calendar.get(Calendar.YEAR);
                    int luna = calendar.get(Calendar.MONTH);
                    int zi = calendar.get(Calendar.DAY_OF_MONTH);
                    String date = ("0"+zi).substring(("0"+zi).length()-2) + "/" + ("0"+(int)(luna+1)).substring(("0"+(int)(luna+1)).length()-2) + "/" + an;
                    String cardTo = null;


                    for (Map.Entry<String, Provider> entry : UtilitatiSingleton.providersList.entrySet()) {
                        if (entry.getValue().getName().equals(beneficiar)) {
                            cardTo = entry.getKey();
                        }
                    }

                    if (TextUtils.isEmpty(cardTo)&&isUtility) {

                        Provider providerNou = new Provider(descriere, Iban, beneficiar, isUtility);

                        String idNouProvider = "p" + (UtilitatiSingleton.providersList.size() + 1);
                       // Toast.makeText(getContext(), idNouProvider, Toast.LENGTH_SHORT).show();

                        UtilitatiSingleton.providersList.put(idNouProvider,providerNou);
                        cardTo = idNouProvider;

                        database.collection("Providers").document(idNouProvider)
                                .set(providerNou)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       // Toast.makeText(getContext(), "S-a adaugat provider", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                     //   Toast.makeText(getContext(), "NU s-a adaugat provider"+e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }

                    int transactionListSize=0;
                    for(Transaction transaction:UtilitatiSingleton.transactionList){
                        transactionListSize+=1;
                    }
                    String idNouTransaction = "t" + (transactionListSize + 1);
                    Toast.makeText(getContext(), idNouTransaction, Toast.LENGTH_SHORT).show();


                    Transaction tranzactieNoua= new Transaction((int) suma,cardFrom,beneficiar,currency,date,descriere,status,isUtility);
                    tranzactieNoua.setIBAN(Iban);
                    UtilitatiSingleton.transactionList.add(0,tranzactieNoua);
                    UtilitatiSingleton.card.setBalance((int) (UtilitatiSingleton.card.getBalance()+suma));


                    database.collection("Cards/"+UtilitatiSingleton.user.getIDCard() +"/Transactions").document(idNouTransaction)
                            .set(tranzactieNoua)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                            //        Toast.makeText(getContext(), "A fost adaugata tranzactia", Toast.LENGTH_SHORT).show();

                                    database.document("Cards/"+UtilitatiSingleton.user.getIDCard())
                                            .update("Balance",UtilitatiSingleton.card.getBalance()+suma)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                 //   Toast.makeText(view.getContext(), "S-a actualizat soldul", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });


                    ecran_principal.btmTrans.performClick();


                }
            }

        });
}


    public boolean validari(){
        if(sum.getText().toString().equals("")){
            sum.setError("Completati suma !");
            return false;
        }
        if(Double.parseDouble(sum.getText().toString()) > UtilitatiSingleton.card.getBalance()){
            sum.setError("Sold insuficient pentru suma completata!");
            return false;
        }
        if(name.getText().toString().equals("")) {
            name.setError("Completati beneficiarul !");
            return false;
        }
        if(iban.getText().toString().equals("")) {
            iban.setError("Completati IBAN-ul !");
            return false;
        }
        if(iban.getText().toString().length()!=24){
            iban.setError("IBAN-ul trebuie sa aiba 24 de caractere!");
            return false;
        }
        return true;
    }

    private void initializeazaSpinner(String mesaj) {
        List<String> providers=new ArrayList<>();
        providers.add(0, mesaj);
        for(Provider provider: UtilitatiSingleton.providersList.values()){
            if(provider.getIsUtility()){
                providers.add(provider.getName());
            }
        }

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(),R.layout.style_spinner,providers);
        arrayAdapter.setDropDownViewResource(R.layout.style_spinner_dropdown);
        spinner.setAdapter(arrayAdapter);
    }

    private void initialAnimations() {

//        View view = this.getView();
//        ImageView transBackground;
//        Animation fromBottomAnimation;
//        ConstraintLayout transactionsInfo;
//
//        transBackground= view.findViewById(R.id.payments_background);
//        transactionsInfo=view.findViewById(R.id.payments_info);
//
//        fromBottomAnimation= AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_faster);
//
//        transBackground.animate().translationY(-1250).setDuration(500);
//        transactionsInfo.startAnimation(fromBottomAnimation);
    }
}
