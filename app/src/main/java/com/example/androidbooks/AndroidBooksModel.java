package com.example.androidbooks;

public class AndroidBooksModel {
    private String bookName,bookPic,bookLanguage,bookAuthor,bookCurrency;
    private double bookPrice;

    public AndroidBooksModel(String bookName, String bookPic, String bookLanguage, String bookAuthor, double bookPrice,String bookCurrency) {
        this.bookName = bookName;
        this.bookPic = bookPic;
        this.bookLanguage = bookLanguage;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.bookCurrency=bookCurrency;
    }

    public String getBookCurrency() {
        return bookCurrency;
    }

    public String getBookName() {
        return bookName;
    }

    public String getBookPic() {
        return bookPic;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public double getBookPrice() {
        return bookPrice;
    }
}
