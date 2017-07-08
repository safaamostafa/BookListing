package com.example.safsaf.booklisting;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>{

    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=%7Byour%20key%20words%20here%7D";

    /**
     * +     * Constant value for the earthquake loader ID. We can choose any integer.
     * +     * This really only comes into play if you're using multiple loaders.
     * +
     */
    public static final int BOOK_LOADER_ID = 1;

    /**
     * Adapter for the list of earthquakes
     */
    private BookAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

// Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, GOOGLE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
// Clear the adapter of previous earthquake data
        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
// Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}