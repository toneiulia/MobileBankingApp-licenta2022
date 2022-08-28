package com.example.mobilebankinglicenta.customDialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.mobilebankinglicenta.R;
import com.example.mobilebankinglicenta.UtilitatiSingleton;
import com.example.mobilebankinglicenta.classes.cards.Transaction;
import com.example.mobilebankinglicenta.fragments.HomeFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ExtrasDeContDialog extends DialogFragment {

    public static final String TAG = "ExtrasDeContDialog";
    private File filePath= new File(Environment.getExternalStorageDirectory() + "/ExtrasDeCont.xls");


    private Button btnDeLa;
    private Button btnPanaLa;
    private Button btnCloseExtras;
    private Button btnExtrasCont;
    private TextView tvData1;
    private TextView tvData2;

    public FirebaseFirestore database = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.extras_de_cont_dialog, null);

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        btnDeLa = view.findViewById(R.id.btnDeLa);
        btnPanaLa = view.findViewById(R.id.btnPanaLa);
        btnCloseExtras = view.findViewById(R.id.btnCloseExtras);
        btnExtrasCont = view.findViewById(R.id.btnExtrasCont);
        tvData1= view.findViewById(R.id.tvData1);
        tvData2= view.findViewById(R.id.tvData2);


        btnCloseExtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnDeLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                int an=calendar.get(Calendar.YEAR);
                int luna=calendar.get(Calendar.MONTH);
                int zi=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog;
                datePickerDialog=new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dataCautata = ("0"+dayOfMonth).substring(("0"+dayOfMonth).length()-2) + "/" + ("0"+(int)(month+1)).substring(("0"+(int)(month+1)).length()-2) + "/" + year;
                                tvData1.setText(dataCautata);
                            }

                        },an,luna,zi);
                datePickerDialog.show();
            }
        });

        btnPanaLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar= Calendar.getInstance();
                int an=calendar.get(Calendar.YEAR);
                int luna=calendar.get(Calendar.MONTH);
                int zi=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog;
                datePickerDialog=new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dataCautata = ("0"+dayOfMonth).substring(("0"+dayOfMonth).length()-2) + "/" + ("0"+(int)(month+1)).substring(("0"+(int)(month+1)).length()-2) + "/" + year;
                                tvData2.setText(dataCautata);
                            }

                        },an,luna,zi);
                datePickerDialog.show();
            }
        });

        btnExtrasCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1=sdf.parse(tvData1.getText().toString());
                    Date date2=sdf.parse(tvData2.getText().toString());

                    List<Transaction> transactionsWithinDates=new ArrayList<>();
                    for(Transaction transaction: UtilitatiSingleton.transactionList){
                        Date transactionDate=sdf.parse(transaction.getDate());

                        assert transactionDate != null;
                        if(transactionDate.compareTo(date1)>=0 && transactionDate.compareTo(date2)<=0){
                            transactionsWithinDates.add(transaction);
                        }
                    }

                    if(transactionsWithinDates.size()==0){
                        new AlertDialog.Builder(view.getContext())
                                .setIcon(android.R.drawable.ic_delete)
                                .setTitle("Nu exista tranzactii in perioada aleasa!")
                                .setMessage("Selectati o alta perioada!")
                                .setNegativeButton("OK",null)
                                .show();
                    }
                    else{

                        String senderEmail = "tone_iulia24@yahoo.com";
                        String senderPassword = "orcatjpozzcxbehd";

                        Properties properties=new Properties();
                        properties.put("mail.smtp.auth","true");
                        properties.put("mail.smtp.starttls.enable","true");
                        properties.put("mail.smtp.host","smtp.mail.yahoo.com");
                        properties.put("mail.smtp.port","587");

                        Session session = Session.getInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(senderEmail,senderPassword);
                            }
                        });

                        try {
                            Message message= new MimeMessage(session);
                            message.setFrom(new InternetAddress(senderEmail));

                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse("toneiulia24@gmail.com"));

                            message.setSubject("Extras de cont");

                            genereazaFisierExcel(transactionsWithinDates);

                            Multipart emailContent= new MimeMultipart();
                            MimeBodyPart attachment= new MimeBodyPart();
                            attachment.attachFile(filePath);
                            emailContent.addBodyPart(attachment);

                            message.setContent(emailContent);

                            new SendMail().execute(message);
//                            sendEmail();

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;
    }

    private void genereazaFisierExcel(List<Transaction> transactionsWithinDates) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet=hssfWorkbook.createSheet();

        HSSFRow row = hssfSheet.createRow(0);
        HSSFCell cell1= row.createCell(0);
        cell1.setCellValue("Date");
        HSSFCell cell2= row.createCell(1);
        cell2.setCellValue("Description");
        HSSFCell cell3= row.createCell(2);
        cell3.setCellValue("From/To");
        HSSFCell cell4= row.createCell(3);
        cell4.setCellValue("Amount");
        for (int i=0;i<transactionsWithinDates.size()-1;i++){
            HSSFRow row_i = hssfSheet.createRow(i+1);
            HSSFCell cell1_i= row_i.createCell(0);
            cell1_i.setCellValue(transactionsWithinDates.get(i+1).getDate());
            HSSFCell cell2_i= row_i.createCell(1);
            cell2_i.setCellValue(transactionsWithinDates.get(i+1).getDescription());
            HSSFCell cell3_i= row_i.createCell(2);
            String beneficiarOrExpeditor;
            if(transactionsWithinDates.get(i+1).getAmount()<0){
                beneficiarOrExpeditor=transactionsWithinDates.get(i+1).getCardTo();
            }
            else{
                beneficiarOrExpeditor=transactionsWithinDates.get(i+1).getCardFrom();
            }
//            if(beneficiarOrExpeditor.contains("p"))
//            {
//                cell3_i.setCellValue(HomeFragment.providersList.get(beneficiarOrExpeditor).getName());
//            }
//            else if(beneficiarOrExpeditor.contains("c")) {
//                String user = HomeFragment.cardList.get(beneficiarOrExpeditor).getOwner();
//                cell3_i.setCellValue(HomeFragment.userList.get(user).getFirstName());
//            }
            cell3_i.setCellValue(beneficiarOrExpeditor);
            HSSFCell cell4_i= row_i.createCell(3);
            cell4_i.setCellValue(transactionsWithinDates.get(i+1).getAmount());
        }
        try{
            if(!filePath.exists()){
                if (Build.VERSION.SDK_INT >= 30){
                    if (!Environment.isExternalStorageManager()){
                        Intent getpermission = new Intent();
                        getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(getpermission);
                    }
                }
                filePath.createNewFile();
            }
            FileOutputStream fileOutputStream=new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if(fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }

    private class SendMail extends AsyncTask<Message, String, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= ProgressDialog.show(getContext(),"Astepati o secunda","Se trimite mail-ul...", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {

            try {
                Transport.send(messages[0]);
                return "Succes!";
            } catch (MessagingException e) {

                e.printStackTrace();
                return "Error!";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Succes!")){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle(Html.fromHtml("<font color='#509324'>Succes</font>"))
                        .setMessage("Extrasul de cont a fost trimis prin mail!")
                        .setNegativeButton("OK",null)
                        .show();

                getDialog().dismiss();
            }
            else{
                Toast.makeText(getContext(), "Mail-ul nu a putut fi trimis", Toast.LENGTH_SHORT).show();
            }
        }

    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"toneiulia24@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            //Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
