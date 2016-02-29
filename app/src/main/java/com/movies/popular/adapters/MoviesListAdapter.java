package com.movies.popular.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.movies.popular.api.ApiConstants;
import com.movies.popular.model.movie_api.MoviesResponseBean;
import com.movies.popular.one.R;

import java.util.ArrayList;

/**
 * Created by mukesh on 1/2/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesListHolder>
{
    private final Context context;
    private int lastPosition = -1;
    private ArrayList<MoviesResponseBean.MoviesResult> mArrayList;

    public MoviesListAdapter(Context context,ArrayList<MoviesResponseBean.MoviesResult> arrayList) {
        this.context = context;
        this.mArrayList=arrayList;
    }
    @Override
    public MoviesListAdapter.MoviesListHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_movie_list, viewGroup, false);

        return new MoviesListHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesListAdapter.MoviesListHolder moviesListHolder, int position) {

        MoviesResponseBean.MoviesResult moviesResult = mArrayList.get(position);
        if (moviesResult!=null && moviesResult.getPosterPath()!=null&& !moviesResult.getPosterPath().isEmpty()) {
            Glide
                    .with(context)
                    .load(ApiConstants.BASE_THUMB_IMAGE_URL_W342 + moviesResult.getPosterPath())
                    //.crossFade()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(moviesListHolder.mImageView);


        }



    }

    public void  onItemClick( int position)
    {

    }
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class MoviesListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView mMovieCardView;
        private ImageView mImageView;
        public MoviesListHolder(View itemView)
        {
            super(itemView);
            mMovieCardView=(CardView)itemView.findViewById(R.id.card_view);
            mImageView=(ImageView)itemView.findViewById(R.id.row_movie_list_imageView);
            mMovieCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick( getAdapterPosition());
        }
    }

}
