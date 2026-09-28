package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.TextView;
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

        String recipeName = getIntent().getStringExtra("recipeName");

        recipeNameText.setText(recipeName);

        if ("Creamy Pasta".equals(recipeName)) {

            ingredientsText.setText(
                    "• Pasta\n" +
                            "• Milk\n" +
                            "• Cheese\n" +
                            "• Butter\n" +
                            "• Salt and pepper"
            );

            instructionsText.setText(
                    "1. Cook the pasta according to the package instructions.\n\n" +
                            "2. Melt butter in a pan and add the milk.\n\n" +
                            "3. Add cheese and stir until the sauce is creamy.\n\n" +
                            "4. Add the cooked pasta and mix well.\n\n" +
                            "5. Season with salt and pepper and serve."
            );

        } else if ("Cheese Omelette".equals(recipeName)) {

            ingredientsText.setText(
                    "• Eggs\n" +
                            "• Cheese\n" +
                            "• Milk\n" +
                            "• Butter\n" +
                            "• Salt and pepper"
            );

            instructionsText.setText(
                    "1. Beat the eggs with a small amount of milk.\n\n" +
                            "2. Melt butter in a frying pan.\n\n" +
                            "3. Pour the egg mixture into the pan.\n\n" +
                            "4. Add grated cheese and cook until the eggs are set.\n\n" +
                            "5. Fold the omelette and serve."
            );

        } else if ("Fresh Veggie Sandwich".equals(recipeName)) {

            ingredientsText.setText(
                    "• Bread\n" +
                            "• Lettuce\n" +
                            "• Tomato\n" +
                            "• Cucumber\n" +
                            "• Cheese\n" +
                            "• Mayonnaise"
            );

            instructionsText.setText(
                    "1. Wash and slice the vegetables.\n\n" +
                            "2. Spread mayonnaise on the bread.\n\n" +
                            "3. Add lettuce, tomato, cucumber and cheese.\n\n" +
                            "4. Place the second slice of bread on top.\n\n" +
                            "5. Cut the sandwich and serve."
            );
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}