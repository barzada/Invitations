package com.example.invitations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
private EditText titleEditText;
    private EditText locationEditText;
    private EditText  startDatetimeEditText;
    private EditText  endDatetimeEditText;
    private EditText descriptionEditText;
    private static final int PERMISSION_REQUEST_WRITE_CALENDAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextInputLayout titleInputLayout = findViewById(R.id.titleTextView);
         titleEditText=titleInputLayout.getEditText();
        TextInputLayout locationInputLayout = findViewById(R.id.locationTextView);
         locationEditText=locationInputLayout.getEditText();
        TextInputLayout  startDatetimeInputLayout = findViewById(R.id.startdatetimeTextView);
          startDatetimeEditText=startDatetimeInputLayout.getEditText();
        TextInputLayout  endDatetimeInputLayout = findViewById(R.id.enddatetimeTextView);
       endDatetimeEditText=endDatetimeInputLayout.getEditText();
        TextInputLayout  descriptionInputLayout = findViewById(R.id.descriptionTextView);
         descriptionEditText=descriptionInputLayout.getEditText();


        // Load and parse the JSON file
        try {
            String jsonString = loadJSONFromAsset("event.json");
            JSONObject eventJson = new JSONObject(jsonString);

            // Retrieve event details from JSON object
            String title = eventJson.getString("title");
            String location = eventJson.getString("location");
            String startDateTime = eventJson.getString("startDateTime");
            String endDateTime = eventJson.getString("endDateTime");
            String description = eventJson.getString("description");
            // Set the event details to the TextViews
            titleEditText.setText(title);
            locationEditText.setText(location);
            startDatetimeEditText.setText(startDateTime);
            startDatetimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    Calendar selectedCalendar = Calendar.getInstance();
                                                    selectedCalendar.set(year, month, dayOfMonth, hourOfDay, minute);

                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                                                    String selectedDateTime = dateFormat.format(selectedCalendar.getTime());
                                                    startDatetimeEditText.setText(selectedDateTime);
                                                }
                                            }, hour, minute, false);

                                    timePickerDialog.show();
                                }
                            }, year, month, dayOfMonth);

                    datePickerDialog.show();
                }
            });
            endDatetimeEditText.setText(endDateTime);
            endDatetimeEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                                            new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    Calendar selectedCalendar = Calendar.getInstance();
                                                    selectedCalendar.set(year, month, dayOfMonth, hourOfDay, minute);

                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                                                    String selectedDateTime = dateFormat.format(selectedCalendar.getTime());
                                                    endDatetimeEditText.setText(selectedDateTime);
                                                }
                                            }, hour, minute, false);

                                    timePickerDialog.show();
                                }
                            }, year, month, dayOfMonth);

                    datePickerDialog.show();
                }
            });
            descriptionEditText.setText(description);

        } catch (JSONException e) {
            e.printStackTrace();
        }

            Button addEventButton = findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for calendar write permission
                if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                    addEventToCalendar();
                } else {
                    // Request permission
                    requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_REQUEST_WRITE_CALENDAR);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addEventToCalendar();
            } else {
                Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addEventToCalendar() {
        // Read the event details from the local JSON file
        String jsonString = loadJSONFromAsset("event.json");
        if (jsonString == null) {
            Toast.makeText(this, "Error reading JSON file", Toast.LENGTH_SHORT).show();
            return;
        }

            try {

                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String location = locationEditText.getText().toString();
                String startDateTimeStr = startDatetimeEditText.getText().toString();
                String endDateTimeStr =endDatetimeEditText.getText().toString();

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                Date startDateTime = format.parse(startDateTimeStr);
                Date endDateTime = format.parse(endDateTimeStr);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(startDateTime);

                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.TITLE, title);
                values.put(CalendarContract.Events.DESCRIPTION, description);
                values.put(CalendarContract.Events.EVENT_LOCATION, location);
                values.put(CalendarContract.Events.DTSTART, calendar.getTimeInMillis());
                values.put(CalendarContract.Events.DTEND, endDateTime.getTime());
                values.put(CalendarContract.Events.CALENDAR_ID, 1);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            // Insert the event
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                cr.insert(CalendarContract.Events.CONTENT_URI, values);
                Toast.makeText(this, "Event added to calendar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error parsing date", Toast.LENGTH_SHORT).show();
        }
    }

    public String loadJSONFromAsset(String filename) {
        String jsonString;
        try {
            InputStream inputStream = getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }
}






//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import android.provider.CalendarContract;
//import android.database.Cursor;
//import android.content.ContentValues;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.content.Context;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//
//   // private static final String CALENDAR_URL = "C:\\Users\\Bar\\Desktop\\לימודים\\פרוייקט גמר\\events\\event.json";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//       // new GetCalendarDataTask().execute(CALENDAR_URL);
//
//        EditText eventTitleEditText = findViewById(R.id.eventTitle);
//        EditText eventLocationEditText = findViewById(R.id.eventLocation);
//        EditText eventStartDateTimeEditText = findViewById(R.id.eventStartDateTime);
//        EditText eventEndDateTimeEditText = findViewById(R.id.eventEndDateTime);
//
//        Button createEventButton = findViewById(R.id.createEventButton);
//        createEventButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String eventTitle = eventTitleEditText.getText().toString();
//                String eventLocation = eventLocationEditText.getText().toString();
//                String eventStartDateTime = eventStartDateTimeEditText.getText().toString();
//                String eventEndDateTime = eventEndDateTimeEditText.getText().toString();
//                // Use the event details to create an event in the calendar
//            }
//        });
//    }
//
//    private class GetCalendarDataTask extends AsyncTask<String, Void, JSONObject> {
//
//        @Override
//        protected JSONObject doInBackground(String... urls) {
//            JSONObject result = null;
//
//            try {
//                Context context = getApplicationContext();
//                InputStream inputStream = context.getAssets().open("event.json");
//                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuilder jsonString = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    jsonString.append(line);
//                }
//
//                JSONObject jsonObject = new JSONObject(jsonString.toString());
//                // Process the JSON data as needed
//
//                reader.close();
//                inputStream.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Error retrieving calendar data", e);
//                e.printStackTrace();
//            } catch (JSONException e) {
//                Log.e(TAG, "Error retrieving calendar data", e);
//                e.printStackTrace();
//            }
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject calendarData) {
//            if (calendarData == null) {
//                Toast.makeText(MainActivity.this, "Error retrieving calendar data", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            try {
//                // Parse the event data from the JSON object
//                String eventName = calendarData.getString("name");
//                String eventLocation = calendarData.getString("location");
//                String startDate = calendarData.getString("start_date");
//                String endDate = calendarData.getString("end_date");
//
//                // Add the event to the calendar
//                addEventToCalendar(eventName, eventLocation, startDate, endDate);
//            } catch (JSONException e) {
//                Log.e(TAG, "Error parsing calendar data", e);
//                Toast.makeText(MainActivity.this, "Error parsing calendar data", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void addEventToCalendar(String eventName, String eventLocation, String startDate, String endDate) {
//        // Get the calendar ID for the user's default calendar
//        String[] projection = new String[]{CalendarContract.Calendars._ID};
//        Cursor cursor = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI, projection,
//                CalendarContract.Calendars.ACCOUNT_NAME + " = ? AND " + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?",
//                new String[]{CalendarContract.Calendars.ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL}, null);
//
//        if (cursor == null || cursor.getCount() == 0) {
//            Log.e(TAG, "No local calendar found");
//            Toast.makeText(MainActivity.this, "No local calendar found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        cursor.moveToFirst();
//        long calendarId = cursor.getLong(0);
//
//        // Create a new event in the calendar
//        ContentValues values = new ContentValues();
//        values.put(CalendarContract.Events.TITLE, eventName);
//
//    }
//}