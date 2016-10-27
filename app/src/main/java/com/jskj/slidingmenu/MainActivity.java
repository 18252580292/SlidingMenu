package com.jskj.slidingmenu;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SlidingLayout activity_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity_main = (SlidingLayout) findViewById(R.id.activity_main);
        activity_main.setOnWindowChanged(new SlidingLayout.OnWindowChanged() {
            @Override
            public void open() {
                Toast.makeText(MainActivity.this, "open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void close() {
            }
        });
    }
}
