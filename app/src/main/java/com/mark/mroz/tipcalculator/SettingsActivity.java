package com.mark.mroz.tipcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button backButton, clearButton, setButton;
    EditText tipField;
    Spinner currencySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        setDefaultValues();
    }

    private void init() {
        layoutToolBar();
        layoutSubviews();
    }

    private void layoutToolBar() {

    }

    private void layoutSubviews() {

        backButton = (Button)findViewById(R.id.backButton_settings);
        backButton.setOnClickListener(this);

        clearButton = (Button)findViewById(R.id.clearButton_settings);
        clearButton.setOnClickListener(this);

        tipField = (EditText)findViewById(R.id.tipAmountTextView_settings);

        tipField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                if (!tipField.getText().toString().isEmpty()) {
                    editor.putInt("default_tip_value", Integer.parseInt(tipField.getText().toString()));

                } else {
                    editor.remove("default_tip_value"); // will delete key name
                }

                editor.commit();

                Context context = getApplicationContext();
                CharSequence text = "Saved!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });



        currencySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.country_arrays, R.layout.spinner_item);
        currencySpinner.setAdapter(adapter);

        currencySpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton_settings:

                Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
                startActivity(intent);

                break;
            case R.id.clearButton_settings:
                tipField.setText("");
                currencySpinner.setSelection(0);

                Context context = getApplicationContext();
                CharSequence text = "Cleared";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                break;
        }
    }

    private void setDefaultValues() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        if (pref.contains("default_tip_value")) {
            tipField.setText(pref.getInt("default_tip_value", 0) + "");
        }

        String lol = pref.getString("default_currency_value", null);

        String currentCurreny = pref.getString("default_currency_value", "$");

        List<String> currencyTypes = Arrays.asList(getResources().getStringArray(R.array.country_arrays));

        currencySpinner.setSelection(currencyTypes.indexOf(currentCurreny));
    }

    private void showError(EditText textEdit, String errorMsg) {
        textEdit.setError(errorMsg);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("default_currency_value", currencySpinner.getSelectedItem().toString());
        editor.commit();

        Context context = getApplicationContext();
        CharSequence text = "Saved!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
