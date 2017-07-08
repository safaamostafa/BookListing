package com.example.safsaf.booklisting;

import android.media.Image;

/**
 * Created by Safsaf on 6/11/2017.
 * An {@link Book} object contains information related to single book
 */

public class Book {

    /** title OF BOOK*/
    private String mTitle;
    /** authors OF BOOK*/
    private String mAuthors;
    /** publishedDate  OF BOOK*/
    private String mPublishedDate;

    /**
     *   construct a new {@link Book} object
     *   @param  title is the title of the book
     *   @param  authors is the authors of the book
     *   @param  publishedDate is the publishedDate of the book*/
 public Book(String title,String authors,String publishedDate){
     mTitle=authors;
     mAuthors=title;
     mPublishedDate=publishedDate;

 }// public Book()

    /** Returns the title of the book*/

    public String getTitle(){return mTitle;};

    /** Returns the authors of the book*/

    public String getAuthors(){return mAuthors;};

    /** Returns the publishedDate of the book*/

    public String getPublishedDate(){return mPublishedDate;};

}// public class Book
