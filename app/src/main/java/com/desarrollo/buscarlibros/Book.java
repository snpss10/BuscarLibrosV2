package com.desarrollo.buscarlibros;

public class Book {

    private String mTitle;
    private String mAuthors;
    private String mCategories;
    private String mPrice;
    private String mThumbnail;
   /* private String mPublishedDate;
    private String mDescription;
    private String mBuy;
    private String mPreview;
    private int pageCount;*/
    private String mUrl;

    public Book(String mTitle, String mAuthors, String mCategories, String mThumbnail, String price ) {
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
        this.mCategories = mCategories;
        this.mThumbnail = mThumbnail;
        this.mPrice = price;

    }




    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public String getCategories() {
        return mCategories;
    }

    public String getThumbnail() {
        return mThumbnail;
    }


    public String getPrice() {
        return mPrice;
    }
}
