package com.codemonkeys.cpp.Adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codemonkeys.cpp.EmptyInventoryFragment;
import com.codemonkeys.cpp.InventoryScreen;
import com.codemonkeys.cpp.MainScreen;
import com.codemonkeys.cpp.PaintItem;
import com.codemonkeys.cpp.R;
import com.codemonkeys.cpp.Utils.IntentUtil;
import com.codemonkeys.cpp.Utils.ToastUtil;

import java.util.List;

/**
 * Created by eric on 2017-12-20.
 */

public class InventoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EmptyInventoryFragment.OnFragmentInteractionListener{


    Context context;
    List<PaintItem> items;

    public InventoryListAdapter(Context context, List<PaintItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_paint_inventory_row, parent, false);
        InventoryListAdapter.Item item = new InventoryListAdapter.Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ((InventoryListAdapter.Item)holder).textView.setText(items.get(position).getPaintName());
        GradientDrawable gd =  (GradientDrawable)(((InventoryListAdapter.Item)holder).view.getBackground());
        if(items.get(position).getColourCode().isEmpty()) {
            gd.setColor(Color.parseColor("#FFFFFF"));
        } else {
            gd.setColor(Color.parseColor(items.get(position).getColourCode()));
        }

        ((InventoryListAdapter.Item)holder).buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String paintName = items.get(position).getPaintName().replace(' ', '-');
                String URL = "https://www.games-workshop.com/en-CA/" + items.get(position).getType() + "-" + paintName;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                IntentUtil.start(i, context);
            }
        });

        ((InventoryListAdapter.Item)holder).deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = items.get(position).getId();

                DBAdapter myAdapter = new DBAdapter(context);
                myAdapter.open();
                int deleted = myAdapter.removeFromInventory(id);
                myAdapter.close();

                if(deleted > 0) {
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, items.size());

                    if(items.size() == 0)
                        EmptyInventoryFragment();
                } else {
                    ToastUtil.DisplayToast(context, "Unable to remove item from inventory.");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class Item extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton buyBtn;
        ImageButton deleteBtn;
        View view;
        public Item(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
            buyBtn = (ImageButton) itemView.findViewById(R.id.buyBtn);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteBtn);
            view = (View) itemView.findViewById(R.id.colorPalet);
        }
    }

    public void EmptyInventoryFragment() {
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EmptyInventoryFragment  emptyInventoryFragment= new EmptyInventoryFragment();
        fragmentTransaction.replace(android.R.id.content, emptyInventoryFragment);
        // add to the backstack
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
