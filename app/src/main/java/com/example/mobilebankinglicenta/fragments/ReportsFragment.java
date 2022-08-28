package com.example.mobilebankinglicenta.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.fonts.Font;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.mobilebankinglicenta.CustomPieChart;
import com.example.mobilebankinglicenta.CustomRenderer;
import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.UtilitatiSingleton;
import com.example.mobilebankinglicenta.adapters.ReportAdapter;
import com.example.mobilebankinglicenta.adapters.TransactionAdapter;
import com.example.mobilebankinglicenta.adapters.WithdrawalAdapter;
import com.example.mobilebankinglicenta.classes.RaportTranzactie;
import com.example.mobilebankinglicenta.classes.cards.Transaction;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.whiteelephant.monthpicker.MonthPickerDialog;


import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ReportsFragment extends Fragment {
    private CustomPieChart pieChart;
    private Button lunaBtn;
    private Button generalBtn;
    TextView tvSuma;
    TextView tvNrTranzactii;
    ListView lvRaport;

    HashMap<String, List<Transaction>> categorii;
    List<RaportTranzactie> raport;

    Dialog dialog2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(view.getContext()));
        }

        Python py=Python.getInstance();
        PyObject obj=py.getModule("myfile");
        PyObject ob=obj.callAttr("main");

        TextView tv=view.findViewById(R.id.predictie);
        tv.setText(ob.toString());

        initialAnimations();
        lvRaport = view.findViewById(R.id.lvcategorii);
        tvSuma = view.findViewById(R.id.cheltuieliReport);
        tvNrTranzactii = view.findViewById(R.id.numarTranzactiiReport);
        pieChart = view.findViewById(R.id.pie_chart);

        setupPieChart();
        loadPieChartData("/");

        lunaBtn = view.findViewById(R.id.grafic_luna);
        generalBtn = view.findViewById(R.id.grafic_general);
        selecteazaButon(generalBtn);

        generalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lunaBtn.setText("Alege luna");
                selecteazaButon(generalBtn);
                deselecteazaButon(lunaBtn);
                loadPieChartData("/");
            }
        });

        lunaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alegeLuna();
            }
        });


        lvRaport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTranzactii(view, raport.get(i));
            }
        });


    }

    private void alegeLuna() {
        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                new MonthPickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        String lunaSelectata = ("0" + (int) (selectedMonth + 1)).substring(("0" + (int) (selectedMonth + 1)).length() - 2) + "/" + selectedYear;
                        lunaBtn.setText(lunaSelectata);
                        selecteazaButon(lunaBtn);
                        deselecteazaButon(generalBtn);

                        Toast.makeText(getContext(), "/" + lunaSelectata, Toast.LENGTH_SHORT).show();
                        loadPieChartData("/" + lunaSelectata);
                    }

                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(Calendar.JULY)
                .setMinYear(2021)
                .setActivatedYear(today.get(Calendar.YEAR))
                .setMaxYear(2023)
                .setTitle("Selectati luna")
                .build()
                .show();
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        //pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setHoleRadius(95);
        pieChart.setTransparentCircleRadius(50);
        pieChart.setCenterTextSize(18);
        pieChart.setDrawEntryLabels(true);
        pieChart.setDrawRoundedSlices(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadPieChartData(String date) {

        categorii = new HashMap<>();
        // HashMap<String, List<Transaction>> categorii2=new HashMap<>();
        // categorii2.get("ceva").stream().filter(o -> o.getAmount() > 0).mapToDouble(o -> o.getAmount()).sum();
        categorii.put("Utilitati", new ArrayList<>());
        categorii.put("Economii și investiții", new ArrayList<>());
        categorii.put("Alimentație și servire", new ArrayList<>());
        categorii.put("Cumpărături", new ArrayList<>());
        categorii.put("Transferuri", new ArrayList<>());
        categorii.put("General", new ArrayList<>());
        categorii.put("Transport și mașină", new ArrayList<>());
        categorii.put("Bani Primiti", new ArrayList<>());

        for (Transaction transaction : UtilitatiSingleton.transactionList) {
            if (transaction.getDate().contains(date)) {
                if (transaction.getIsUtilityBill()) {
                    categorii.get("Utilitati").add(transaction);
                } else if (transaction.getAmount() < 0) {

                    if (transaction.getDescription().contains("Utilitati")) {
                        categorii.get("Utilitati").add(transaction);
                    } else if (transaction.getDescription().contains("Economii") || transaction.getDescription().contains("Investiții")) {
                        categorii.get("Economii și investiții").add(transaction);
                    } else if (transaction.getDescription().contains("Alimentație") || transaction.getDescription().contains("Restaurant")) {
                        categorii.get("Alimentație și servire").add(transaction);
                    } else if (transaction.getDescription().contains("Transport") || transaction.getDescription().contains("Uber") || transaction.getDescription().contains("Benzina") || transaction.getDescription().contains("Motorina") || transaction.getDescription().contains("Mașină")) {
                        categorii.get("Transport și mașină").add(transaction);
                    } else if (transaction.getDescription().contains("Cumpărături")) {
                        categorii.get("Cumpărături").add(transaction);
                    } else if (transaction.getDescription().contains("Transferuri")) {
                        categorii.get("Transferuri").add(transaction);
                    } else if (transaction.getDescription().contains("General")) {
                        categorii.get("General").add(transaction);
                    }
                } else if (transaction.getAmount() > 0) {
                    categorii.get("Bani Primiti").add(transaction);
                }
            }

        }

        // Drawable dr= ResourcesCompat.getDrawable(getResources(), R.drawable.arrow, null);

        float sum = 0f;

        int nrTranzactii = 0;

        raport = new ArrayList<>();

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (String key : categorii.keySet()) {
            float suma = (float) categorii.get(key).stream().mapToDouble(o -> o.getAmount()).sum();
            Drawable dr = ResourcesCompat.getDrawable(getResources(), R.drawable.gnrback, null);

            //= ResourcesCompat.getDrawable(getResources(), R.drawable.arrow, null);
            if (key.equals("Utilitati")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.utilitati2, null);
            } else if (key.equals("Economii și investiții")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.investitii, null);
            } else if (key.equals("Alimentație și servire")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.mancare2, null);
            } else if (key.equals("Cumpărături")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.cumparaturi2, null);
            } else if (key.equals("Transferuri")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.transfer2, null);
            } else if (key.equals("General")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.general2, null);
            } else if (key.equals("Transport și mașină")) {
                dr = ResourcesCompat.getDrawable(getResources(), R.drawable.transport2, null);
            }
            if (-suma > 0) {
                entries.add(new PieEntry(-suma, dr));
                sum += suma;
                nrTranzactii += categorii.get(key).size();
                RaportTranzactie row = new RaportTranzactie();
                row.setImgResource(dr);
                row.setNrTranzactii(categorii.get(key).size());
                row.setSuma(suma);
                row.setNumeCategorie(key);
                row.setTranzactii(categorii.get(key));
                raport.add(row);

            } else if (key.equals("Bani Primiti") && suma > 0) {
                RaportTranzactie row = new RaportTranzactie();
                row.setNrTranzactii(categorii.get(key).size());
                row.setSuma(suma);
                row.setNumeCategorie(key);
                row.setTranzactii(categorii.get(key));
                raport.add(row);
            }
        }
        raport.sort(new Comparator<RaportTranzactie>() {
            @Override
            public int compare(RaportTranzactie raportTranzactie, RaportTranzactie t1) {
                return Float.compare(raportTranzactie.getSuma(), t1.getSuma());
            }
        });
        GetColorCategorie(raport);
        entries.sort(new Comparator<PieEntry>() {
            @Override
            public int compare(PieEntry pieEntry, PieEntry t1) {
                return -Float.compare(pieEntry.getValue(), t1.getValue());
            }
        });
        PieDataSet dataSet = new PieDataSet(entries, "   ");

        dataSet.setColors(RaportTranzactie.culori);
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(2f);

        RaportTranzactie.sumaTotala = -sum;

        tvNrTranzactii.setText(nrTranzactii + " tranzactii");
        tvSuma.setText(String.format("%.2f", -sum) + " RON");

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        //data.setValueFormatter(new PercentFormatter(pieChart));

        pieChart.setData(data);
        pieChart.invalidate();

        ReportAdapter adapter = new ReportAdapter(getContext(), R.layout.reportsdesign, R.layout.lastrowcategorii, raport, getLayoutInflater());
        lvRaport.setAdapter(adapter);

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

    private void GetColorCategorie(List<RaportTranzactie> raport) {
        RaportTranzactie.culori.clear();
//        for(int culoare: ColorTemplate.MATERIAL_COLORS){
//            RaportTranzactie.culori.add(culoare);
//        }
//        for(int culoare: ColorTemplate.VORDIPLOM_COLORS){
//            RaportTranzactie.culori.add(culoare);
//        }
        for (int i = 0; i < raport.size(); i++) {
            if (raport.get(i).getNumeCategorie().equals("Utilitati")) {
                RaportTranzactie.culori.add(Color.rgb(216, 111, 87));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.utlback, null));
            } else if (raport.get(i).getNumeCategorie().equals("Economii și investiții")) {
                RaportTranzactie.culori.add(Color.rgb(120, 167, 165));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.invback, null));
            } else if (raport.get(i).getNumeCategorie().equals("Alimentație și servire")) {
                RaportTranzactie.culori.add(Color.rgb(206, 91, 124));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.mncback, null));
            } else if (raport.get(i).getNumeCategorie().equals("Cumpărături")) {
                RaportTranzactie.culori.add(Color.rgb(209, 177, 71));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.cmpback, null));
            } else if (raport.get(i).getNumeCategorie().equals("Transferuri")) {
                RaportTranzactie.culori.add(Color.rgb(149, 132, 169));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.trfback, null));
            } else if (raport.get(i).getNumeCategorie().equals("General")) {
                RaportTranzactie.culori.add(Color.rgb(96, 148, 206));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.gnrback, null));
            } else if (raport.get(i).getNumeCategorie().equals("Transport și mașină")) {
                RaportTranzactie.culori.add(Color.rgb(97, 180, 173));
                raport.get(i).setImgResource(ResourcesCompat.getDrawable(getResources(), R.drawable.msnback, null));
            }
        }
    }

    private void selecteazaButon(Button butonApasat) {
        butonApasat.setBackgroundColor(Color.WHITE);
        butonApasat.setTextColor(ContextCompat.getColor(getContext(), R.color.purple_500));
    }

    private void deselecteazaButon(Button butonApasat) {
        butonApasat.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.purple_500));
        butonApasat.setTextColor(Color.WHITE);
    }


    public void showTranzactii(@NonNull View view, RaportTranzactie tranzactiiDialog) {
        dialog2 = new Dialog(getContext());
        dialog2.setContentView(R.layout.tranzactiiraportdialog);
        dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        dialog2.setCancelable(true);
        dialog2.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        Button closeBtn = dialog2.findViewById(R.id.btnCloseCateg);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();
            }
        });
        Drawable dr=tranzactiiDialog.getImgResource();
        int pozitive=-1;
        if(tranzactiiDialog.getNumeCategorie().equals("Bani Primiti")) {
            pozitive = +1;
            dr=ResourcesCompat.getDrawable(getResources(), R.drawable.bnpback, null);
        }
        TransactionAdapter adapter = new TransactionAdapter(dialog2.getContext(),
                R.layout.tranzactieraportrow, tranzactiiDialog.getTranzactii(), getLayoutInflater(),pozitive,dr);

        TextView tvCateg=dialog2.findViewById(R.id.denCateg);
        tvCateg.setText(tranzactiiDialog.getNumeCategorie());
        TextView tvSuma=dialog2.findViewById(R.id.categSum);
        tvSuma.setText(pozitive*tranzactiiDialog.getSuma()+" RON");
        TextView tvNrTranz=dialog2.findViewById(R.id.nrTranzcateg);
        tvNrTranz.setText(tranzactiiDialog.getNrTranzactii()+" tranzactii");
        ListView listView = dialog2.findViewById(R.id.lvcategtranz);
        listView.setAdapter(adapter);

        dialog2.show();


    }

    private void initialAnimations() {

//        View view = this.getView();
//        ImageView transBackground;
//        Animation fromBottomAnimation;
//        ConstraintLayout transactionsInfo;
//
//        transBackground= view.findViewById(R.id.reports_background);
//        transactionsInfo=view.findViewById(R.id.reports_info);
//
//        fromBottomAnimation= AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_faster);
//
//        transBackground.animate().translationY(-1250).setDuration(500);
//        transactionsInfo.startAnimation(fromBottomAnimation);
    }
}
