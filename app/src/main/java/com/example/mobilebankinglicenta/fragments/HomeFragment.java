package com.example.mobilebankinglicenta.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.mobilebankinglicenta.MainActivity;
import com.example.mobilebankinglicenta.PinView;
import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.UtilitatiSingleton;
import com.example.mobilebankinglicenta.adapters.DepositAdapter;
import com.example.mobilebankinglicenta.adapters.TransactionAdapter;
import com.example.mobilebankinglicenta.adapters.WithdrawalAdapter;
import com.example.mobilebankinglicenta.classes.Provider;
import com.example.mobilebankinglicenta.classes.User;
import com.example.mobilebankinglicenta.classes.cards.Card;
import com.example.mobilebankinglicenta.classes.cards.CardType;
import com.example.mobilebankinglicenta.classes.cards.Deposit;
import com.example.mobilebankinglicenta.classes.cards.Transaction;
import com.example.mobilebankinglicenta.classes.cards.Withdrawal;
import com.example.mobilebankinglicenta.customDialogs.AddDepositDialog;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static android.os.Looper.getMainLooper;

public class HomeFragment extends Fragment {

    public static String cardId;  //id card curent
    public static Card card; //card curent
    public static User user;  //user curent
    public static CardType cardType;  //tipul cardului curent
    public static List<Withdrawal> withdrawalList = new ArrayList<>();  //toate retragerile
    public static List<Deposit> depositList = new ArrayList<>();  //toate depozitele
    public static List<Transaction> transactionList = new ArrayList<>();  //toate tranzatiile
    public static HashMap<String, Provider> providersList = new HashMap<>(); //toti providerii
    public static HashMap<String, User> userList = new HashMap<>(); //toti userii
    public static HashMap<String, Card> cardList = new HashMap<>(); //toate cardurile

    public static TextView myBalanceHome;
    public static TextView depozite;

    Dialog dialog1; //dialog ptr informatii card
    Dialog dialog2;  //dialog ptr aifsare retrageri
    Dialog dialog3;  //dialog ptr aifsare retrageri

    Switch swBlocat;

    SwipeMenuListView listView;


    ImageButton showDetailsButton;
    ImageView imgPin;

    boolean areDetailsShown = false;
    boolean areDepositsShown = false;

    public static DepositAdapter depositAdapter;

    public static FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        UtilitatiSingleton.initializare();
         listView = view.findViewById(R.id.depositListView);

        depositAdapter = new DepositAdapter(view.getContext(),
                R.layout.depozit_adapter, UtilitatiSingleton.depositList, getLayoutInflater());

        listView.setAdapter(depositAdapter);

        swBlocat = view.findViewById(R.id.switchfreeze);
        imgPin = view.findViewById(R.id.seePin);
        myBalanceHome = view.findViewById(R.id.textView14);
        depozite = view.findViewById(R.id.textView8);
        //animatii
        initialAnimations();

        getCurrentUser();
        //luam userul curent


        //eye button cu informatiile de pe card
        showDetailsButton = view.findViewById(R.id.btn_show_details);
        showDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetails(view);
                if (!areDetailsShown) {
                    showDetailsButton.setImageResource(R.drawable.iconeye);
                } else if (areDetailsShown) {
                    showDetailsButton.setImageResource(R.drawable.iconeyeclosed);
                }
            }
        });

        //Custom dialog cu withdrawals la atm la short click pe card
        showWithdrawals(view);

        //Custom dialog cu card informations la long click pe card
        showInformationsDialog(view);


        //biblioteca externa ptr slide si delete pe listview
        //se creaza menu ptr fiecare item din listview
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        view.getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
                        255, 255)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


        //afiseaza si ascunde depozitele la click pe sagetica
        ImageButton arrowBtn = view.findViewById(R.id.arrow);
        arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDeposits(view, creator);
            }
        });

        //buton adauga depozit care deschide un custom dialog cu formularul de completat
        ImageButton addDeposit = view.findViewById(R.id.addDeposit);
        addDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDepositDialog depositDialog = new AddDepositDialog();
                depositDialog.show(getFragmentManager(), "AddDepositDialog");
            }
        });
        depozite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3 = new Dialog(getContext());
                dialog3.setContentView(R.layout.infodepozitedialog);
                dialog3.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
                dialog3.setCancelable(true);
                dialog3.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

                Button closeBtn = dialog3.findViewById(R.id.btnCloseCateg);

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog3.dismiss();
                    }
                });

                dialog3.show();

            }
        });

        swBlocat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!UtilitatiSingleton.card.isBlocat() == b) {
                    UtilitatiSingleton.card.setBlocat(b);
                    database.collection("Cards").document(UtilitatiSingleton.user.getIDCard()).update("Blocat", b);
                }
                if (b) {
                    if (areDetailsShown) {
                        showDetailsButton.performClick();
                    }
                    showDetailsButton.setImageResource(R.drawable.blockedcard);
                    showDetailsButton.setEnabled(false);
                } else {
                    showDetailsButton.setImageResource(R.drawable.iconeye);
                    showDetailsButton.setEnabled(true);
                }
            }
        });

        imgPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PinView.class);
                startActivity(intent);
            }
        });
    }

    private void showWithdrawals(@NonNull View view) {
        dialog2 = new Dialog(getContext());
        dialog2.setContentView(R.layout.withdrawals_dialog);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog2.setCancelable(true);
        dialog2.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        Button closeBtn = dialog2.findViewById(R.id.btn_close);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();
            }
        });


        TextView tv_cardNumber = view.findViewById(R.id.tv_card_number);
        tv_cardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WithdrawalAdapter adapter = new WithdrawalAdapter(dialog2.getContext(),
                        R.layout.withdrawldesign, UtilitatiSingleton.withdrawalList, getLayoutInflater());

                ListView listView = dialog2.findViewById(R.id.lv_withdrawal);
                listView.setAdapter(adapter);

                dialog2.show();


            }
        });
    }

    private void showInformationsDialog(@NonNull View view) {

        dialog1 = new Dialog(getContext());
        dialog1.setContentView(R.layout.cardinfo_dialog);
        dialog1.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog1.setCancelable(true);
        //  dialog1.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;

        Button copyBtn = dialog1.findViewById(R.id.btn_copy);
        Button closeBtn = dialog1.findViewById(R.id.btn_close);

        TextView tvName = dialog1.findViewById(R.id.tv_name);
        TextView tvIban = dialog1.findViewById(R.id.tv_iban);
        TextView tvCurrency = dialog1.findViewById(R.id.tv_currency);
        TextView tvBank = dialog1.findViewById(R.id.tv_bank);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String informations = " Name:" + tvName.getText() + "\n IBAN:" + tvIban.getText() + "\n Currency: " + tvCurrency.getText() + "\n Bank:" + tvBank.getText();
                ClipData clip = ClipData.newPlainText("card info", informations);
                clipboard.setPrimaryClip(clip);


                Toast.makeText(getContext(), informations, Toast.LENGTH_SHORT).show();

            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog1.dismiss();
            }
        });

        View cardview = view.findViewById(R.id.cardview);
        cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                tvName.setText(" " + UtilitatiSingleton.user.getLastName() + " " + UtilitatiSingleton.user.getFirstName());
                tvIban.setText(" " + UtilitatiSingleton.card.getIBAN());
                tvCurrency.setText(" RON");
                tvBank.setText(" Gamma");

                dialog1.show();
                return true;
            }
        });
    }

    private void showDeposits(@NonNull View view, SwipeMenuCreator creator) {

         listView = view.findViewById(R.id.depositListView);
        //ListView listView=view.findViewById(R.id.depositListView);

        depositAdapter = new DepositAdapter(view.getContext(),
                R.layout.depozit_adapter, UtilitatiSingleton.depositList, getLayoutInflater());

        listView.setAdapter(depositAdapter);


        //implementare optiune de stergere la swipe
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                new AlertDialog.Builder(view.getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Esti sigur?")
                        .setMessage("Vrei sa inchizi acest depozit?")
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Deposit depositToErase = UtilitatiSingleton.depositList.get(position);
                                String numeDep=depositToErase.getName();

                                UtilitatiSingleton.card.setBalance((int) (UtilitatiSingleton.card.getBalance() + depositToErase.getAmount()));
                                HomeFragment.myBalanceHome.setText(String.valueOf(UtilitatiSingleton.card.getBalance()) + " RON");
                                UtilitatiSingleton.depositList.remove(position);
                                depositAdapter.notifyDataSetChanged();
                                database.collection("Cards/"+UtilitatiSingleton.user.getIDCard()+"/Deposits").whereEqualTo("name",numeDep)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                                                    String depositID= document.getId();
                                                    database.collection("Cards/"+UtilitatiSingleton.user.getIDCard()+"/Deposits").document(depositID)
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    database.document("Cards/"+UtilitatiSingleton.user.getIDCard())
                                                                            .update("Balance",UtilitatiSingleton.card.getBalance());
                                                                }
                                                            });


                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .show();
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        ImageButton arrowBtn = view.findViewById(R.id.arrow);

        if (!areDepositsShown) {
            areDepositsShown = true;

            listView.setVisibility(View.VISIBLE);
            arrowBtn.setImageResource(R.drawable.arrow_up);
        } else if (areDepositsShown) {
            areDepositsShown = false;

            listView.setVisibility(View.GONE);
            arrowBtn.setImageResource(R.drawable.arrow);

        }
    }

    private void showDetails(@NonNull View view) {
        if (!areDetailsShown) {
            areDetailsShown = true;

            TextView cardNumber = view.findViewById(R.id.tv_card_number);
            cardNumber.setText(UtilitatiSingleton.card.getNumber());

            TextView cvv = view.findViewById(R.id.tv_cvv);
            String cvv_string = String.valueOf(UtilitatiSingleton.card.getCVV());
            cvv.setText(cvv_string);

        } else if (areDetailsShown) {
            areDetailsShown = false;

            TextView cardNumber = view.findViewById(R.id.tv_card_number);
            String text = UtilitatiSingleton.card.getNumber().substring(UtilitatiSingleton.card.getNumber().length() - 4);
            String message = "****  ****  ****  " + text;
            cardNumber.setText(message);

            TextView cvv = view.findViewById(R.id.tv_cvv);
            cvv.setText("***");

        }
    }

    private void getCurrentUser() {

        database.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UtilitatiSingleton.user = document.toObject(User.class);
                                findCardID();
                            }
                        } else {
                            user = UtilitatiSingleton.user;
                        }
                    }
                });
    }


    private void getTransactions() {
//UtilitatiSingleton.fire();

        database.collection("Cards/" + UtilitatiSingleton.user.getIDCard() + "/Transactions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        UtilitatiSingleton.transactionList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Transaction transaction = document.toObject(Transaction.class);
                            UtilitatiSingleton.transactionList.add(transaction);
                        }
                        Collections.sort(UtilitatiSingleton.transactionList, Transaction.esteCronologic);
                    }
                });

    }

    private void findCardID() {

        findCardDetails(getView());

    }

    private void findCardDetails(@NonNull View view) {


        database.document("Cards/" + UtilitatiSingleton.user.getIDCard())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //   for (QueryDocumentSnapshot document : task.getResult()) {

                            UtilitatiSingleton.card = task.getResult().toObject(Card.class);
                            fillCardWithDetails(view);
                            if (UtilitatiSingleton.card.isBlocat()) {
                                swBlocat.performClick();
                            }

                            // }
                        } else {
                            user = UtilitatiSingleton.user;
                        }

                    }
                });


        getWithdrawals();
        getDeposits();
        getTransactions();
        getProviders();

    }

    private void getProviders() {

        database.collection("Providers")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        UtilitatiSingleton.providersList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Provider provider = document.toObject(Provider.class);
                            UtilitatiSingleton.providersList.put(document.getId(), provider);
                        }
                    }
                });
    }


    private void findCardType() {
        Thread thread = new Thread() {
            @Override
            public void run() {

                database.document("CardTypes/" + card.getType())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                cardType = documentSnapshot.toObject(CardType.class);
                                //Toast.makeText(getContext(), cardType.toString(), Toast.LENGTH_LONG).show();

                                if (cardType.getIsMastercard()) {
                                    View cardImage = getView().findViewById(R.id.cardview);
                                    //   cardImage.setBackgroundResource(R.drawable.mastercard);
                                }

                                if ("Credit".equals(cardType.getType())) {
                                    ConstraintLayout deposits = getView().findViewById(R.id.deposits_layout);
                                    deposits.setVisibility(View.GONE);
                                }
                            }
                        });

                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
        thread.start();

    }

    private void getWithdrawals() {


        database.collection("Cards/" + UtilitatiSingleton.user.getIDCard() + "/Withdrawals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UtilitatiSingleton.withdrawalList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Withdrawal withdrawal = document.toObject(Withdrawal.class);
                            UtilitatiSingleton.withdrawalList.add(withdrawal);
                            //Toast.makeText(getContext(), withdrawal.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void getDeposits() {

        database.collection("Cards/" + UtilitatiSingleton.user.getIDCard() + "/Deposits")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        UtilitatiSingleton.depositList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Deposit deposit = document.toObject(Deposit.class);
                            UtilitatiSingleton.depositList.add(deposit);
                        }
                    }
                });

    }

    private void fillCardWithDetails(@NonNull View view) {
        TextView endDate = view.findViewById(R.id.tv_end_date);
        endDate.setText(UtilitatiSingleton.card.getEndDate());

        TextView cardNumber = view.findViewById(R.id.tv_card_number);
        String text = UtilitatiSingleton.card.getNumber().substring(UtilitatiSingleton.card.getNumber().length() - 4);
        String message = "****  ****  ****  " + text;
        cardNumber.setText(message);

        TextView owner = view.findViewById(R.id.tv_owner);
        String ownerName = UtilitatiSingleton.user.getLastName().toUpperCase() + " " + UtilitatiSingleton.user.getFirstName().toUpperCase();
        owner.setText(ownerName);

        TextView balance = view.findViewById(R.id.textView14);
        String mybalance = String.format("%,.2f", UtilitatiSingleton.card.getBalance()) + " RON";
        //String mybalance=String.valueOf(card.getBalance());
        balance.setText(mybalance);
    }


    private void initialAnimations() {
//        View view = this.getView();
//        ImageView background;
//        ImageView cardIcon;
//        LinearLayout titleLayout;
//        Animation fromBottomAnimation;
//        ConstraintLayout homeInfo;
//
//        background= view.findViewById(R.id.background);
//        cardIcon= view.findViewById(R.id.card_icon);
//        titleLayout= view.findViewById(R.id.titleLayout);
//        homeInfo=view.findViewById(R.id.home_info);
//
//        fromBottomAnimation= AnimationUtils.loadAnimation(getContext(),R.anim.from_bottom);
//
//        background.animate().translationY(-1000).setDuration(800).setStartDelay(100);
//        cardIcon.animate().translationX(-1100).setDuration(800).setStartDelay(600);
//
//        titleLayout.startAnimation(fromBottomAnimation);
//        homeInfo.startAnimation(fromBottomAnimation);
    }
}
