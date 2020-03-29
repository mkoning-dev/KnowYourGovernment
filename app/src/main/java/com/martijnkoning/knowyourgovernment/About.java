package com.martijnkoning.knowyourgovernment;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView url = findViewById(R.id.google);
        url.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
