package com.movies.popular.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.movies.popular.api.ApiConstants;
import com.movies.popular.model.trailer_api.TrailersResponseBean;
import com.movies.popular.one.R;
import com.movies.popular.utility.SnackBarBuilder;
import com.movies.popular.utility.Utility;

import java.util.ArrayList;

/**
 * Created by mukesh on 30/3/16.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {

    private final Context mContext;
    private final ArrayList<TrailersResponseBean.ResultEntity> resultEntityArrayList;

    public TrailersAdapter(Context mContext, ArrayList<TrailersResponseBean.ResultEntity> resultEntityArrayList) {

        this.mContext = mContext;
        this.resultEntityArrayList = resultEntityArrayList;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_trailers_row, parent, false);
        return new TrailerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder holder, final int position2) {
        final int position = holder.getAdapterPosition();
        if (!TextUtils.isEmpty(resultEntityArrayList.get(position).getKey())) {

            Glide
                    .with(mContext)
                    .load(Utility.getYoutubeThumbUrl(resultEntityArrayList.get(position).getKey()))
                    //.crossFade()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(holder.thumbIv);


        }

        holder.playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String videoUrl = ApiConstants.BASE_VIDEO_URL + resultEntityArrayList.get(position).getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                    intent.setDataAndType(Uri.parse(videoUrl), "video/*");
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    SnackBarBuilder.make(holder.playIv, mContext.getString(R.string.no_video_app_found)).build();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultEntityArrayList.size();
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbIv, playIv;

        TrailerViewHolder(View itemView) {
            super(itemView);
            thumbIv = (ImageView) itemView.findViewById(R.id.trailer_thumb_iv);
            playIv = (ImageView) itemView.findViewById(R.id.trailer_play_iv);
        }
    }

}
