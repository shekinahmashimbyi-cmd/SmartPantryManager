package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyPantryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_pantry);
        ListView pantryListView = findViewById(R.id.pantryListView);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ArrayList<String> pantryItems = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllPantryItems();
        if (cursor.getCount() == 0) {

            Toast.makeText(
                    MyPantryActivity.this,
                    "Your pantry is empty",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            while (cursor.moveToNext()) {

                String name = cursor.getString(1);
                int quantity = cursor.getInt(2);
                String category = cursor.getString(3);
                String expiryDate = cursor.getString(4);

                String itemDetails =
                        name +
                                "\nQuantity: " + quantity +
                                "\nCategory: " + category +
                                "\nExpiry Date: " + expiryDate;

                pantryItems.add(itemDetails);
            }
        }

        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                pantryItems
        );

        pantryListView.setAdapter(adapter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}