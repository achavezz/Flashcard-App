package com.example.flashcardapp;

import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeleteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeleteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteFragment newInstance(String param1, String param2) {
        DeleteFragment fragment = new DeleteFragment();
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
        View v = inflater.inflate(R.layout.fragment_delete, container, false);

        AccountSettings act = (AccountSettings) v.getContext();
        Intent inte = act.getIntent();
        String usr = inte.getStringExtra("username");
        Button btn = v.findViewById(R.id.dltButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txt = v.findViewById(R.id.deleteInput);
                String pass = txt.getText().toString();

                if(!pass.isEmpty()){
                    DBUserInfo db = new DBUserInfo(getActivity(), "USER");
                    try{
                        TextView error = v.findViewById(R.id.errMsg3);
                        String hashedPassword = new String(DBUserInfo.messageDigest(pass));
                        String qpd = db.selectQuery("password", "username= \"" + usr + "\"");

                        if(qpd.equals(hashedPassword)){
                            error.setText("");
                            db.deleteQuery("username= \"" + usr + "\"");
                            Intent next = new Intent(getActivity(), MainActivity.class);
                            startActivity(next);
                        }else{
                            error.setText("Password is incorrect, please try again.");
                        }
                    }catch (NoSuchAlgorithmException e){
                        System.out.println("Exception thrown for incorrect algorithm: " + e);
                    }
                }
            }
        });
        return v;
    }
}