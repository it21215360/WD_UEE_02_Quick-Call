

package com.example.ueeapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Shop_Adapter extends RecyclerView.Adapter<Shop_Adapter.ShopViewHolder> {

    private List<Shop> shopList;


    public Shop_Adapter(List<Shop> shopList) {
        this.shopList = shopList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.shopImage.setImageResource(shop.getImageResource());
        holder.shopName.setText(shop.getName());
        holder.shopCity.setText(shop.getCity());
        holder.shopTime.setText(shop.getTime());
        holder.shopRating.setText("Rating: " + shop.getRating());

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shopName = shop.getName();
                String shopCity = shop.getCity();
                String shopTime = shop.getTime();
                float shopRating = shop.getRating();
                int imageResource = shop.getImageResource();
                CartShopItem cartItem = new CartShopItem(shopName, shopCity, shopTime, shopRating, imageResource);

                Intent intent = new Intent(view.getContext(), LaundryOrder.class);
                intent.putExtra("shopName", shopName);
                view.getContext().startActivity(intent);

                Toast.makeText(view.getContext(), "You have selected a Laundry ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    // This method is used to update the shop list in the adapter
    public void updateShopList(List<Shop> newList) {
        shopList.clear();
        shopList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView shopImage;
        TextView shopName;
        TextView shopCity;
        TextView shopTime;
        TextView shopRating;
        Button addButton;

        public ShopViewHolder(View itemView) {
            super(itemView);
            shopImage = itemView.findViewById(R.id.shopImage);
            shopName = itemView.findViewById(R.id.shopName);
            shopCity = itemView.findViewById(R.id.shopCity);
            shopTime = itemView.findViewById(R.id.shopTime);
            shopRating = itemView.findViewById(R.id.shopRating);
            addButton = itemView.findViewById(R.id.addButton);
        }
    }
}