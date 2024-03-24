package com.example.cw11formatowanedaneteleadresowe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.provider.ContactsContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;

import java.util.HashMap;

public class PhonePicker extends Fragment {
    private static final int REQUEST_READ_CONTACTS_PERMISSION = 0;
    public View view;

    public PhonePicker() {}

    private boolean hasContactsPermission()
    {
        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestContactsPermission()
    {
        if (!hasContactsPermission())
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
    }
    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode != REQUEST_READ_CONTACTS_PERMISSION || data == null) return;

        String phoneNumber, emailAddress, displayName, contactId;

        Uri contactUri = data.getData();
        Cursor cursor = getContext().getContentResolver().query(contactUri, null, null, null, null);
        if (cursor == null) return;

        try {
            if (cursor.moveToFirst()) {
                contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Spannable spnDisplayName = formatText("Imie: " + displayName, Color.DKGRAY, 2f);

                Cursor phoneCursor = getContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null
                );

                Spannable spnPhone = null;
                if (phoneCursor != null) {
                    try {
                        if (phoneCursor.moveToFirst()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            int color = phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME ?
                                    Color.rgb(159, 251, 136) : Color.rgb(8, 74, 8);
                            spnPhone = formatText("Telefon: " + phoneNumber, color, phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME ?
                                    1f : 1f, phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME);
                        }
                    } finally {
                        phoneCursor.close();
                    }
                }

                Cursor emailCursor = getContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{contactId},
                        null
                );

                Spannable spnEmail = null;
                if (emailCursor != null) {
                    try {
                        if (emailCursor.moveToFirst()) {
                            emailAddress = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                            spnEmail = formatText("Email: " + emailAddress, Color.MAGENTA, 1f);
                        }
                    } finally {
                        emailCursor.close();
                    }
                }

                DataViewModel dataVM = new ViewModelProvider(requireActivity()).get(DataViewModel.class);

                HashMap<String, Spannable> contactInfo = new HashMap<>();
                contactInfo.put("Display_name", spnDisplayName);
                contactInfo.put("Phone_number", spnPhone);
                contactInfo.put("Email", spnEmail);
                dataVM.setData(contactInfo);
            }
        } finally {
            cursor.close();
        }
    }

    private Spannable formatText(String text, int color, float sizeMultiplier) {
        return formatText(text, color, sizeMultiplier, false);
    }

    private Spannable formatText(String text, int color, float sizeMultiplier, boolean bold) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(sizeMultiplier), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (bold) {
            spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_phone_picker, container, false);

        Button contactPicker = view.findViewById(R.id.buttonPickContact);
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        contactPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent, REQUEST_READ_CONTACTS_PERMISSION);
            }
        });

        requestContactsPermission();

        return view;
    }


}
