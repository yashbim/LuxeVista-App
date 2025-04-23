package com.example.luxevista_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room_Info> roomList;
    private List<Room_Info> filteredRoomList;

    public RoomAdapter(Context context, List<Room_Info> roomList) {
        this.context = context;
        this.roomList = roomList;
        this.filteredRoomList = new ArrayList<>(roomList);
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_card, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room_Info room = filteredRoomList.get(position);
        holder.title.setText(room.getTitle());
        holder.description.setText(room.getDescription());
        holder.price.setText(room.getPrice());
        holder.image.setImageResource(room.getImageResId());

        // Display availability indicator if needed
        // Could add an availability indicator to your item_room_card.xml
    }

    @Override
    public int getItemCount() {
        return filteredRoomList.size();
    }

    // Filter method that will be called from ViewRooms activity
    public void filterRooms(String roomType, double minPrice, double maxPrice, boolean availableOnly) {
        filteredRoomList.clear();

        for (Room_Info room : roomList) {
            boolean matchesRoomType = roomType.equals("All") || room.getRoomType().equals(roomType);
            boolean matchesPrice = (room.getPriceValue() >= minPrice &&
                    (maxPrice == 0 || room.getPriceValue() <= maxPrice));
            boolean matchesAvailability = !availableOnly || room.isAvailable();

            if (matchesRoomType && matchesPrice && matchesAvailability) {
                filteredRoomList.add(room);
            }
        }

        notifyDataSetChanged();
    }

    // Sort method
    public void sortRooms(String sortBy) {
        switch (sortBy) {
            case "Price (Low to High)":
                filteredRoomList.sort((r1, r2) -> Double.compare(r1.getPriceValue(), r2.getPriceValue()));
                break;
            case "Price (High to Low)":
                filteredRoomList.sort((r1, r2) -> Double.compare(r2.getPriceValue(), r1.getPriceValue()));
                break;
            case "Room Type":
                filteredRoomList.sort((r1, r2) -> r1.getRoomType().compareTo(r2.getRoomType()));
                break;
            // Add more sorting options as needed
        }

        notifyDataSetChanged();
    }

    // Reset filters
    public void resetFilters() {
        filteredRoomList.clear();
        filteredRoomList.addAll(roomList);
        notifyDataSetChanged();
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