package com.example.safsaf.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Safsaf on 6/11/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Create a new {@link BookAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param books is the list of {@link Book}s to be displayed.
     */
    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID miwok_text_view.
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        // Get the Miwok translation from the currentWord object and set this text on
        // the Miwok TextView.
       titleTextView.setText(currentBook.getTitle());

        // Find the TextView in the list_item.xml layout with the ID default_text_view.
        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.authors);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        authorsTextView.setText(currentBook.getAuthors());

        // Find the TextView in the list_item.xml layout with the ID default_text_view.
        TextView publishedDateTextView = (TextView) listItemView.findViewById(R.id.publishedDate);
        // Get the default translation from the currentWord object and set this text on
        // the default TextView.
        publishedDateTextView.setText(currentBook.getPublishedDate());

        // Return the whole list item layout (containing 2 TextViews) so that it can be shown in
        // the ListView.
        return listItemView;
    }
}