package com.example.convertercurrency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CalculatePage extends AppCompatActivity implements View.OnClickListener {
    TextView ans1, ans2;
    ImageView change;
    EditText first, second;
    String numberCode, nameCode, currencyName, defaultCurrency = "RUB";
    double course, units;
    boolean checker = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_page);
        init();
        actions();
    }

    @SuppressLint("SetTextI18n")
    private void actions() {
        ans1.setOnClickListener(this);
        ans2.setOnClickListener(this);
        change.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras.getString("TYPE").equals("FIRST")) {
            String temp = extras.getString("VALUE");
            String[] tempList = temp.split(",");
            nameCode = tempList[0];
            units = Double.parseDouble(tempList[1].replace(',', '.'));
            course = Double.parseDouble(tempList[2].replace(',', '.'));
        } else if (extras.getString("TYPE").equals("SECOND")) {
            String temp = extras.getString("VALUE");
            String temp2 = extras.getString("VALUE2");
            String[] tempList = temp.split(",");
            String[] tempList2 = temp2.split(",");
            nameCode = tempList[0];
            course = Double.parseDouble(tempList2[1].replace(',', '.'));

            defaultCurrency = tempList2[0];
            units = Double.parseDouble(tempList2[2].replace(',', '.'));


        } else if (extras.getString("TYPE").equals("NAN")) {
            numberCode = extras.getString("NUMCODE");
            nameCode = extras.getString("ALPHCODE");
            units = Double.parseDouble(extras.getString("UNIT"));
            currencyName = extras.getString("NAMECUR");
            course = Double.parseDouble(extras.getString("COURSE").replace(',', '.'));
        }


        first.setText(String.valueOf(units));
        second.setText(String.valueOf(course));
        ans1.setText(nameCode);
        ans2.setText(defaultCurrency);

        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(""))
                    if (checker) {
                        checker = false;
                        double mean = Double.parseDouble(charSequence.toString().replace(',', '.'));
                        second.setText(String.valueOf((course * mean) / units));
                    } else {
                        checker = true;
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(""))
                    if (checker) {
                        checker = false;
                        double mean = Double.parseDouble(charSequence.toString().replace(',', '.'));
                        first.setText(String.valueOf((units * mean) / course));
                    } else {
                        checker = true;
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void init() {
        ConstraintLayout constraintLayout = findViewById(R.id.calculateLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);

        change = findViewById(R.id.change);

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Intent changeCur = new Intent(this, ChangeCurrency.class);
        switch (view.getId()) {
            case R.id.ans1:
                changeCur.putExtra("VALUE", "0,0,0");
                changeCur.putExtra("TYPE", "FIRST");
                startActivity(changeCur);
                break;
            case R.id.ans2:
                changeCur.putExtra("VALUE", nameCode + "," + units + "," + course);
                changeCur.putExtra("TYPE", "SECOND");
                startActivity(changeCur);
                break;
            case R.id.change:
                break;
        }
    }
}