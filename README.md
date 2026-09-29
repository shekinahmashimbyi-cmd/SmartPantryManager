# Smart Pantry Manager

## About the App

Smart Pantry Manager is an Android application that I developed to make it easier for users to keep track of the food items they have available in their pantry.

The app allows a user to add, view, edit and delete pantry items. Each item can include its name, quantity, unit of measurement, category and expiry date.

The app also suggests recipes based on the ingredients that the user currently has available. A recipe will only be suggested if all the ingredients needed for that recipe are available in the pantry in the required quantities.

## Main Features

The Smart Pantry Manager includes the following features:

- Add new pantry items
- View saved pantry items
- Edit existing pantry items
- Delete pantry items
- Save the quantity, unit, category and expiry date of an item
- Store pantry information even after the app has been closed
- View a collection of 20 stored recipes
- View the ingredients and preparation steps for each recipe
- Suggest recipes based on available pantry ingredients
- Check that enough of each ingredient is available before suggesting a recipe
- Convert compatible units such as kg to g and L to ml when matching ingredients
- Display a message when there are no recipes that can currently be made
- View the Settings and Profile screen

## Database

I used SQLite as the database for the application.

I chose SQLite because it works well with Android applications and allows the app to store information locally on the device. This means that pantry items and recipe information can still be accessed without requiring an internet connection.

The database is used to store pantry items, recipes and the ingredients required for each recipe.

Recipes and their ingredients are stored separately and are linked using the recipe ID. This makes it possible for one recipe to have multiple required ingredients.

## Recipe Matching

One of the main functions of the app is suggesting recipes based on what is currently available in the pantry.

For a recipe to appear under Suggested Recipes, every ingredient required for the recipe must be available in the pantry and the available quantity must be enough.

For example, if a recipe requires 100 g of peas but the pantry only contains 50 g, that recipe will not be suggested.

The app can also compare compatible measurement units such as:

- kg and g
- L and ml

If the units cannot be safely converted, they are treated as incompatible. For example, the app does not automatically convert kg to cups because these are different types of measurements and the conversion can depend on the ingredient.

## Technologies Used

The application was developed using:

- Java
- Android Studio
- XML
- SQLite
- Git
- GitHub

## How to Run the App

1. Clone or download the project from GitHub.
2. Open the project in Android Studio.
3. Allow the project and Gradle files to finish syncing.
4. Start an Android emulator or connect an Android device.
5. Run the application from Android Studio.

## App Screens

The application includes the following screens:

- Home
- My Pantry
- Add Item
- Edit Item
- Suggested Recipes
- Recipe Details
- Settings & Profile

## Project Purpose

This project was developed as part of my Mobile App Development 700 assignment.

Through this project I worked with Android activities, XML layouts, SQLite databases, CRUD operations, Intents, adapters and recipe matching logic.