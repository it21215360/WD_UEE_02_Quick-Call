package com.example.ueeapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Rose_Chat extends Fragment {

    View vw;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vw= inflater.inflate(R.layout.fragment_rose__chat, container, false);
        return vw;
    }
}