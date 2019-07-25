package com.example.androidbooks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class AndroidBooksAdapter extends ArrayAdapter<AndroidBooksModel> {


    public AndroidBooksAdapter( Context context, int resource, List<AndroidBooksModel> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listview =convertView;
        if(listview==null){
            listview= LayoutInflater.from(getContext()).inflate(
                    R.layout.book_item,parent,false);
        }
        AndroidBooksModel currentBook=getItem(position);

        TextView nameBookTextView= listview.findViewById(R.id.name_book_text_view);
        TextView authorBookTextView= listview.findViewById(R.id.author_text_view);
        TextView languageBookTextView= listview.findViewById(R.id.language_text_view);
        TextView currencyBookTextView= listview.findViewById(R.id.currency_text_view);
        TextView priceBookTextView= listview.findViewById(R.id.price_text_view);
        ImageView bookImageView=listview.findViewById(R.id.book_image_view);

        nameBookTextView.setText(currentBook.getBookName());
        authorBookTextView.setText(currentBook.getBookAuthor());
        languageBookTextView.setText(currentBook.getBookLanguage());
        currencyBookTextView.setText(currentBook.getBookCurrency());
        priceBookTextView.setText(""+currentBook.getBookPrice());
        Picasso.get().load(currentBook.getBookPic()).into(bookImageView);



    }
}
