package com.example.android.pets.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * Created by hanssi on 9/17/16.
 */

/**
 * API Contract for the Pets app
 */
public final class PetContract {

    // Empty constructor
    // In case the contract class is accidentally initialized
    private PetContract() {

    }

    /**
     * The CONTENT_AUTHORITY is a name for the entire content provider
     * In this case, the package name for this app is a convenient
     * string to use for the content provider since it is guaranteed
     * to be unique
     */
    public static final String CONTENT_AUTHORITY = ".com.example.android.pets";

    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which app will use
     * to contact the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Possible path to store pet data (appended to base content URI for possible URI's)
     */
    public static final String PATH_PETS = "pets";

    /**
     * Inner class that defines the constant values for the pets
     * database table
     * Each entry in the table represents a single pet
     */
    public static final class PetEntry implements BaseColumns {

        /* The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        /**
         * The MIME type of the CONTENT_URI for a list of pets
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the CONTENT_URI for a single pet
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /* Name of the database table for pets */
        public final static String TABLE_NAME = "pets";

        /**
         * Unique ID number for the pet (only used in database table)
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet
         *
         * Type: TEXT
         */
        public final static String COLUMN_PET_NAME = "name";

        /**
         * Breed of the pet
         *
         * Type: TEXT
         */
        public final static String COLUMN_PET_BREED = "breed";

        /**
         * Gender of the pet
         *
         * Type: INTEGER
         *
         * The only possible values are GENDER_UNKNOWN,
         * GENDER_MALE or GENDER_FEMALE
         */
        public final static String COLUMN_PET_GENDER = "gender";

        /**
         * Weight of the pet
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PET_WEIGHT = "weight";

        /**
         * Possible values for the gender of the pet
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        /**
         * Returns whether the given gender is GENDER_UNKNOWN, GENDER_MALE or GENDER_FEMALE
         */
        public static boolean isValidGender(int gender) {
            if(gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

    }
}
