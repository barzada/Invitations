package com.example.invitations;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

public class FinishPage extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.finish_page);

            // Get the title from the intent
            String title = getIntent().getStringExtra("title");

            // Display the title in a TextView
            TextView titleTextView = findViewById(R.id.titleTextView);
            titleTextView.setText(title);

    }
}
