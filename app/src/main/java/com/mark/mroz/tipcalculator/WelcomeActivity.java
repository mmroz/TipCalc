package com.mark.mroz.tipcalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, InputFilter {

    private Button incrementPersonButton, decremenetPersonButton, calculateButton, suggestButton, infoButton;
    private EditText billAmountTextView, numberOfPeopleTextView, tipPercentageTextView;
    private TextView currencySymbol;

    private static final Pattern mPattern = Pattern.compile("[0-9]{0," + (8-1) + "}+((\\.[0-9]{0," + (2-1) + "})?)||(\\.)?");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
        reviveToPreviousState();

    }

    private void init() {
        layoutToolBar();
        layoutSubviews();
    }

    private void layoutToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        infoButton = (Button)findViewById(R.id.settingsButton);
        infoButton.setOnClickListener(this);
    }

    private void layoutSubviews() {

        //Text View
        currencySymbol = (TextView)findViewById(R.id.bill_currencySymbol_welcome);

        //Buttons

        incrementPersonButton = (Button)findViewById(R.id.incrementButton);
        incrementPersonButton.setOnClickListener(this);
        decremenetPersonButton = (Button)findViewById(R.id.decremenetButton);
        decremenetPersonButton.setOnClickListener(this);
        calculateButton = (Button)findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(this);
        suggestButton = (Button)findViewById(R.id.suggestButton);
        suggestButton.setOnClickListener(this);

        //Text Edit

        billAmountTextView = (EditText) findViewById(R.id.billTotalTextView);
        billAmountTextView.setFilters(new InputFilter[] {this});
        numberOfPeopleTextView = (EditText)findViewById(R.id.splitWithTextView);
        tipPercentageTextView = (EditText)findViewById(R.id.tipPercentageTextView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.incrementButton:
                if (!numberOfPeopleTextView.getText().toString().isEmpty()) {
                    int incPeople = Integer.parseInt(numberOfPeopleTextView.getText().toString());
                    numberOfPeopleTextView.setText(++incPeople + "");
                }
                break;
            case R.id.decremenetButton:
                if (!numberOfPeopleTextView.getText().toString().isEmpty()) {
                    int decPeople = Integer.parseInt(numberOfPeopleTextView.getText().toString());
                    if (decPeople >= 2) {
                        numberOfPeopleTextView.setText(--decPeople + "");
                    }
                }
                break;
            case R.id.calculateButton:

                if (billAmountTextView.getText().toString().isEmpty() ||
                        numberOfPeopleTextView.getText().toString().isEmpty() ||
                        tipPercentageTextView.getText().toString().isEmpty() ||
                        Integer.parseInt(numberOfPeopleTextView.getText().toString()) == 0) {

                    if (billAmountTextView.getText().toString().isEmpty()) {
                        showError(billAmountTextView,"Bill total cannot be blank");
                    }

                    if (numberOfPeopleTextView.getText().toString().isEmpty()) {
                        showError(numberOfPeopleTextView,"Number of people cannot be blank");
                    } else if (Integer.parseInt(numberOfPeopleTextView.getText().toString()) == 0) {
                        showError(numberOfPeopleTextView,"Number of people cannot be 0");
                    }

                    if (tipPercentageTextView.getText().toString().isEmpty()) {
                        showError(tipPercentageTextView,"Tip percentage cannot be blank");
                    }


                } else {

                    Intent intent = new Intent(getBaseContext(), CalculateActivity.class);

                    intent.putExtra("bill", Float.parseFloat(billAmountTextView.getText().toString()));
                    intent.putExtra("people", Integer.parseInt(numberOfPeopleTextView.getText().toString()));
                    intent.putExtra("percentage", Integer.parseInt(tipPercentageTextView.getText().toString()));

                    startActivity(intent);
                }

                break;
            case R.id.suggestButton:

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(WelcomeActivity.this, suggestButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (!item.getTitle().toString().contains("Rating")) {

                            tipPercentageTextView.setText(calculateTip(Integer.parseInt(item.getTitle().toString())) + "");

                        } else {
                            return false;
                        }

                        return true;
                    }
                });

                popup.show();//showing popup menU

            break;
            case R.id.settingsButton:

                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);

                break;
        }
    }

    private int calculateTip(int rating) {
        return 10 + rating * 2;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher=mPattern.matcher(dest);
        if(!matcher.matches())
            return "";
        return null;
    }

    private boolean reviveToPreviousState() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        currencySymbol.setText(pref.getString("default_currency_value", "$"));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            float bill = extras.getFloat("bill");
            int people = extras.getInt("people");
            int percentage = extras.getInt("percentage");

            billAmountTextView.setText(bill + "");
            numberOfPeopleTextView.setText(people + "");
            tipPercentageTextView.setText(percentage + "");

            return true;

        } else if (pref.contains("default_tip_value")) {
            tipPercentageTextView.setText(pref.getInt("default_tip_value", 0) + "");
            return true;
        } else {
            return false;
        }

    }

    private void showError(EditText textEdit, String errorMsg) {
        textEdit.setError(errorMsg);
    }
}
