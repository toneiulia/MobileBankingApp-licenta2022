package com.example.mobilebankinglicenta.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.classes.cards.Withdrawal;

import java.util.List;

public class WithdrawalAdapter extends ArrayAdapter<Withdrawal> {
    private Context context;
    private List<Withdrawal> listWithdrawals;
    private LayoutInflater inflater;
    private int resource;

    public WithdrawalAdapter(@NonNull Context context, int resource,
                             @NonNull List<Withdrawal> objects, LayoutInflater inflater) {
        super(context, resource, objects);
        this.context = context;
        this.listWithdrawals = objects;
        this.inflater = inflater;
        this.resource = resource;
    }


    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view= inflater.inflate(resource,parent,false);
        Withdrawal withdrawal=listWithdrawals.get(position);

        if(withdrawal!=null){
            TextView tv_location=view.findViewById(R.id.textTitle);
            TextView tv_sum=view.findViewById(R.id.textDobanda);
            TextView tv_date=view.findViewById(R.id.textTimpRamas);

            tv_location.setText(withdrawal.getLocation());
            tv_date.setText(withdrawal.getDate());
            tv_sum.setText(String.valueOf(withdrawal.getAmount())+" RON");
            if(withdrawal.getAmount()>0) {
                tv_sum.setTextColor(ContextCompat.getColor(context, R.color.verde));
            }
            else {
                tv_sum.setTextColor(ContextCompat.getColor(context, R.color.purple_200));
            }

        }

        return view;
    }
}
