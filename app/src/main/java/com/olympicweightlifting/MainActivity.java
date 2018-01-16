package com.olympicweightlifting;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface samsungSansBold = Typeface.createFromAsset(getAssets(), "SamsungSans-Bold.ttf");
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(samsungSansBold);

        Typeface montserrat = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf");
        TextView cardTitle = findViewById(R.id.card_title);
        cardTitle.setTypeface(montserrat);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
