package com.example.safsaf.booklisting;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * URL for earthquake data from the USGS dataset
     */

    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=%7Byour%20key%20words%20here%7D";

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;


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

        Button search =(Button) findViewById(R.id.search_btn);


        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);


        // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);

                        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getSupportLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        } else {
                        // Otherwise, display error
                                // First, hide loading indicator so error message will be visible
                                        View loadingIndicator = findViewById(R.id.loading_indicator);
                        loadingIndicator.setVisibility(View.GONE);

                                // Update empty state with no connection error message
                                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, GOOGLE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

      // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_books);

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


    // search

    @Override
    public void onClick(View v) {
        //finding views by id and setting to variables
        EditText titleSearchEditText = (EditText) findViewById(R.id.search_title_title);
        if(v.getId() == R.id.search_btn) {

            //Formatting Strings from EditText views prior to setting corresponding variables.
            String titleString = formatSearchText(titleSearchEditText.getText().toString());


            //Displaying toast message if search was pressed without entering text or if text was entered by
            //user, then starting BookReportActivity and sending text entered by user as extras in intent.
            if (titleString.equals("")) {
                Toast.makeText(BookActivity.this, getString(R.string.no_input_toast_message), Toast.LENGTH_LONG).show();
            } else {

                Intent intent = new Intent(BookActivity.this, BookActivity.class);
                intent.putExtra("TITLE_SEARCH_STRING", titleString);

                startActivity(intent);
            }
        }else{

            //Clearing EditText views of any user typed text.
            titleSearchEditText.setText("");

        }
    }

    /**
     * Formatting strings entered by user to be suitable for the google books API search.
     * First trimming leading and trailing spaces, then replacing all spaces between words with + character.
     */
    private String formatSearchText(String string) {
        String trimmedString = string.trim();
        do {
            trimmedString = trimmedString.replace(" ", "+");
        } while (trimmedString.contains(" "));
        return trimmedString;
    }


}