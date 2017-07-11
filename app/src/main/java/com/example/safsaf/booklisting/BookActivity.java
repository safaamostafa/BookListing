package com.example.safsaf.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>,
        View.OnClickListener {

    private static final String LOG_TAG = BookActivity.class.getName();

    /**
     * URL for earthquake data from the USGS dataset
     */

    private static final String GOOGLE_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    private View mLoadingIndicator;

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
    private ListView mBookListView;
    private ArrayList<Book> mBooks = new ArrayList<>();
    private String mTitleString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Button search = (Button) findViewById(R.id.search_btn);
        search.setOnClickListener(this);
        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        // Find a reference to the {@link ListView} in the layout
        mBookListView = (ListView) findViewById(R.id.list);
        mBookListView.setEmptyView(mEmptyStateTextView);
        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        if (savedInstanceState == null) {
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            mBookListView.setAdapter(mAdapter);
            mEmptyStateTextView.setText("Android");
            callLoader("android");
        }else {
            mBooks = savedInstanceState.getParcelableArrayList("books_key");
            mTitleString = savedInstanceState.getString("title_string_key");
            mEmptyStateTextView.setText(mTitleString);
            assert mBooks != null;
            mAdapter.addAll(mBooks);
            mAdapter.notifyDataSetChanged();
            mBookListView.setAdapter(mAdapter);
            mBookListView.setSelection(savedInstanceState.getInt("scroll_position_key"));
            mLoadingIndicator.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("scroll_position_key", mBookListView.getFirstVisiblePosition());
        outState.putParcelableArrayList("books_key",mBooks);
        outState.putString("title_string_key", mTitleString);
        super.onSaveInstanceState(outState);
    }

    private void callLoader(String query) {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Bundle b = new Bundle();
        b.putString("query", query);
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            if (null != loaderManager.getLoader(BOOK_LOADER_ID))
                if (loaderManager.getLoader(BOOK_LOADER_ID).isStarted())
                    loaderManager.restartLoader(BOOK_LOADER_ID, b, this);
            loaderManager.initLoader(BOOK_LOADER_ID, b, this);
        } else {
            // Otherwise, display error
            Toast.makeText(BookActivity.this, "No Items Available",Toast.LENGTH_SHORT).show();
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Hide loading indicator because the data has been loaded
        return new BookLoader(this, GOOGLE_REQUEST_URL + bundle.get("query"));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_books);
        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        mBooks.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
            mBooks.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
// Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    @Override
    public void onClick(View v) {
        EditText titleSearchEditText = (EditText) findViewById(R.id.search_title_title);
        int id = v.getId();
        switch (id) {
            case R.id.search_btn:
                String titleString = formatSearchText(titleSearchEditText.getText().toString());
                if (titleString.equals("")) {
                    Toast.makeText(BookActivity.this, getString(R.string.no_input_toast_message), Toast.LENGTH_LONG).show();
                    mTitleString = titleString;
                } else {
                    mTitleString = titleString;
                    callLoader(titleString);
                }
                break;
            default:
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