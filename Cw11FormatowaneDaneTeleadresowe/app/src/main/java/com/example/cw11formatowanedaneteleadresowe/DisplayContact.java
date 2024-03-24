package com.example.cw11formatowanedaneteleadresowe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends Fragment {
    TextView emailTextView, phoneTextView, nameTextView;
    public DisplayContact() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_contact, container, false);
        nameTextView = view.findViewById(R.id.nameTextView);
        phoneTextView = view.findViewById(R.id.phoneTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        DataViewModel dataVM = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        dataVM.getSharedData().observe(getViewLifecycleOwner(), stringSpannableHashMap -> {
            nameTextView.setText(stringSpannableHashMap.get("Display_name"));
            phoneTextView.setText(stringSpannableHashMap.get("Phone_number"));
            emailTextView.setText(stringSpannableHashMap.get("Email"));
        });
        return view;
    }
}