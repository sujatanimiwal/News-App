package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, @NonNull ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.list, parent, false);
        }
        News current_obj = (News) getItem(position);

        TextView mtitle = (TextView) view.findViewById(R.id.title);
        TextView msection = (TextView) view.findViewById(R.id.section);
        TextView mauthor = (TextView) view.findViewById(R.id.author);
        TextView mdate = (TextView) view.findViewById(R.id.date);

        String new_title = current_obj.getTitle();
        mtitle.setText(new_title);
        String new_section = current_obj.getSection();
        msection.setText(new_section);
        String new_author = current_obj.getAuthor();
        mauthor.setText(new_author);
        String new_date = current_obj.getDate();
        mdate.setText(new_date);
        return view;
    }
}
