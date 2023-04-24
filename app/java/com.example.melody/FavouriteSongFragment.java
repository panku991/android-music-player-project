package com.example.melody;

import static com.example.melody.MainActivity.musicFiles;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteSongFragment extends Fragment {


    RecyclerView recyclerView_fav;
    Music_Fav_Adapter music_adapter_fav;
    SwipeRefreshLayout swipeRefreshLayout_fav;
    ArrayList<Music_Model> FavmusicFiles;







    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouriteSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteSongFragment newInstance(String param1, String param2) {
        FavouriteSongFragment fragment = new FavouriteSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favourite_song, container, false);

        recyclerView_fav = view.findViewById(R.id.recyclerView_favsongs);
        swipeRefreshLayout_fav = view.findViewById(R.id.swipe_refresh_favsongs);

        try {
            FavmusicFiles = getFavSongs(getContext());

            recyclerView_fav.setHasFixedSize(true);
            setFavSongs();

            swipeRefreshLayout_fav.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    FavmusicFiles.clear();
                    try {
                        FavmusicFiles = getFavSongs(getContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setFavSongs();
                    swipeRefreshLayout_fav.setRefreshing(false);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }



        return view ;
    }

    private ArrayList<Music_Model> getFavSongs(Context context) {

        DBHelper dbHelper = new DBHelper(context);
        return dbHelper.fetchFavSongs();

    }

    public void setFavSongs() {
        try {
            if (!(FavmusicFiles.size() < 1)) {

                music_adapter_fav = new Music_Fav_Adapter(getContext(), FavmusicFiles);
                recyclerView_fav.setAdapter(music_adapter_fav);
                recyclerView_fav.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        try {
            if (FavmusicFiles.size() > 0) {
                if (recyclerView_fav != null) {
                    recyclerView_fav.setAdapter(new Music_Fav_Adapter(getContext(), FavmusicFiles));
                    recyclerView_fav.getAdapter().notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }
}
