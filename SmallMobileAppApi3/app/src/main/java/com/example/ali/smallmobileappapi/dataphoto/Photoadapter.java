package com.example.ali.smallmobileappapi.dataalbum;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ali.smallmobileappapi.R;
import com.example.ali.smallmobileappapi.dataphoto.Photo;
import com.example.ali.smallmobileappapi.ui.ListPhoto;
import com.example.ali.smallmobileappapi.ui.Photoaf;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 'Ali' on 30/11/2017.
 */

public class Photoadapter extends RecyclerView.Adapter<Photoadapter.Viewholder>{

    public static class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView photourl;
        List<Photo> photos=  new ArrayList<>();
        Context ctx;

        public Viewholder(View itemView,Context cntxt,List<Photo> alb) {
            super(itemView);
            this.photos = alb;
            this.ctx = cntxt;
            itemView.setOnClickListener(this);
            photourl = (ImageView) itemView.findViewById(R.id.photourl);
        }

        @Override
        public void onClick(View view) {
            int position =  getAdapterPosition();
            Photo photos = this.photos.get(position);
            Intent intent =  new Intent(this.ctx, Photoaf.class);
            intent.putExtra("id_photo",photos.getId());
            this.ctx.startActivity(intent);
        }
    }
    private Context context;
    private List<Photo> photos;

    public Photoadapter(Context c , List<Photo> photoslist){
        this.context = c;
        this.photos = photoslist;
    }

    @Override
    public Photoadapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.photo,parent,false);
        return new Viewholder(v,context,photos);
    }

    @Override
    public void onBindViewHolder(Photoadapter.Viewholder holder, int position) {
        Photo photoss=  photos.get(position);

        // holder.cover_photourl.setImageUrl(albumpo.getCover_photourl() );
        Picasso.with(context).load(photoss.getUrl_photo()).into(holder.photourl);

    }

    @Override
    public int getItemCount() {

        return photos.size();
    }
}
