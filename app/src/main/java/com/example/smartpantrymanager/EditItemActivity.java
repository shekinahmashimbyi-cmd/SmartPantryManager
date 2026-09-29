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

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_item);
        EditText itemNameInput = findViewById(R.id.editItemNameInput);
        EditText quantityInput = findViewById(R.id.editQuantityInput);
        EditText categoryInput = findViewById(R.id.editCategoryInput);
        EditText expiryDateInput = findViewById(R.id.editExpiryDateInput);
        Button updateItemButton = findViewById(R.id.updateItemButton);
        Button backToPantryButton = findViewById(R.id.backToPantryButton);
        Button deleteItemButton = findViewById(R.id.deleteItemButton);

        int itemId = getIntent().getIntExtra("itemId", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.getPantryItemById(itemId);

        if (cursor.moveToFirst()) {

            String name = cursor.getString(1);
            int quantity = cursor.getInt(2);
            String category = cursor.getString(3);
            String expiryDate = cursor.getString(4);

            itemNameInput.setText(name);
            quantityInput.setText(String.valueOf(quantity));
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
            String category = categoryInput.getText().toString().trim();
            String expiryDate = expiryDateInput.getText().toString().trim();

            if (name.isEmpty() || quantityText.isEmpty()
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