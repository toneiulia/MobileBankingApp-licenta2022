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
import com.example.mobilebankinglicenta.classes.cards.Deposit;

import java.util.List;

public class DepositAdapter extends ArrayAdapter<Deposit> {
    private Context context;
    private List<Deposit> depositList;
    private LayoutInflater inflater;
    private int resource;

    public DepositAdapter(@NonNull Context context, int resource,
                          @NonNull List<Deposit> objects, LayoutInflater inflater) {
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

        Deposit deposit=depositList.get(position);
        if(deposit!=null) {

            TextView depositname = view.findViewById(R.id.textTitle);
            TextView depositsum = view.findViewById(R.id.textAmount);
            TextView depositrate = view.findViewById(R.id.textDobanda);
            TextView deposittimeleft = view.findViewById(R.id.textTimpRamas);



            depositname.setText(deposit.getName());
            depositsum.setText(String.valueOf(deposit.getAmount())+ " RON");
            depositrate.setText("Dobândă: "+String.valueOf(deposit.getInterestRate()));
            deposittimeleft.setText("Timp rămas: "+deposit.getTimeLeft() +" luni");
        }
        return view;
    }
}
