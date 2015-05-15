package app.com.brownjee.nigerpot.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by BROWNJEE on 17-Mar-15.
 */
public class FoodDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "customer.db";
    FoodDbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE CUSTOMER TABLE
        db.execSQL("CREATE TABLE " + CustomerDetails.TABLE_NAME
        + " (" + CustomerDetails.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + CustomerDetails.NAME + " TEXT,"
        + CustomerDetails.ADDRESS + " TEXT,"
        + CustomerDetails.PHONE + " TEXT,"
        + CustomerDetails.QUANTITY + " INTEGER,"
        + CustomerDetails.PICK_UP + " TEXT,"
        + CustomerDetails.DELIVER + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        Log.w("LOG_TAG", "Upgrading database from version "
        + oldVersion + " to " + newVersion + ", which will destroy all old data");
    // KILL PREVIOUS TABLES IF UPGRADED
         db.execSQL("DROP TABLE IF EXISTS " + CustomerDetails.TABLE_NAME);
        // CREATE NEW INSTANCE OF SCHEMA
        onCreate(db);

    }
}
