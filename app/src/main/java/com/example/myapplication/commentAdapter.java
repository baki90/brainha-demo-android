package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class commentAdapter extends BaseAdapter {

    List<comment> commentList;
    private Activity context;

    public commentAdapter(Activity _context, List<comment> _commentList){
        context = _context;
        commentList = _commentList;
    }
    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView image = null;
        TextView title = null;
        TextView content = null;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_quest, viewGroup, false);
        }
        image = (ImageView) view.findViewById(R.id.imageView4);
        title = (TextView) view.findViewById(R.id.comment_user);
        content = (TextView) view.findViewById(R.id.comment_content);
        image.setImageResource(R.drawable.user);

        title.setText(Integer.toString(commentList.get(i).id));
        content.setText(commentList.get(i).content);

        return view;
    }
}