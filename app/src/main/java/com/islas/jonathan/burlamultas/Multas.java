package com.islas.jonathan.burlamultas;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link Multas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Multas extends Fragment {

    public static Multas newInstance() {
        Multas fragment = new Multas();
        Bundle args = new Bundle();
        return fragment;
    }

    public Multas() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multas, container, false);
    }

}
