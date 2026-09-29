package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
public class SuggestedRecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggested_recipes);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        ListView recipeListView = findViewById(R.id.recipeListView);

        ArrayList<String> recipeNames = new ArrayList<>();
        ArrayList<Integer> recipeIds = new ArrayList<>();

        Cursor cursor = databaseHelper.getAllRecipes();

        if (cursor.moveToFirst()) {

            int idColumn = cursor.getColumnIndexOrThrow(
                    DatabaseHelper.COL_RECIPE_ID
            );

            int nameColumn = cursor.getColumnIndexOrThrow(
                    DatabaseHelper.COL_RECIPE_NAME
            );

            do {

                recipeIds.add(cursor.getInt(idColumn));
                recipeNames.add(cursor.getString(nameColumn));

            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                recipeNames
        );

        recipeListView.setAdapter(adapter);

        recipeListView.setOnItemClickListener(
                (parent, view, position, id) -> {

                    int recipeId = recipeIds.get(position);

                    Intent intent = new Intent(
                            SuggestedRecipesActivity.this,
                            RecipeDetailActivity.class
                    );

                    intent.putExtra("recipeId", recipeId);

                    startActivity(intent);
                }
        );

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}