package com.example.flashcardapp;

import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        Button b = v.findViewById(R.id.registrationButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText name = v.findViewById(R.id.nameInput);
                EditText age = v.findViewById(R.id.ageInput);
                EditText username = v.findViewById(R.id.usernameInput);
                EditText password = v.findViewById(R.id.passwordInput);

                RadioButton b1 = v.findViewById(R.id.radioButton);
                RadioButton b2 = v.findViewById(R.id.radioButton2);
                RadioButton b3 = v.findViewById(R.id.radioButton3);

                String genderStr = "";

                if (b1.isChecked()) {
                    genderStr = b1.getText().toString();
                } else if (b2.isChecked()) {
                    genderStr = b2.getText().toString();
                } else if (b3.isChecked()) {
                    genderStr = b3.getText().toString();
                }

                String nameStr = name.getText().toString();
                String ageStr = age.getText().toString();
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                TextView error = v.findViewById(R.id.errorMsg);

                boolean hasError = false;
                error.setText("");

                if (usernameStr.length() < 5 ) {
                    error.setText("Username must be at least of length 5");
                    hasError = true;
                }
                if (passwordStr.length() != 8) {
                    error.setText(error.getText().toString() + "\nPassword must be of length 8");
                    hasError = true;
                }


                if (!hasError && !nameStr.isEmpty() && !ageStr.isEmpty() && !genderStr.isEmpty()
                        && !usernameStr.isEmpty() && !passwordStr.isEmpty()) {

                    DBUserInfo db = new DBUserInfo(getActivity(), "USER");


                    try {
                        String hashPassword = new String(DBUserInfo.messageDigest(passwordStr));
                        Log.d("==Updated HASH PASSWORD==",hashPassword);
                        Log.d("==USERNAME=============",usernameStr);

                        error.setText("");
                        String exists = db.selectQuery("username", "username= \"" +usernameStr + "\"");
                        if (!exists.isEmpty()) {
                            error.setText("Username is already taken");
                            return;
                        } else {
                            db.addInfo(usernameStr, nameStr, Integer.parseInt(ageStr), genderStr, hashPassword);
                        }

                    } catch (NoSuchAlgorithmException e) {
                        System.out.println("Exception thrown for incorrect algorithm: " + e);
                    }

                    name.setText("");
                    age.setText("");
                    username.setText("");
                    password.setText("");
                    error.setText("");
                    b1.setChecked(false);
                    b2.setChecked(false);
                    b3.setChecked(false);
                }
            }
        });

        return v;
    }
}