package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
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
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);


        Button b = v.findViewById(R.id.login);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = v.findViewById(R.id.usernameInput);
                EditText password = v.findViewById(R.id.passwordInput);

                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();

                if (!usernameStr.isEmpty() && !passwordStr.isEmpty()) {
                    DBUserInfo db = new DBUserInfo(getActivity(), "USER");

                    try {
                        String hashPassword = new String(DBUserInfo.messageDigest(passwordStr));

                        String qpd = db.selectQuery("password", "username= \"" +usernameStr + "\"");
                        Log.d("==HASH PASSWORD==", hashPassword);

                        TextView error = v.findViewById(R.id.errorMsg);

                        if (qpd.isEmpty()) {
                            error.setText("Wrong username");
                        } else if (qpd.equals(hashPassword)) {
//                          Log.d("==INSIDE==", "");
//                          MainActivity m = (MainActivity) getActivity();
//                          m.login();

                            error.setText("");
                            Intent next = new Intent(getActivity(), HomePage.class);

                            String name = db.selectQuery("name", "username= \"" +usernameStr + "\"");
                            next.putExtra("name", name);
                            next.putExtra("username", usernameStr);
                            startActivity(next);
                        } else {
                            error.setText("Wrong password");
                        }
                    } catch (NoSuchAlgorithmException e) {
                        System.out.println("Exception thrown for incorrect algorithm: " + e);
                    }

                }

            }
        });


        return v;
    }
}