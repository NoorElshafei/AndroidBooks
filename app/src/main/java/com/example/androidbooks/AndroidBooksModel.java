package com.example.androidbooks;

public class AndroidBooksModel {
    private String bookName,bookPic,bookLanguage,bookAuthor,bookLink,bookPrice;


    public AndroidBooksModel(String bookName, String bookPic, String bookLanguage, String bookAuthor, String bookPrice,String bookLink) {
        this.bookName = bookName;
        this.bookPic = bookPic;
        this.bookLanguage = bookLanguage;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;

        this.bookLink=bookLink;
    }


    public String getBookLink() {
        return bookLink;
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

    public String getBookPrice() {
        return bookPrice;
    }
}
