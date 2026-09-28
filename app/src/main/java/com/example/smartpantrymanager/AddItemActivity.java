package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import android.app.DatePickerDialog;
import android.widget.EditText;

import java.util.Calendar;
public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);

        String[] categories = {
                "Select Category",
                "Dairy",
                "Fruit & Vegetables",
                "Meat",
                "Grains",
                "Snacks",
                "Drinks",
                "Canned Goods",
                "Other"
        };

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        categorySpinner.setAdapter(categoryAdapter);
        EditText expiryDateInput = findViewById(R.id.expiryDateInput);

        expiryDateInput.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddItemActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String selectedDate =
                                selectedDay + "/" +
                                        (selectedMonth + 1) + "/" +
                                        selectedYear;

                        expiryDateInput.setText(selectedDate);
                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.show();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}