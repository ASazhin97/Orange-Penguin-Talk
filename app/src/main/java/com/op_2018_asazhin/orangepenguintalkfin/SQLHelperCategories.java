package com.op_2018_asazhin.orangepenguintalkfin;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelperCategories extends SQLiteOpenHelper {
    private static String DB_NAME = "categories";
    private static int DB_VERSION = 1;


    public SQLHelperCategories(Context context){
     super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       updateMyDatabase(db, 0, DB_VERSION);


    }

    public void insertCategory(SQLiteDatabase db, String category, String name, int type, int image){
        ContentValues categoryValues = new ContentValues();

        categoryValues.put("CATEGORY", category);
        categoryValues.put("NAME", name);
        categoryValues.put("TYPE", type);
        categoryValues.put("IMAGE_RESOURCE_ID", image);
        db.insert("CATEGORIES", null, categoryValues);

    }

    public void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion <= 1){
            //CREATES THE CATEGORIES DATABASE
            db.execSQL("CREATE TABLE CATEGORIES ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " //id
                    + "CATEGORY TEXT, " //under what category to show
                    + "NAME TEXT, " //what is written when shown
                    + "TYPE INTEGER, " //0 means it is a category 1 means its a button
                    + "IMAGE_RESOURCE_ID INTEGER);"); //image shown

            //POPULATING THE DATABASE
        /*
        When adding new categories
        Repeat the lines given here. This will automatically
        add the category to the display
         */
        // method goes insertCategory(db, <name of category>, R.drawable.<name of file>);
            insertCategory(db, "default", "Emotions", 0, R.drawable.emotions);
            insertCategory(db, "default","Home", 0, R.drawable.home);
            insertCategory(db, "default","Animals", 0, R.drawable.animals);
            insertCategory(db, "Animals", "dog", 1, R.drawable.animals);

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }
}
