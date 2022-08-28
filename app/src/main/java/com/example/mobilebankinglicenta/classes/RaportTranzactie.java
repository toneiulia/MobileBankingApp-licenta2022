package com.example.mobilebankinglicenta.classes;

import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.example.mobilebankinglicenta.classes.cards.Transaction;

import java.util.ArrayList;
import java.util.List;

public class RaportTranzactie {
    public String numeCategorie;
    public List<Transaction> tranzactii;
    public Drawable imgResource;
    public float suma;
    public int nrTranzactii;
    public static ArrayList<Integer> culori= new ArrayList<>();
    public static float sumaTotala=0f;

    public RaportTranzactie() {
    }

    public RaportTranzactie(String numeCategorie, List<Transaction> tranzactii) {
        this.numeCategorie = numeCategorie;
        this.tranzactii = tranzactii;
    }

    public String getNumeCategorie() {
        return numeCategorie;
    }

    public void setNumeCategorie(String numeCategorie) {
        this.numeCategorie = numeCategorie;
    }

    public List<Transaction> getTranzactii() {
        return tranzactii;
    }

    public void setTranzactii(List<Transaction> tranzactii) {
        this.tranzactii = tranzactii;
    }

    public Drawable getImgResource() {
        return imgResource;
    }

    public void setImgResource(Drawable imgResource) {
        this.imgResource = imgResource;
    }

    public float getSuma() {
        return suma;
    }

    public void setSuma(float suma) {
        this.suma = suma;
    }

    public int getNrTranzactii() {
        return nrTranzactii;
    }

    public void setNrTranzactii(int nrTranzactii) {
        this.nrTranzactii = nrTranzactii;
    }


}
