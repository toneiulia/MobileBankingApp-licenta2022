package com.example.mobilebankinglicenta.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.classes.RaportTranzactie;
import com.example.mobilebankinglicenta.classes.cards.Deposit;
import com.example.mobilebankinglicenta.classes.cards.Transaction;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ReportAdapter extends ArrayAdapter<RaportTranzactie> {
    private Context context;
    private List<RaportTranzactie> tranzactii;
    private LayoutInflater inflater;
    private int resource;
    private int resource2;



    @RequiresApi(api = Build.VERSION_CODES.N)
    public ReportAdapter(@NonNull Context context, int resource,int resource2,
                         @NonNull List<RaportTranzactie> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.tranzactii = objects;
        this.inflater = inflater;
        this.resource = resource;
        this.resource2 = resource2;
    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= inflater.inflate(resource,parent,false);

        TextView tvCategorie=view.findViewById(R.id.numecategorie);
        TextView tvSumaCheltuuita=view.findViewById(R.id.sumatotalacategorie);
        ProgressBar pgBar=view.findViewById(R.id.progressbar);
        TextView tvprocent=view.findViewById(R.id.procentbar);
        TextView tvNumaeTranzactii=view.findViewById(R.id.numartranzactiicategorie);

        AppCompatImageView img=view.findViewById(R.id.imgcat);



        RaportTranzactie raport=tranzactii.get(position);
        if(raport!=null){
            if(raport.getSuma()<0){
                tvCategorie.setText(raport.getNumeCategorie());
                tvSumaCheltuuita.setText(-raport.getSuma()+" RON");
                pgBar.getProgressDrawable().setColorFilter(
                        RaportTranzactie.culori.get(position), android.graphics.PorterDuff.Mode.SRC_IN);
                int prog=-(int) ((raport.getSuma()/RaportTranzactie.sumaTotala)*100);
                pgBar.setProgress(prog,true);
                tvprocent.setText(String.format("%.2f",-raport.getSuma()/RaportTranzactie.sumaTotala*100)+"%");
                tvNumaeTranzactii.setText(raport.getNrTranzactii()+" tranzactii");
                img.setBackgroundDrawable(raport.getImgResource());

               // view.setBackgroundColor(R.color.bgcol);
            }else if(raport.getSuma()>0&&raport.getNumeCategorie().equals("Bani Primiti")){
                View view2=inflater.inflate(resource2,parent,false);

                TextView tvNumarTranzactii2=view2.findViewById(R.id.procentbar2);
                TextView tvSumaPrimita=view2.findViewById(R.id.sumatotalacategorie2);

                tvNumarTranzactii2.setText(raport.getNrTranzactii()+" tranzactii");
                tvSumaPrimita.setText("+"+String.format("%.2f",raport.getSuma())+" RON");

                view=view2;

            }

//            TextView depositname=view.findViewById(R.id.textTitle);
//            TextView depositsum=view.findViewById(R.id.textAmount);
//            TextView depositrate=view.findViewById(R.id.textDobanda);
//            TextView deposittimeleft=view.findViewById(R.id.textTimpRamas);
//
//            depositname.setText(deposit.getName());
//            depositsum.setText(String.valueOf(deposit.getAmount())+ " RON");
//            depositrate.setText("Dobanda: "+String.valueOf(deposit.getInterestRate()));
//            deposittimeleft.setText("Timp ramas: "+deposit.getTimeLeft() +" luni");
        }
        return view;
    }


}
