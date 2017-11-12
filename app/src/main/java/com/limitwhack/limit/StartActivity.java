package com.limitwhack.limit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import io.realm.Realm;

public class StartActivity extends AppCompatActivity {
    Button submit;
    EditText weightEditText;
    EditText emergencyContact;
    Spinner genderSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle("Start");

        submit = findViewById(R.id.saveButton);
        weightEditText = findViewById(R.id.weightInput);
        emergencyContact = findViewById(R.id.contactEditText);
        genderSpinner = findViewById(R.id.genderSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();

                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        User user = new User();
                        user.setGender(genderSpinner.getSelectedItem().toString());
                        user.setEmergencyNumber(emergencyContact.getText().toString());
                        user.setWeight(Integer.parseInt(weightEditText.getText().toString()));

                         bgRealm.copyToRealm(user);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("added user", "onSuccess: ");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("fail to add user", error.toString());
                    }
                });






                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
