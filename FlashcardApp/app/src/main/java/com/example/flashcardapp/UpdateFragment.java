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
 * Use the {@link UpdateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(String param1, String param2) {
        UpdateFragment fragment = new UpdateFragment();
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
        View v = inflater.inflate(R.layout.fragment_update, container, false);
        AccountSettings act = (AccountSettings) v.getContext();
        Intent inte = act.getIntent();
        String usr = inte.getStringExtra("username");
        Button btn = v.findViewById(R.id.updatebtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText newpass = v.findViewById(R.id.newpassInput);
                EditText oldpass = v.findViewById(R.id.oldpassInput);
                String newpasstxt = newpass.getText().toString();
                String oldpasstxt = oldpass.getText().toString();

                TextView error = v.findViewById(R.id.errorMsg2);

//                boolean hasError = false;
//                error.setText("");
//
//                if (newpasstxt.length() < 7) {
//                    error.setText("New Password must be at least of length 7");
//                    hasError = true;
//                }

                if(!newpasstxt.isEmpty() && !oldpasstxt.isEmpty()){
                    DBUserInfo db = new DBUserInfo(getActivity(), "USER");

                    try {
                        String hashOldPassword = new String(DBUserInfo.messageDigest(oldpasstxt));
                        String hashNewPassword = new String(DBUserInfo.messageDigest(newpasstxt));

                        String qpd = db.selectQuery("password", "username= \"" + usr + "\"");
                        Log.d("==HASH PASSWORD==", hashOldPassword);

                        if(newpasstxt.length() != 8) {
                            error.setText("Please make sure new password is of length 8.");
                        }else if (qpd.equals(hashOldPassword)) {
                            error.setText("");
                            db.updateQuery("password = \"" + hashNewPassword + "\"", "username= \"" + usr + "\"");
                            error.setText("Password has been updated.");
                            newpass.setText("");
                            oldpass.setText("");
                        }else{
                            error.setText("Old password is incorrect, please try again.");
                        }
                    }catch (NoSuchAlgorithmException e) {
                        System.out.println("Exception thrown for incorrect algorithm: " + e);
                    }
                }

            }
        });



        return v;
    }
}