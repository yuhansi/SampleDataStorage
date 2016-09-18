package com.example.android.pets;

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
     * Inner class that defines the constant values for the pets
     * database table
     * Each entry in the table represents a single pet
     */
    public static final class PetEntry implements BaseColumns {

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
    }
}
