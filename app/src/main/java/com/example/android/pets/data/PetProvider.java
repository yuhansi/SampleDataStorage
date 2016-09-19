package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by hanssi on 9/18/16.
 */

/**
 * ContentProvider for Pets app
 */
public class PetProvider extends ContentProvider {

    /* Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    /* URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /* URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code
     * The input passed into the constructor represents the code to return for the root URI
     * It is common to use NO_MATCH as the input for this case
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer, this runs when the first time anything is called from this class
    static {

        // In this case, the URI is used to provide access to multiple rows of the pets table
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);

        // In this case, the URI is used to provide access to one single row of the pets table
        // Here, "#" can be substituted by an integer
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "/#", PET_ID);
    }

    /* Database helper object */
    private PetDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object
     */
    @Override
    public boolean onCreate() {

        // Create and initialize a PetDbHelper object to gain access to the pets database
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    /**
     * Perform query on the given URI using the given projection, selection, selection arguments and sort order
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // Create a cursor to hold the result of the query
        Cursor cursor;

        // Check if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch(match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given projection, selection,
                // selection arguments and sort order
                // In this case, the cursor could contain multiple rows of the pets table
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI
                // In this case, the selection will be "_id=?" and the selection
                // argument will be a String array containing the actual ID
                // Since there is one "?" in the selection, 1 String is needed in the
                // selection arguments' String array
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                // This will perform a query on the pets table with a specific _id and
                // return a cursor containing that row of the table
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;

    }

    /**
     * Insert new data into the provider with the given contentValue
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch(match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Method to insert a pet into the database with the given content values
     * Return the new content URI for that specific row in the database
     */
    private Uri insertPet(Uri uri, ContentValues values) {

        // Check that the name of the pet being inserted is not null
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        if(name == null) {
            throw new IllegalArgumentException("New pet requires a name");
        }

        // Check that the gender of the pet being inserted is valid
        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if(gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("New pet requires a valid gender");
        }

        // If the weight is provided, check if it is greater than or equal to 0
        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if(weight != null && weight < 0) {
            throw new IllegalArgumentException("New pet requires a valid weight");
        }

        // No need to check validity of the breed since any value is valid

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(PetEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion is failed, Log an error and return null
        if(id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Return the new content URI with the ID of the newly inserted row appended at the end
        return ContentUris.withAppendedId(uri, id);

    }

    /**
     * Update the data at the given selection and selection arguments with the new contentValue
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);

        switch(match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI
                // In this case, the selection will be "_id=?" and the selection
                // argument will be a String array containing the actual ID
                // Since there is one "?" in the selection, 1 String is needed in the
                // selection arguments' String array
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    /**
     * Update pets in the databse with the given content values
     * Apply the changes to the rows specified in the selections and selection arguments
     * which could be 0 or 1 or more pets
     * Return the number of rows that were successfully updated
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // If the COLUMN_PET_NAME key is present
        // check that the name value is not null
        if(values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if(name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the COLUMN_PET_GENDER key is present
        // check that the gender value is valid
        if(values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if(gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires a valid gender");
            }
        }

        // If the COLUMN_PET_WEIGHT key is present
        // check that the weight value is valid
        if(values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if(weight != null && weight < 0 ) {
                throw new IllegalArgumentException("Pet requires a valid weight");
            }
        }

        // No need to check validity of the breed since any value is valid

        // If there are no values to update, then do not try to update the database
        if(values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Return the number of database rows affected by the update statement
        return database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Delete the data at the given selection and selection arguments
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch(match) {
            case PETS:
                // Delete all rows that match the selection and selection arguments
                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            case PET_ID:
                // Delete a single row gievn by the ID in the URI
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }

    /**
     * Return the MIME type of data for the content URI
     */
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch(match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

}
