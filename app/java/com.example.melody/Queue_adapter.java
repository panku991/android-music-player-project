package com.example.melody;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Queue_adapter extends RecyclerView.Adapter<Queue_adapter.MyViewHolderQueue>{


    private Context mcontext;
    private ArrayList<Music_Model> mfiles;

    public Queue_adapter(Context mcontext, ArrayList<Music_Model> mfiles) {
        this.mcontext = mcontext;
        this.mfiles = mfiles;
    }


    @NonNull
    @Override
    public MyViewHolderQueue onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.music_items,parent,false);

        return new MyViewHolderQueue(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderQueue holder, int position) {

        position=holder.getAdapterPosition();

        holder.file_name.setText(mfiles.get(position).getTitle());
        holder.file_artist.setText(mfiles.get(position).getArtist());

        byte[] image = getAlbumArt(mfiles.get(position).getPath());

        if (image != null){
            Glide.with(mcontext).asBitmap().load(image).into(holder.album_art);
        }

        else{
            Glide.with(mcontext).load(R.mipmap.img).into(holder.album_art);
        }

        //Animations
//        setAnimation(holder.itemView,position);

        int finalPosition = position;


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyMediaPlayer.getInstance().reset();

                if (MyMediaPlayer.currentIndex==(-1)){
                    MyMediaPlayer.currentIndex=holder.getAdapterPosition();
                }
                else {
                    MyMediaPlayer.currentIndex=holder.getAdapterPosition()-1;
                }

                Intent youtIntent = new Intent(view.getContext(),MusicPlayer.class);

                youtIntent.putExtra("LIST",mfiles);
                youtIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                PendingIntent pendingIntent = PendingIntent.getActivity(view.getContext(),0,youtIntent,0);

                try{
                    pendingIntent.send(view.getContext(),0,youtIntent);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }



            }
        });

    }

    @Override
    public int getItemCount() {
        return mfiles.size();
    }

    public class MyViewHolderQueue extends RecyclerView.ViewHolder {
        TextView file_name,file_artist;
        ImageView album_art;


        public MyViewHolderQueue(@NotNull View itemView ){
            super(itemView);

            file_name = itemView.findViewById(R.id.Music_fileName);
            album_art = itemView.findViewById(R.id.music_img);
            file_artist = itemView.findViewById(R.id.Music_artist);

        }
    }

    private byte[] getAlbumArt(String path){
        Uri uri = Uri.parse(path);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

//
//    private void setAnimation(View view,int position){
//
//        Animation slidIn = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_out_right);
//        view.setAnimation(slidIn);
//
//
//    }

}
