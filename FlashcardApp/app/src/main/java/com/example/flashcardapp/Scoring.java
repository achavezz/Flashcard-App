package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Scoring#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Scoring extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Scoring() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Scoring.
     */
    // TODO: Rename and change types and number of parameters
    public static Scoring newInstance(String param1, String param2) {
        Scoring fragment = new Scoring();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scoring, container, false);
        Log.d("=============Scoring Made=========", "made");
        int countCorrect = getArguments().getInt("countCorrect");
        int countIncorrect = getArguments().getInt("countIncorrect");
        String user = getArguments().getString("username");

        TextView countC = v.findViewById(R.id.fsC);
        TextView countI = v.findViewById(R.id.fsI);
        Button back = v.findViewById(R.id.backHome);

        countC.setText(countCorrect);
        countI.setText(countIncorrect);
        Log.d("=============Scoring made it this far", "here");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(getActivity(), HomePage.class);
                dash.putExtra("username", user);
                startActivity(dash);
            }
        });
        return v;
    }
}