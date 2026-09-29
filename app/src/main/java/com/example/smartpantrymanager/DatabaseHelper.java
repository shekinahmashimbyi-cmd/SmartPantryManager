package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_PANTRY = "pantry_items";

    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_CATEGORY = "category";
    public static final String COL_EXPIRY = "expiry_date";

    // Recipe table
    public static final String TABLE_RECIPES = "recipes";

    public static final String COL_RECIPE_ID = "recipe_id";
    public static final String COL_RECIPE_NAME = "recipe_name";
    public static final String COL_INSTRUCTIONS = "instructions";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    public static final String COL_RECIPE_INGREDIENT_ID = "ingredient_id";
    public static final String COL_RECIPE_FOREIGN_ID = "recipe_id";
    public static final String COL_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_REQUIRED_QUANTITY = "required_quantity";
    public static final String COL_REQUIRED_UNIT = "required_unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_QUANTITY + " INTEGER, " +
                COL_UNIT + " TEXT, " +
                COL_CATEGORY + " TEXT, " +
                COL_EXPIRY + " TEXT)";

        db.execSQL(createTable);

        String createRecipesTable =
                "CREATE TABLE " + TABLE_RECIPES + " (" +
                        COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_RECIPE_NAME + " TEXT NOT NULL, " +
                        COL_INSTRUCTIONS + " TEXT NOT NULL)";

        db.execSQL(createRecipesTable);


        String createRecipeIngredientsTable =
                "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                        COL_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_RECIPE_FOREIGN_ID + " INTEGER, " +
                        COL_INGREDIENT_NAME + " TEXT NOT NULL, " +
                        COL_REQUIRED_QUANTITY + " REAL, " +
                        COL_REQUIRED_UNIT + " TEXT, " +
                        "FOREIGN KEY(" + COL_RECIPE_FOREIGN_ID + ") REFERENCES " +
                        TABLE_RECIPES + "(" + COL_RECIPE_ID + "))";

        db.execSQL(createRecipeIngredientsTable);
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.execSQL(
                    "ALTER TABLE " + TABLE_PANTRY +
                            " ADD COLUMN " + COL_UNIT +
                            " TEXT DEFAULT 'Items'"
            );
        }
        if (oldVersion < 3) {

            String createRecipesTable =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                            COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_RECIPE_NAME + " TEXT NOT NULL, " +
                            COL_INSTRUCTIONS + " TEXT NOT NULL)";

            db.execSQL(createRecipesTable);


            String createRecipeIngredientsTable =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_INGREDIENTS + " (" +
                            COL_RECIPE_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            COL_RECIPE_FOREIGN_ID + " INTEGER, " +
                            COL_INGREDIENT_NAME + " TEXT NOT NULL, " +
                            COL_REQUIRED_QUANTITY + " REAL, " +
                            COL_REQUIRED_UNIT + " TEXT, " +
                            "FOREIGN KEY(" + COL_RECIPE_FOREIGN_ID + ") REFERENCES " +
                            TABLE_RECIPES + "(" + COL_RECIPE_ID + "))";

            db.execSQL(createRecipeIngredientsTable);
            seedRecipes(db);
        }
    }

    public boolean addPantryItem(String name, int quantity, String unit,
                                 String category, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_CATEGORY, category);
        values.put(COL_EXPIRY, expiryDate);

        long result = db.insert(TABLE_PANTRY, null, values);

        return result != -1;
    }

public Cursor getAllPantryItems() {

    SQLiteDatabase db = this.getReadableDatabase();

    return db.rawQuery(
            "SELECT * FROM " + TABLE_PANTRY,
            null
    );
}
    public Cursor getPantryItemById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_PANTRY + " WHERE " + COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
    public boolean updatePantryItem(int id, String name, int quantity,
                                    String unit, String category, String expiryDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_CATEGORY, category);
        values.put(COL_EXPIRY, expiryDate);

        int rowsAffected = db.update(
                TABLE_PANTRY,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsAffected > 0;
    }
    public boolean deletePantryItem(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted = db.delete(
                TABLE_PANTRY,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return rowsDeleted > 0;
    }
    private long addRecipe(SQLiteDatabase db, String name, String instructions) {

        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, name);
        values.put(COL_INSTRUCTIONS, instructions);

        return db.insert(TABLE_RECIPES, null, values);
    }


    private void addRecipeIngredient(SQLiteDatabase db, long recipeId,
                                     String ingredientName, double quantity,
                                     String unit) {

        ContentValues values = new ContentValues();

        values.put(COL_RECIPE_FOREIGN_ID, recipeId);
        values.put(COL_INGREDIENT_NAME, ingredientName);
        values.put(COL_REQUIRED_QUANTITY, quantity);
        values.put(COL_REQUIRED_UNIT, unit);

        db.insert(TABLE_RECIPE_INGREDIENTS, null, values);
    }
    private void seedRecipes(SQLiteDatabase db) {

        long recipeId;

        // 1. Cheese Omelette
        recipeId = addRecipe(
                db,
                "Cheese Omelette",
                "Beat the eggs in a bowl. Heat a pan and cook the eggs until almost set. " +
                        "Add the cheese, fold the omelette and cook until the cheese melts."
        );

        addRecipeIngredient(db, recipeId, "Egg", 2, "Items");
        addRecipeIngredient(db, recipeId, "Cheese", 50, "g");


        // 2. Banana Oatmeal
        recipeId = addRecipe(
                db,
                "Banana Oatmeal",
                "Cook the oats with milk until soft and creamy. Slice the banana and add it on top before serving."
        );

        addRecipeIngredient(db, recipeId, "Oats", 1, "cups");
        addRecipeIngredient(db, recipeId, "Milk", 250, "ml");
        addRecipeIngredient(db, recipeId, "Banana", 1, "Items");


        // 3. Tomato Pasta
        recipeId = addRecipe(
                db,
                "Tomato Pasta",
                "Cook the pasta according to the packet instructions. " +
                        "Cook the tomato and onion in a pan, then combine with the pasta."
        );

        addRecipeIngredient(db, recipeId, "Pasta", 200, "g");
        addRecipeIngredient(db, recipeId, "Tomato", 2, "Items");
        addRecipeIngredient(db, recipeId, "Onion", 1, "Items");


        // 4. Scrambled Eggs on Toast
        recipeId = addRecipe(
                db,
                "Scrambled Eggs on Toast",
                "Beat the eggs and cook them gently in a pan while stirring. Toast the bread and serve the eggs on top."
        );

        addRecipeIngredient(db, recipeId, "Egg", 2, "Items");
        addRecipeIngredient(db, recipeId, "Bread", 2, "Items");


        // 5. Grilled Cheese Sandwich
        recipeId = addRecipe(
                db,
                "Grilled Cheese Sandwich",
                "Place the cheese between two slices of bread. Grill in a pan until the bread is golden and the cheese has melted."
        );

        addRecipeIngredient(db, recipeId, "Bread", 2, "Items");
        addRecipeIngredient(db, recipeId, "Cheese", 50, "g");


        // 6. Banana Smoothie
        recipeId = addRecipe(
                db,
                "Banana Smoothie",
                "Add the banana and milk to a blender. Blend until smooth and serve immediately."
        );

        addRecipeIngredient(db, recipeId, "Banana", 1, "Items");
        addRecipeIngredient(db, recipeId, "Milk", 250, "ml");


        // 7. Tomato and Cheese Sandwich
        recipeId = addRecipe(
                db,
                "Tomato and Cheese Sandwich",
                "Slice the tomato and cheese. Place them between the bread slices and serve."
        );

        addRecipeIngredient(db, recipeId, "Bread", 2, "Items");
        addRecipeIngredient(db, recipeId, "Tomato", 1, "Items");
        addRecipeIngredient(db, recipeId, "Cheese", 40, "g");


        // 8. Egg Fried Rice
        recipeId = addRecipe(
                db,
                "Egg Fried Rice",
                "Cook the egg in a pan and break it into small pieces. Add the cooked rice and stir-fry until heated through."
        );

        addRecipeIngredient(db, recipeId, "Rice", 1, "cups");
        addRecipeIngredient(db, recipeId, "Egg", 1, "Items");


        // 9. Creamy Pasta
        recipeId = addRecipe(
                db,
                "Creamy Pasta",
                "Cook the pasta until tender. Heat the milk and cheese in a pan until creamy, then stir in the cooked pasta."
        );

        addRecipeIngredient(db, recipeId, "Pasta", 200, "g");
        addRecipeIngredient(db, recipeId, "Milk", 200, "ml");
        addRecipeIngredient(db, recipeId, "Cheese", 50, "g");


        // 10. Avocado Toast
        recipeId = addRecipe(
                db,
                "Avocado Toast",
                "Toast the bread. Mash the avocado and spread it over the toast before serving."
        );

        addRecipeIngredient(db, recipeId, "Bread", 2, "Items");
        addRecipeIngredient(db, recipeId, "Avocado", 1, "Items");


        // 11. Cheese and Tomato Omelette
        recipeId = addRecipe(
                db,
                "Cheese and Tomato Omelette",
                "Beat the eggs and pour them into a heated pan. Add the tomato and cheese, then fold and cook until set."
        );

        addRecipeIngredient(db, recipeId, "Egg", 2, "Items");
        addRecipeIngredient(db, recipeId, "Tomato", 1, "Items");
        addRecipeIngredient(db, recipeId, "Cheese", 40, "g");


        // 12. Simple Pancakes
        recipeId = addRecipe(
                db,
                "Simple Pancakes",
                "Mix the flour, milk and egg into a smooth batter. Pour small amounts into a heated pan and cook on both sides."
        );

        addRecipeIngredient(db, recipeId, "Flour", 1, "cups");
        addRecipeIngredient(db, recipeId, "Milk", 250, "ml");
        addRecipeIngredient(db, recipeId, "Egg", 1, "Items");


        // 13. Vegetable Rice
        recipeId = addRecipe(
                db,
                "Vegetable Rice",
                "Cook the rice until tender. Cook the carrots and peas separately, then mix the vegetables into the rice."
        );

        addRecipeIngredient(db, recipeId, "Rice", 1, "cups");
        addRecipeIngredient(db, recipeId, "Carrot", 1, "Items");
        addRecipeIngredient(db, recipeId, "Peas", 100, "g");


        // 14. Egg Sandwich
        recipeId = addRecipe(
                db,
                "Egg Sandwich",
                "Cook the eggs and place them between slices of bread. Serve warm or cold."
        );

        addRecipeIngredient(db, recipeId, "Bread", 2, "Items");
        addRecipeIngredient(db, recipeId, "Egg", 2, "Items");


        // 15. Tomato Rice
        recipeId = addRecipe(
                db,
                "Tomato Rice",
                "Cook the rice until tender. Cook the tomato and onion in a pan, then mix them into the rice."
        );

        addRecipeIngredient(db, recipeId, "Rice", 1, "cups");
        addRecipeIngredient(db, recipeId, "Tomato", 2, "Items");
        addRecipeIngredient(db, recipeId, "Onion", 1, "Items");


        // 16. Cheese Pasta
        recipeId = addRecipe(
                db,
                "Cheese Pasta",
                "Cook the pasta according to the packet instructions. Drain it, add the cheese and stir until melted."
        );

        addRecipeIngredient(db, recipeId, "Pasta", 200, "g");
        addRecipeIngredient(db, recipeId, "Cheese", 60, "g");


        // 17. Banana Pancakes
        recipeId = addRecipe(
                db,
                "Banana Pancakes",
                "Mash the banana and combine it with the egg and flour. Cook spoonfuls of the mixture in a heated pan on both sides."
        );

        addRecipeIngredient(db, recipeId, "Banana", 1, "Items");
        addRecipeIngredient(db, recipeId, "Egg", 1, "Items");
        addRecipeIngredient(db, recipeId, "Flour", 100, "g");


        // 18. Avocado Egg Toast
        recipeId = addRecipe(
                db,
                "Avocado Egg Toast",
                "Toast the bread and spread with mashed avocado. Cook the egg and place it on top before serving."
        );

        addRecipeIngredient(db, recipeId, "Bread", 2, "Items");
        addRecipeIngredient(db, recipeId, "Avocado", 1, "Items");
        addRecipeIngredient(db, recipeId, "Egg", 1, "Items");


        // 19. Carrot and Pea Rice
        recipeId = addRecipe(
                db,
                "Carrot and Pea Rice",
                "Cook the rice until tender. Cook the carrot and peas, then combine everything and serve."
        );

        addRecipeIngredient(db, recipeId, "Rice", 1, "cups");
        addRecipeIngredient(db, recipeId, "Carrot", 1, "Items");
        addRecipeIngredient(db, recipeId, "Peas", 100, "g");


        // 20. Tomato Egg Scramble
        recipeId = addRecipe(
                db,
                "Tomato Egg Scramble",
                "Cook the chopped tomato briefly in a pan. Add the beaten eggs and stir gently until cooked."
        );

        addRecipeIngredient(db, recipeId, "Tomato", 1, "Items");
        addRecipeIngredient(db, recipeId, "Egg", 2, "Items");
    }
    public Cursor getAllRecipes() {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_RECIPES +
                        " ORDER BY " + COL_RECIPE_NAME + " ASC",
                null
        );
    }
    public Cursor getRecipeById(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_RECIPES +
                        " WHERE " + COL_RECIPE_ID + " = ?",
                new String[]{String.valueOf(recipeId)}
        );
    }
    public Cursor getRecipeIngredients(int recipeId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_RECIPE_INGREDIENTS +
                        " WHERE " + COL_RECIPE_FOREIGN_ID + " = ?",
                new String[]{String.valueOf(recipeId)}
        );
    }
    public boolean pantryHasIngredient(String ingredientName,
                                       double requiredQuantity,
                                       String requiredUnit) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_PANTRY +
                        " WHERE LOWER(" + COL_NAME + ") = LOWER(?)",
                new String[]{ingredientName}
        );

        boolean enoughIngredient = false;

        if (cursor.moveToFirst()) {

            int quantityColumn = cursor.getColumnIndexOrThrow(COL_QUANTITY);
            int unitColumn = cursor.getColumnIndexOrThrow(COL_UNIT);

            do {

                double pantryQuantity = cursor.getDouble(quantityColumn);
                String pantryUnit = cursor.getString(unitColumn);

                double convertedPantryQuantity =
                        convertQuantity(pantryQuantity, pantryUnit, requiredUnit);

                if (convertedPantryQuantity >= requiredQuantity) {
                    enoughIngredient = true;
                    break;
                }

            } while (cursor.moveToNext());
        }

        cursor.close();

        return enoughIngredient;
    }

    private double convertQuantity(double quantity,
                                   String fromUnit,
                                   String toUnit) {

        if (fromUnit.equalsIgnoreCase(toUnit)) {
            return quantity;
        }

        // kg to g
        if (fromUnit.equalsIgnoreCase("kg")
                && toUnit.equalsIgnoreCase("g")) {

            return quantity * 1000;
        }

        // g to kg
        if (fromUnit.equalsIgnoreCase("g")
                && toUnit.equalsIgnoreCase("kg")) {

            return quantity / 1000;
        }

        // L to ml
        if (fromUnit.equalsIgnoreCase("L")
                && toUnit.equalsIgnoreCase("ml")) {

            return quantity * 1000;
        }

        // ml to L
        if (fromUnit.equalsIgnoreCase("ml")
                && toUnit.equalsIgnoreCase("L")) {

            return quantity / 1000;
        }

        // Units are incompatible
        return -1;
    }

    public boolean canMakeRecipe(int recipeId) {

        Cursor cursor = getRecipeIngredients(recipeId);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }

        int nameColumn = cursor.getColumnIndexOrThrow(
                COL_INGREDIENT_NAME
        );

        int quantityColumn = cursor.getColumnIndexOrThrow(
                COL_REQUIRED_QUANTITY
        );

        int unitColumn = cursor.getColumnIndexOrThrow(
                COL_REQUIRED_UNIT
        );

        do {

            String ingredientName =
                    cursor.getString(nameColumn);

            double requiredQuantity =
                    cursor.getDouble(quantityColumn);

            String requiredUnit =
                    cursor.getString(unitColumn);

            boolean ingredientAvailable =
                    pantryHasIngredient(
                            ingredientName,
                            requiredQuantity,
                            requiredUnit
                    );

            if (!ingredientAvailable) {
                cursor.close();
                return false;
            }

        } while (cursor.moveToNext());

        cursor.close();

        return true;
    }
}