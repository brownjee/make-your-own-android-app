package app.com.brownjee.nigerpot.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;



/**
 * Created by BROWNJEE on 19-Mar-15.
 */
public class FoodContentProvider extends ContentProvider {

    public static final String AUTHORITY = "app.com.brownjee.nigerpot";

    private FoodDbHelper dbHelper;
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> projectionMap;

    // URI MATCH OF A GENERAL CUSTOMER QUERY
    private static final int CUSTOMERDETAIL = 1;

    // URI MATCH OF A SPECIFIC CUSTOMER QUERY
    private static final int CDID = 2;

    @Override
    public boolean onCreate() {
        // HELPER DATABASE IS INITIALIZED
        dbHelper = new FoodDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(CustomerDetails.TABLE_NAME);
        switch (sUriMatcher.match(uri)){
            case CUSTOMERDETAIL:
                qb.setProjectionMap(projectionMap);
                break;
            case CDID:
                String cd_id =
                        uri.getPathSegments().get(CustomerDetails.CDID_PATH_POSITION);
                qb.setProjectionMap(projectionMap);
                // FOR QUERYING BY SPECIFIC CD_ID
                qb.appendWhere(CustomerDetails.ID + "=" + cd_id);
                break;
            default:
                throw new IllegalArgumentException ("Unknown URI" + uri);

        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // REGISTERS NOTIFICATION LISTENER WITH GIVEN CURSOR
        // CURSOR KNOWS WHEN UNDERLYING DATA HAS CHANGED
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case CUSTOMERDETAIL:
                return CustomerDetails.CONTENT_TYPE;
            case CDID:
                return CustomerDetails.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // ONLY GENERAL CUSTOMER URI IS ALLOWED FOR INSERTS
        // DOESN'T MAKE SENSE TO SPECIFY A SINGLE CITIZEN
        if (sUriMatcher.match(uri) != CUSTOMERDETAIL){
            throw new IllegalArgumentException("Unknown URI" + uri);
        }
        // PACKAGE DESIRED VALUES AS A CONTENTVALUE OBJECT
        ContentValues values;
        if (initialValues != null){
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(CustomerDetails.TABLE_NAME, CustomerDetails.NAME, values);
        if (rowId > 0){
            Uri customerUri = ContentUris.withAppendedId(CustomerDetails.CONTENT_URI, rowId);
            // NOTIFY CONTEXT OF THE CHANGE
            getContext().getContentResolver().notifyChange(customerUri, null);
            return customerUri;
        }
        throw new android.database.SQLException( "Failed to insert row " + uri);

    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)){
            case CUSTOMERDETAIL:
                //PERFORM REGULAR DELETE
                count = db.delete(CustomerDetails.TABLE_NAME, where,whereArgs);
                break;
            case CDID:
                //FROM INCOMING URI GET CDID
                String cdid = uri.getPathSegments().get(CustomerDetails.CDID_PATH_POSITION);
                //USER WANTS TO DELETE A SPECIFIC CUSTOMER
                String finalWhere = CustomerDetails.ID+"="+cdid;
                //IF USER SPECIFIES WHERE FILTER THEN APPEND
                if (where !=null){
                    finalWhere = finalWhere + "AND" + where;
                }
                count = db.delete(CustomerDetails.TABLE_NAME, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);


        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)){
            case CUSTOMERDETAIL:
                // GENERAL UPDATE ON ALL CUSTOMERS
                count = db.update(CustomerDetails.TABLE_NAME, values, where, whereArgs);
                break;
            case CDID:
                //FROM INCOMING URI GET CDID
                String cdid = uri.getPathSegments().get(CustomerDetails.CDID_PATH_POSITION);
                //THE USER WANTS TO UPDATE A SPECIFIC CUSTOMER
                String finalWhere = CustomerDetails.ID+"="+cdid;
                if (where != null){
                    finalWhere = finalWhere + "AND" + where;
                }
                //PERFORM THE UPDATE ON THE SPECIFIC CUSTOMER
                count = db.update(CustomerDetails.TABLE_NAME, values, finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException
                    ("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    // INSTANTIATE AND SET STATIC VARIABLES
    static {
      sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
      sUriMatcher.addURI(AUTHORITY, "customer", CUSTOMERDETAIL);
      sUriMatcher.addURI(AUTHORITY, "customer/#", CDID);
      // PROJECTION MAP USED FOR ROW ALIAS
      projectionMap = new HashMap<String, String>();
      projectionMap.put(CustomerDetails.ID, CustomerDetails.ID);
      projectionMap.put(CustomerDetails.NAME, CustomerDetails.NAME);
      projectionMap.put(CustomerDetails.ADDRESS, CustomerDetails.ADDRESS);
      projectionMap.put(CustomerDetails.PHONE, CustomerDetails.PHONE);
      projectionMap.put(CustomerDetails.QUANTITY, CustomerDetails.QUANTITY);
      projectionMap.put(CustomerDetails.PICK_UP, CustomerDetails.PICK_UP);
      projectionMap.put(CustomerDetails.DELIVER, CustomerDetails.DELIVER);

    }

}
