package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by hanssi on 9/19/16.
 */

/**
 * PetCursorAdapter is an adapter for a list view that uses a Cursor of pet data as its
 * data source. This adapter will create list items for each row of pet data in the
 * Cursor
 */
public class PetCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new PetCursorAdapter
     * @param context The context
     * @param cursor The cursor from which to get data
     */
    public PetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* flags */);
    }

    /**
     * Make a new blank list item view
     * No data is set to the views yet
     * @param context Context of this app
     * @param cursor The cursor from which to get data, it is already moved to the correct row
     * @param parent The parent to which the new view is attached to
     * @return the newly created list item view
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by the cursor) to the given
     * list item layout.
     * @param view Returned by newView() method
     * @param context Context of this app
     * @param cursor The cursor from which to get data, it is already moved to the correct row
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find individual views to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);

        // Find the columns of pet attributes
        int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);

        // Read the pet attributes from the Cursor for the current pet
        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);

        // If the pet breed is empty string or null, then use the default string
        if(TextUtils.isEmpty(petBreed)) {
            petBreed = context.getString(R.string.unknown_breed);
        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(petName);
        summaryTextView.setText(petBreed);

    }
}
