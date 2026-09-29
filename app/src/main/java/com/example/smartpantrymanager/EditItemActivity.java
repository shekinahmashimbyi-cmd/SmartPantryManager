package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_item);
        EditText itemNameInput = findViewById(R.id.editItemNameInput);
        EditText quantityInput = findViewById(R.id.editQuantityInput);
        Spinner unitSpinner = findViewById(R.id.editUnitSpinner);
        EditText categoryInput = findViewById(R.id.editCategoryInput);
        EditText expiryDateInput = findViewById(R.id.editExpiryDateInput);
        Button updateItemButton = findViewById(R.id.updateItemButton);
        Button backToPantryButton = findViewById(R.id.backToPantryButton);
        Button deleteItemButton = findViewById(R.id.deleteItemButton);

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

        int itemId = getIntent().getIntExtra("itemId", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.getPantryItemById(itemId);

        if (cursor.moveToFirst()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME)
            );

            int quantity = cursor.getInt(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_QUANTITY)
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_UNIT)
            );

            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY)
            );

            String expiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EXPIRY)
            );

            itemNameInput.setText(name);
            quantityInput.setText(String.valueOf(quantity));
            int unitPosition = unitAdapter.getPosition(unit);

            if (unitPosition >= 0) {
                unitSpinner.setSelection(unitPosition);
            }
            categoryInput.setText(category);
            expiryDateInput.setText(expiryDate);
        }

        cursor.close();
        backToPantryButton.setOnClickListener(v -> {
            finish();
        });
        deleteItemButton.setOnClickListener(v -> {

            new AlertDialog.Builder(EditItemActivity.this)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this pantry item?")
                    .setPositiveButton("Delete", (dialog, which) -> {

                        boolean deleted = databaseHelper.deletePantryItem(itemId);

                        if (deleted) {

                            Toast.makeText(
                                    EditItemActivity.this,
                                    "Item deleted successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            finish();

                        } else {

                            Toast.makeText(
                                    EditItemActivity.this,
                                    "Failed to delete item",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        updateItemButton.setOnClickListener(v -> {

            String name = itemNameInput.getText().toString().trim();
            String quantityText = quantityInput.getText().toString().trim();
            String unit = unitSpinner.getSelectedItem().toString();
            String category = categoryInput.getText().toString().trim();
            String expiryDate = expiryDateInput.getText().toString().trim();

            if (name.isEmpty() || quantityText.isEmpty()
                    || unit.equals("Select Unit")
                    || category.isEmpty() || expiryDate.isEmpty()) {

                Toast.makeText(
                        EditItemActivity.this,
                        "Please complete all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int quantity = Integer.parseInt(quantityText);

            boolean updated = databaseHelper.updatePantryItem(
                    itemId,
                    name,
                    quantity,
                    unit,
                    category,
                    expiryDate
            );

            if (updated) {

                Toast.makeText(
                        EditItemActivity.this,
                        "Item updated successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();

            } else {

                Toast.makeText(
                        EditItemActivity.this,
                        "Failed to update item",
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