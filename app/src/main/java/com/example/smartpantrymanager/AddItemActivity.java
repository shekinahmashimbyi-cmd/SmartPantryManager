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
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        Spinner unitSpinner = findViewById(R.id.unitSpinner);

        EditText itemNameInput = findViewById(R.id.itemNameInput);
        EditText quantityInput = findViewById(R.id.quantityInput);
        Button saveItemButton = findViewById(R.id.saveItemButton);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

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
        String[] units = {
                "Select Unit",
                "Items",
                "g",
                "kg",
                "ml",
                "L",
                "cups",
                "tbsp",
                "tsp"
        };

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        unitSpinner.setAdapter(unitAdapter);
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
            saveItemButton.setOnClickListener(v -> {

                String name = itemNameInput.getText().toString().trim();
                String quantityText = quantityInput.getText().toString().trim();
                String unit = unitSpinner.getSelectedItem().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String expiryDate = expiryDateInput.getText().toString().trim();

                if (name.isEmpty() ||
                        quantityText.isEmpty() ||
                        unit.equals("Select Unit") ||
                        category.equals("Select Category") ||
                        expiryDate.isEmpty()) {


                    Toast.makeText(
                            AddItemActivity.this,
                            "Please complete all fields",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                int quantity;

                try {
                    quantity = Integer.parseInt(quantityText);
                } catch (NumberFormatException e) {

                    Toast.makeText(
                            AddItemActivity.this,
                            "Please enter a valid quantity",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                boolean inserted = databaseHelper.addPantryItem(
                        name,
                        quantity,
                        unit,
                        category,
                        expiryDate
                );

                if (inserted) {

                    Toast.makeText(
                            AddItemActivity.this,
                            "Item saved successfully!",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(
                            AddItemActivity.this,
                            MyPantryActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(
                            AddItemActivity.this,
                            "Unable to save item",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
