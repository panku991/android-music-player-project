package com.example.melody;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Music_Fav_Adapter extends RecyclerView.Adapter<Music_Fav_Adapter.MyViewHolderFAV> {

    private Context mcontext;
    private ArrayList<Music_Model> mfiles;

    public Music_Fav_Adapter(Context mcontext, ArrayList<Music_Model> mfiles){
        this.mcontext=mcontext;
        this.mfiles=mfiles;
    }


    @Override
    public MyViewHolderFAV onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.music_items,parent,false);

        return new MyViewHolderFAV(view);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderFAV holder, int position) {
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
        setAnimation(holder.itemView,position);



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

                Intent intent = new Intent(view.getContext() , MusicPlayer.class);
                intent.putExtra("LIST",mfiles);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final int witch_item = holder.getAdapterPosition();

                new AlertDialog.Builder(view.getContext())
                        .setTitle("Remove :- ")
                        .setMessage("Do you want to remove this song from favourite songs list")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                DBHelper dbHelper = new DBHelper(view.getContext());
                                dbHelper.removeSongs(mfiles.get(witch_item).getPath());
                            }
                        })
                        .setNegativeButton("no",null)
                        .show();


                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mfiles.size();
    }

    public class MyViewHolderFAV extends RecyclerView.ViewHolder{
        TextView file_name,file_artist;
        ImageView album_art;


        public MyViewHolderFAV(@NotNull View itemView ){
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


    private void setAnimation(View view,int position){

        Animation slidIn = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
        view.setAnimation(slidIn);


    }
}
