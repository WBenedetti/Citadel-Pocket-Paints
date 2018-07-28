package com.codemonkeys.cpp.Adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import com.codemonkeys.cpp.EmptyWishlistFragment;
import com.codemonkeys.cpp.PaintItem;
import com.codemonkeys.cpp.R;
import com.codemonkeys.cpp.Utils.IntentUtil;
import com.codemonkeys.cpp.Utils.ToastUtil;

import java.util.List;

/**
 * Created by Nicholas on 12/21/2017.
 */

public class WishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements EmptyWishlistFragment.OnFragmentInteractionListener {

    Context context;
    List<PaintItem> items;
    private String URL;

    public WishlistAdapter(Context context, List<PaintItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.custom_paint_wishlist_row, parent, false);
        WishlistAdapter.Item item = new WishlistAdapter.Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((WishlistAdapter.Item)holder).textView.setText(items.get(position).getPaintName());
        GradientDrawable gd =  (GradientDrawable)(((WishlistAdapter.Item)holder).view.getBackground());
        if(items.get(position).getColourCode().isEmpty()) {
            gd.setColor(Color.parseColor("#FFFFFF"));
        } else {
            gd.setColor(Color.parseColor(items.get(position).getColourCode()));
        }

        ((WishlistAdapter.Item)holder).inventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBAdapter dba = new DBAdapter(context);
                if(dba.addToInventory(items.get(position)) == false) {
                    ToastUtil.DisplayToast(context, "Paint already in inventory!");
                }
                else {
                    ToastUtil.DisplayToast(context, items.get(position).getPaintName() + "Added to inventory!");
                }
            }
        });

        ((WishlistAdapter.Item)holder).removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = items.get(position).getId();

                DBAdapter myAdapter = new DBAdapter(context);
                myAdapter.open();
                int deleted = myAdapter.removeFromWishlist(id);
                myAdapter.close();

                if(deleted > 0) {
                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, items.size());
                    if(items.size() == 0)
                        EmptyWishlistFragment();
                } else {
                    ToastUtil.DisplayToast(context, "Unable to remove item from wishlist.");
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
        ImageButton inventoryBtn;
        ImageButton removeBtn;
        View view;
        public Item(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
            inventoryBtn = (ImageButton) itemView.findViewById(R.id.addToInventory);
            removeBtn = (ImageButton) itemView.findViewById(R.id.removeFromWishlist);
            view = (View) itemView.findViewById(R.id.colorPalet);
        }
    }

    public void EmptyWishlistFragment() {
        FragmentManager fragmentManager = ((Activity) context).getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EmptyWishlistFragment emptyWishlistFragment= new EmptyWishlistFragment();
        fragmentTransaction.replace(android.R.id.content, emptyWishlistFragment);
        // add to the backstack
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
