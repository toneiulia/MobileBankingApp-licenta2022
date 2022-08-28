package com.example.mobilebankinglicenta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.UtilitatiSingleton;
import com.example.mobilebankinglicenta.classes.cards.Card;
import com.example.mobilebankinglicenta.classes.cards.Deposit;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<Card> {

    private Context context;
    private List<Card> depositList;
    private LayoutInflater inflater;
    private int resource;

    public AccountAdapter(@NonNull Context context, int resource,
                          @NonNull List<Card> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.depositList = objects;
        this.inflater = inflater;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= inflater.inflate(resource,parent,false);
        TextView tvNume=view.findViewById(R.id.tvNumeDepozit);
        TextView tvSuma=view.findViewById(R.id.tvSumaDepozit);
        TextView tvIBAN=view.findViewById(R.id.tvContDepozit);
        if(position==0){
            tvNume.setText("Account");
            ViewGroup vg= (ViewGroup) tvSuma.getParent();
            vg.removeView(tvSuma);
            ViewGroup vg2= (ViewGroup) tvIBAN.getParent();
            vg2.removeView(tvIBAN);
        }else{
            tvNume.setText(UtilitatiSingleton.user.getFirstName()+" "+UtilitatiSingleton.user.getLastName());
            tvSuma.setText(depositList.get(position).getBalance()+" RON");
            tvIBAN.setText(depositList.get(position).getIBAN()+"");
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position,convertView,parent);
    }
}
