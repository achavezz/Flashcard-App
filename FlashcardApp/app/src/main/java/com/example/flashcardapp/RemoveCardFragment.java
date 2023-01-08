package com.example.flashcardapp;

import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemoveCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemoveCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RemoveCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RemoveCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RemoveCardFragment newInstance(String param1, String param2) {
        RemoveCardFragment fragment = new RemoveCardFragment();
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
        View v = inflater.inflate(R.layout.fragment_remove_card, container, false);

        Button removeID = v.findViewById(R.id.removeIndex);

        removeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText id = v.findViewById(R.id.cardNumInput);
                String idS = id.getText().toString();

                if (!idS.isEmpty()) {
                    DeckConfiguration dc = (DeckConfiguration) getActivity();
                    dc.removeCardFromCurrentDeck(Integer.parseInt(idS));

                    id.setText("");
                }
            }
        });


        return v;
    }
}