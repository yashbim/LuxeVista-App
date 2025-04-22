package com.example.luxevista_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_card, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.title.setText(room.getTitle());
        holder.description.setText(room.getDescription());
        holder.price.setText(room.getPrice());
        holder.image.setImageResource(room.getImageResId());
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, price;
        ImageView image;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.roomTitle);
            description = itemView.findViewById(R.id.roomDescription);
            price = itemView.findViewById(R.id.roomPrice);
            image = itemView.findViewById(R.id.roomImage);
        }
    }
}
