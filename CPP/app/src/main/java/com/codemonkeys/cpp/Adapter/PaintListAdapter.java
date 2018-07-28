package com.codemonkeys.cpp.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.codemonkeys.cpp.PaintItem;
import com.codemonkeys.cpp.R;
import com.codemonkeys.cpp.Utils.ToastUtil;

import java.util.List;

public class PaintListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<PaintItem> items;
    private String URL;

    public PaintListAdapter(Context context, List<PaintItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_paint_list_row, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((Item)holder).textView.setText(items.get(position).getPaintName());
        GradientDrawable gd =  (GradientDrawable)(((Item)holder).view.getBackground());
        gd.setColor(Color.parseColor(items.get(position).getColourCode()));

        ((Item)holder).inventoryBtn.setChecked(items.get(position).getInvetory());
        ((Item)holder).wishBtn.setChecked(items.get(position).getWishList());

        ((Item)holder).inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAdapter dba = new DBAdapter(context);
                if(dba.addToInventory(items.get(position)) == false) {
                    dba.open();
                    dba.removeFromInventory(items.get(position).getId());
                    dba.close();
                }
            }
        });

        ((Item)holder).wishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAdapter dba = new DBAdapter(context);
                if(dba.addToWishlist(items.get(position)) == false) {
                    dba.open();
                    dba.removeFromWishlist(items.get(position).getId());
                    dba.close();
                }
            }
        });

         // Code to create dynamic shopping URL after each item is clicked
        ((Item)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paintName = items.get(position).getPaintName().replace(' ', '-');
                SharedPreferences settings = context.getSharedPreferences("CPP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                URL = "https://www.games-workshop.com/en-CA/" + items.get(position).getType() + "-" + paintName;
                editor.putString("URL", URL);
                editor.commit();

                ToastUtil.DisplayToast(context, items.get(position).getPaintName() + " selected!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Item extends RecyclerView.ViewHolder {
        TextView textView;
        ToggleButton inventoryBtn;
        ToggleButton wishBtn;
        View view;
        public Item(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
            inventoryBtn = (ToggleButton) itemView.findViewById(R.id.addToInventory);
            wishBtn = (ToggleButton) itemView.findViewById(R.id.addWishListBtn);
            view = (View) itemView.findViewById(R.id.colorPalet);
        }
    }
}
