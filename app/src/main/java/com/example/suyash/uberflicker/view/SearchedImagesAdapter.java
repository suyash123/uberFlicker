package com.example.suyash.uberflicker.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suyash.uberflicker.R;
import com.example.suyash.uberflicker.model.FlickrImage;

import java.util.List;

public class SearchedImagesAdapter extends RecyclerView.Adapter<SearchedImagesAdapter.MyViewHolder> {

    private List<FlickrImage> flickrImageList;
    private Context context;

    public SearchedImagesAdapter(Context context, List<FlickrImage> flickrImageList) {
        super();
        this.flickrImageList = flickrImageList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_card, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        FlickrImage flickrImage = flickrImageList.get(position);
        viewHolder.setData(flickrImage, position);
    }

    @Override
    public int getItemCount() {
        return flickrImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;

        private FlickrImage flickrImage;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.mSearchImg);
            titleView = itemView.findViewById(R.id.mSearchTitle);
        }

        void setData(final FlickrImage image, final int position) {
            flickrImage = image;

            if (flickrImage.getImageURL() != null) {
                imageView.setImageDrawable(null);
            }
            titleView.setText(flickrImage.getTitle());
        }
    }
}
