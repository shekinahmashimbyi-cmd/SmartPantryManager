package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
import android.database.Cursor;
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        findViewById(R.id.backToRecipesButton).setOnClickListener(v -> {
            finish();
        });
        TextView recipeNameText = findViewById(R.id.recipeNameText);
        TextView ingredientsText = findViewById(R.id.ingredientsText);
        TextView instructionsText = findViewById(R.id.instructionsText);

        int recipeId = getIntent().getIntExtra("recipeId", -1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor recipeCursor = databaseHelper.getRecipeById(recipeId);

        if (recipeCursor.moveToFirst()) {

            String recipeName = recipeCursor.getString(
                    recipeCursor.getColumnIndexOrThrow(
                            DatabaseHelper.COL_RECIPE_NAME
                    )
            );

            String instructions = recipeCursor.getString(
                    recipeCursor.getColumnIndexOrThrow(
                            DatabaseHelper.COL_INSTRUCTIONS
                    )
            );

            recipeNameText.setText(recipeName);
            instructionsText.setText(instructions);
        }

        recipeCursor.close();

        Cursor ingredientCursor =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredients = new StringBuilder();

        if (ingredientCursor.moveToFirst()) {

            int nameColumn = ingredientCursor.getColumnIndexOrThrow(
                    DatabaseHelper.COL_INGREDIENT_NAME
            );

            int quantityColumn = ingredientCursor.getColumnIndexOrThrow(
                    DatabaseHelper.COL_REQUIRED_QUANTITY
            );

            int unitColumn = ingredientCursor.getColumnIndexOrThrow(
                    DatabaseHelper.COL_REQUIRED_UNIT
            );

            do {

                String ingredientName =
                        ingredientCursor.getString(nameColumn);

                double quantity =
                        ingredientCursor.getDouble(quantityColumn);

                String unit =
                        ingredientCursor.getString(unitColumn);

                ingredients
                        .append("• ")
                        .append(ingredientName)
                        .append(" - ");

                if (quantity == Math.floor(quantity)) {
                    ingredients.append((int) quantity);
                } else {
                    ingredients.append(quantity);
                }

                ingredients
                        .append(" ")
                        .append(unit)
                        .append("\n");

            } while (ingredientCursor.moveToNext());
        }

        ingredientCursor.close();

        ingredientsText.setText(ingredients.toString());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}