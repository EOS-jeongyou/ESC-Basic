package com.example.escbasicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageButton addContact;
    private ImageButton contact;
    private TextView phoneNum;
    private TextView[] dials = new TextView[10];
    private TextView star;
    private TextView sharp;
    private ImageButton message;
    private ImageButton call;
    private ImageButton backspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
    }
    private void setUpUI()
    {
        addContact = findViewById(R.id.main_ibtn_add);
        contact = findViewById(R.id.main_itbn_contact);
        phoneNum = findViewById(R.id.main_tv_phone);
        for (int i = 0; i < dials.length; i++) {
            dials[i] = findViewById(getResourceID("main_tv_" + i, "id", this));
        }
        star = findViewById(R.id.main_tv_star);
        sharp = findViewById(R.id.main_tv_sharp);
        message = findViewById(R.id.main_ibtn_message);
        call = findViewById(R.id.main_ibtn_call);
        backspace = findViewById(R.id.main_ibtn_backspace);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Add Contact
                Toast.makeText(MainActivity.this, "AddContactTest", Toast.LENGTH_SHORT).show();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Address
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Message
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Call
            }
        });
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNum.getText().length() == 0)
                    return;
                phoneNum.setText(iHateHyphen(phoneNum.getText().subSequence(0,phoneNum.getText().length()-1).toString()));
            }
        });
        setOnClickDial(star, "*");
        setOnClickDial(sharp, "#");
        for (int i = 0; i < dials.length; i++) {
            setOnClickDial(dials[i],String.valueOf(i));

        }
    }
    private void setOnClickDial(View view, final String input)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNum.setText(iHateHyphen(phoneNum.getText() + input));
            }
        });
    }
    private int getResourceID(final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);
        if(ResourceID == 0)
            throw new IllegalArgumentException("No resource string found with name : "+resName);
        else
            return ResourceID;
    }
    private String iHateHyphen(String phoneNum)
    {
        phoneNum=phoneNum.replace("-","");
        if(phoneNum.contains("*") || phoneNum.contains("#"))
            return phoneNum;
        if(phoneNum.length() >= 4 && phoneNum.length() <= 7)
        {
            phoneNum = phoneNum.subSequence(0,3)+"-"+phoneNum.subSequence(3,phoneNum.length());
            return phoneNum;
        }
        if(phoneNum.length() >= 8 && phoneNum.length() <= 11)
        {
            phoneNum = phoneNum.subSequence(0,3)+"-"+phoneNum.subSequence(3,phoneNum.length());
            phoneNum = phoneNum.subSequence(0,8)+"-"+phoneNum.subSequence(8,phoneNum.length());
            return phoneNum;
        }
        if(phoneNum.length() >= 12)
            return phoneNum;

        return phoneNum;
    }
}