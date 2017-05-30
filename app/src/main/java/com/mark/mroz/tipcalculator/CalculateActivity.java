package com.mark.mroz.tipcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


public class CalculateActivity extends AppCompatActivity implements View.OnClickListener {

    private Button doneButton, backButton;
    private TextView billAmountTextView, tipTotalTextView, totalTextView, tipPerPersonTextView, eachPersonTextView;
    private TextView currencyBill, currencyTip, currencyTotal, currencyTipPP, currencyTotalPP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        init();
        setDefaults();


    }

    private void init() {

        layoutSubviews();
    }

    private void layoutSubviews() {

        currencyBill = (TextView)findViewById(R.id.bill_currencySymbol_calculate);
        currencyTip = (TextView)findViewById(R.id.tip_currencySymbol_calculate);
        currencyTotal = (TextView)findViewById(R.id.total_currencySymbol_calculate);
        currencyTipPP = (TextView)findViewById(R.id.tip_pp_currencySymbol_calculate);
        currencyTotalPP = (TextView)findViewById(R.id.total_pp_currencySymbol_calculate);

        doneButton = (Button)findViewById(R.id.doneButton_calculate);
        doneButton.setOnClickListener(this);
        backButton = (Button)findViewById(R.id.backButton_calculate);
        backButton.setOnClickListener(this);

        //Text Views
        billAmountTextView = (TextView) findViewById(R.id.billAmountTextView);
        tipTotalTextView = (TextView)findViewById(R.id.tipAmountTextView);
        totalTextView = (TextView)findViewById(R.id.totalAmountTextView);
        tipPerPersonTextView = (TextView)findViewById(R.id.tipPerPersonTextView);
        eachPersonTextView = (TextView)findViewById(R.id.totalPerPersonTextView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            float bill =  extras.getFloat("bill");
            int people =  extras.getInt("people");
            float percentage =  extras.getInt("percentage") / (float) 100;

            billAmountTextView.setText(String.format("%.2f", bill));

            float tipTotal = (float) bill * percentage;

            tipTotalTextView.setText(String.format("%.2f", tipTotal));

            float totalWithTip = (float) bill * (1 + percentage);

            totalTextView.setText(String.format("%.2f", totalWithTip));



            if (people >= 2) {

                float tipPerPerson = tipTotal / (float)people;

                tipPerPersonTextView.setText(String.format("%.2f", tipPerPerson));;

                float totalPerPerson = totalWithTip / (float)people;

                eachPersonTextView.setText(String.format("%.2f", totalPerPerson));
            } else {
                findViewById(R.id.mulitperson_tip).setVisibility(View.GONE);
                findViewById(R.id.mulitperson_total).setVisibility(View.GONE);

                findViewById(R.id.single_person_bottom).setBackgroundDrawable(getResources().getDrawable(R.drawable.border_bottom));

            }
        }
    }

    private void setDefaults() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode

        currencyBill.setText(pref.getString("default_currency_value", "$"));
        currencyTip.setText(pref.getString("default_currency_value", "$"));;
        currencyTotal.setText(pref.getString("default_currency_value", "$"));;
        currencyTipPP.setText(pref.getString("default_currency_value", "$"));;
        currencyTotalPP.setText(pref.getString("default_currency_value", "$"));;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doneButton_calculate:
                Intent doneIntent = new Intent(getBaseContext(), WelcomeActivity.class);
                startActivity(doneIntent);
                break;
            case R.id.backButton_calculate:
                Intent backIntent = new Intent(getBaseContext(), WelcomeActivity.class);

                Bundle extras = getIntent().getExtras();
                if (extras != null) {

                    float bill = extras.getFloat("bill");
                    int people = extras.getInt("people");
                    int percentage = extras.getInt("percentage");

                    backIntent.putExtra("bill", bill);
                    backIntent.putExtra("people", people);
                    backIntent.putExtra("percentage", percentage);

                }

                startActivity(backIntent);
                break;
        }
    }



}
