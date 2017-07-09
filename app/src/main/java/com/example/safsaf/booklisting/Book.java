package com.example.safsaf.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Safsaf on 6/11/2017.
 * An {@link Book} object contains information related to single book
 */

public class Book implements Parcelable {

    /**
     * title OF BOOK
     */
    private String mTitle;
    /**
     * authors OF BOOK
     */
    private ArrayList<String> mAuthors;
    /**
     * publishedDate  OF BOOK
     */
    private String mPublishedDate;

    /**
     * construct a new {@link Book} object
     *
     * @param title         is the title of the book
     * @param authors       is the authors of the book
     * @param publishedDate is the publishedDate of the book
     */
    public Book(String title, ArrayList<String> authors, String publishedDate) {
        mTitle = title;
        mAuthors = authors;
        mPublishedDate = publishedDate;

    }// public Book()

    /**
     * Returns the title of the book
     */

    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the authors of the book
     */

    public ArrayList<String> getAuthors() {
        return mAuthors;
    }

    /**
     * Returns the publishedDate of the book
     */

    public String getPublishedDate() {
        return mPublishedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}// public class Book
