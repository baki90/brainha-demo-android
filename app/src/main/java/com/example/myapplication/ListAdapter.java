package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private List<menuList> menu;
    private Activity context;

    public ListAdapter(Activity _context, List<menuList> _menu){
        context = _context;
        menu = _menu;
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int i) {
       return menu.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView image1 = null;
        ImageView image2 = null;

        TextView title = null;
        TextView content = null;
        TextView category = null;
        TextView user = null;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_menu, viewGroup, false);
        }

        image1 = (ImageView) view.findViewById(R.id.imageView2);
        title = (TextView) view.findViewById(R.id.menu_title);
        content = (TextView) view.findViewById(R.id.menu_content);
        category = (TextView) view.findViewById(R.id.menu_category);
        user = (TextView) view.findViewById(R.id.menu_user);
        image2 = (ImageView) view.findViewById(R.id.imageView4);

        if(i == 0){
            image1.setImageResource(R.drawable.fun);
        } else if(i == 1) {
            image1.setImageResource(R.drawable.rec);
        } else {
            image1.setImageResource(R.drawable.rank);
        }

        image2.setImageResource(R.drawable.user);

        title.setText(menu.get(i).title);
        content.setText(menu.get(i).content);
        category.setText(menu.get(i).interesting_category);
        user.setText(menu.get(i).name);

        return view;
    }
}
