package com.example.escbasicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
        checkPermissions();
        if(phoneNum.getText().length() == 0)
        {
            message.setVisibility(View.GONE);
            backspace.setVisibility(View.GONE);
        }
    }

    private void checkPermissions()
    {
        int resultCall = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int resultSms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);


        if(resultCall == PackageManager.PERMISSION_DENIED || resultSms == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS},1004);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1004)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED )
            {
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "PERMISSION DENIED, PLEASE GRANT PERMISSION AT SETTINGS MENU.", Toast.LENGTH_SHORT).show();
                Log.d("PermissionDenied", "권한이 거부되어 앱을 종료합니다.");
                finish();
            }
        }
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

        name = findViewById(R.id.main_tv_name);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addIntent = new Intent(MainActivity.this, AddEditActivity.class);
                addIntent.putExtra("phone_num",phoneNum.getText().toString());
                addIntent.putExtra("add_edit","add");
                startActivity(addIntent);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactIntent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(contactIntent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(MainActivity.this,MessageActivity.class);
                messageIntent.putExtra("phone_num",phoneNum.getText().toString());
                startActivity(messageIntent);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum.getText()));
                startActivity(callIntent);
            }
        });
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneNum.getText().length() == 0)
                    return;
                phoneNum.setText(iHateHyphen(phoneNum.getText().subSequence(0,phoneNum.getText().length()-1).toString()));
                findPhone();
                if(phoneNum.getText().length() == 0)
                {
                    message.setVisibility(View.GONE);
                    backspace.setVisibility(View.GONE);
                }
            }
        });
        backspace.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                phoneNum.setText("");
                findPhone();
                message.setVisibility(View.GONE);
                backspace.setVisibility(View.GONE);
                return true;
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
                message.setVisibility(View.VISIBLE);
                backspace.setVisibility(View.VISIBLE);
                findPhone();
            }

});

    }
    private void findPhone()
    {
        String find = phoneNum.getText().toString().replaceAll("-","");
        Boolean found = false;

        for (int i = 0; i < DummyData.contacts.size(); i++) {
            if(DummyData.contacts.get(i).getPhone().toString().replaceAll("-","").contains(find) && !found)
            {
                found = true;
                name.setText(DummyData.contacts.get(i).getName() + " : " + DummyData.contacts.get(i).getPhone());
            }
            else if (DummyData.contacts.get(i).getPhone().toString().replaceAll("-","").contains(find) && found)
            {
                name.setText(name.getText().toString() + "\n" + DummyData.contacts.get(i).getName() + " : " + DummyData.contacts.get(i).getPhone());
            }
        }
        if(phoneNum.length() <= 3 || !found)
            name.setText("");
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