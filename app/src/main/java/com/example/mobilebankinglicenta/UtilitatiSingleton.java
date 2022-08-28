package com.example.mobilebankinglicenta;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mobilebankinglicenta.classes.Provider;
import com.example.mobilebankinglicenta.classes.User;
import com.example.mobilebankinglicenta.classes.cards.Card;
import com.example.mobilebankinglicenta.classes.cards.Deposit;
import com.example.mobilebankinglicenta.classes.cards.Transaction;
import com.example.mobilebankinglicenta.classes.cards.Withdrawal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UtilitatiSingleton {
    public static boolean esteBlocat=false;
    public static String phoneNumber;
    public static User user;
    public static Card card;
    public static List<Withdrawal> withdrawalList=new ArrayList<>();
//            = Arrays.asList(
//            new Withdrawal("MegaImage", -150, "25/05/2022"),
//            new Withdrawal("Gamma ATM", 150, "25/05/2022")
//    );  //toate retragerile
    public static List<Deposit> depositList=new ArrayList<Deposit>() ;
//{{
//            add(new Deposit(2800,1.1f,"Masina",24,12));
//            add(new Deposit(90000,1.1f,"Casa",60,27));
//    }};  //toate depozitele
    public static List<Transaction> transactionList=new ArrayList<Transaction>();
    public static List<Transaction> transactionList2=new ArrayList<Transaction>() {{
        add(new Transaction(-321.86f, "CARREFOUR ROMANIA SA ", "CARREFOUR ROMANIA SA ", "RON", "01/06/2022", "Alimentație", "procesata", false,"RO06PORL5856834318442176"));

        add(new Transaction(180.35f, "binance.com", "binance.com", "RON", "01/06/2022", "Invetiții", "procesata", false,"RO06PORL5856834318442176"));

        add(new Transaction(8000f, "CEGEKA ROMANIA SRL", "CEGEKA ROMANIA SRL", "RON", "01/07/2022", "General", "procesata", false,"RO06PORL5856834318442176"));

        add(new Transaction(489.65f, "Trading212", "Trading212", "RON", "03/07/2022", "Investiții", "procesata", false,"RO06PORL5856834318442176"));

        add(new Transaction(350f, "Tradeville", "Tradeville", "RON", "01/07/2022", "Investiții", "procesata", false,"RO06PORL5856834318442176"));
        add(new Transaction(-101.00f, "PayU*vodafone.ro", "02/07/2022", "Utilități",  false));
        add(new Transaction(-110.80f, "ENEL ENERGIE MUNTENIA", "02/04/2022", "Utilități",  true));
        add(new Transaction(-83.67f, "MEGACONSTRUCT SA", "02/04/2022", "Utilități",  true));
        add(new Transaction(-20.00f, "SUPERCOM SA", "02/04/2022", "Utilități",  true));
        add(new Transaction(-43.59f, "APA NOVA SA", "02/04/2022", "Cumpărături",  true));
        add(new Transaction(-369.95f, "OMV 1779", "02/04/2022", "Transport",  false));
        add(new Transaction(-177.00f, "Parfumerie Douglas", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-129.90f, "ZARA 1428PARKLAKE C1", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-130.00f, "Trattoria Monza", "02/04/2022", "Restaurant",  false));
        add(new Transaction(-80.00f, "IANCU CRISTINA", "02/04/2022", "Transferuri",  false));
        add(new Transaction(-92.90f, "FARM TEI", "02/04/2022", "General",  false));
        add(new Transaction(-17.48f, "GHINDOC IMPEX", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-20.00f, "KFC MEGA MALL", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-24.50f, "ALSIMA FOOD", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-90.00f, "TONE PAUL", "02/04/2022", "Transferuri",  false));
        add(new Transaction(-320.01f, "MOL AFUMATI", "02/04/2022", "Transport",  false));
        add(new Transaction(-31.50f, "KFC KIOSK COLENTINA", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-59.60f, "PENA COM", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-100.00f, "MARIN DANIEL-CONSTANTIN", "02/04/2022", "Transferuri",  false));
        add(new Transaction(-53.30f, "FARMACIA FARMASANO", "02/04/2022", "General",  false));
        add(new Transaction(-73.12f, "FARMACIA TEI BV154 C27", "02/04/2022", "General",  false));
        add(new Transaction(-18.40f, "SPRING FARM SRL", "02/04/2022", "General",  false));
        add(new Transaction(-27.95f, "SMODIN.IO – TEXT TOOLS", "02/04/2022", "General",  false));
        add(new Transaction(-250.00f, "TSV Afumati", "02/04/2022", "Transport",  false));
        add(new Transaction(-236.91f, "KAUFLAND ROMANIA SA", "02/04/2022", "Transport",  false));
        add(new Transaction(-142.97f, "Teatrul National Bucuresti", "02/04/2022", "General",  false));
        add(new Transaction(-36.33f, "MEGAIMAGE 0109 AFUMATI", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-33.00f, "PREMIER RESTAURANTS RO", "02/04/2022", "Alimentație",  false));
        add(new Transaction(-32.50f, "Cinema City Park Lake", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-42.50f, "EDITURA STYLISHED", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-192.50f, "World Class Veranda Mall", "02/04/2022", "General",  false));
        add(new Transaction(-210.50f, "H M UNIC IMPORT SRL", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-130.07f, "PayU/*expertbeauty.ro", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-45.99f, "PENTI MEGA MALL", "02/04/2022", "Cumpărături",  false));
        add(new Transaction(-142.00f, "Afumati – Partener Mol", "02/04/2022", "Transport",  false));
        add(new Transaction(-592.32f, "Booking.com", "02/04/2022", "General",  false));
    }};

    public static void fire(){
        for(int i=0;i<transactionList2.size();i++){

            database.collection("Cards/"+UtilitatiSingleton.user.getIDCard() +"/Transactions").document("t"+i)
                    .set(transactionList2.get(i));
        }
    }

    public static HashMap<String, Provider> providersList= new HashMap<String, Provider>();
//    {{
//        put("p1",new Provider("Energie", "RO34PORL7876274717549341", "ENEL ENERGIE MUNTENIA", true));
//        put("p2",new Provider("Energie", "RO34PORL7876274717549341", "MEGACONSTRUCT SA", true));
//        put("p3",new Provider("Energie", "RO34PORL7876274717549341", "PayU*vodafone.ro", true));
//        put("p4",new Provider("Telefonie", "RO34PORL7876274717549341", "Telekom Ro Mobile", true));
//    }}; //toti providerii
    public static HashMap<String,User> userList= new HashMap<>(); //toti userii
    public static HashMap<String,Card> cardList= new HashMap<>(); //toate cardurile


    public static FirebaseFirestore database=FirebaseFirestore.getInstance();


    public static void initializare(){
                database.collection("Users")
                        .whereEqualTo("Phone", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       // Toast.makeText(getContext(), task.toString(), Toast.LENGTH_LONG).show();

                        for(QueryDocumentSnapshot document : task.getResult()){
                                    user =document.toObject(User.class);
                                }
                    }
                });

        database.collection("Cards").whereEqualTo("Owner","u1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // Toast.makeText(getContext(), task.toString(), Toast.LENGTH_LONG).show();
                        for(QueryDocumentSnapshot document : task.getResult()){
                            card =document.toObject(Card.class);
                        }
                    }
                });
    }
}
