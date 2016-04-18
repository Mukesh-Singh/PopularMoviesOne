package com.movies.popular.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movies.popular.model.review_api.ReviewsListingResponse;
import com.movies.popular.one.R;

import java.util.ArrayList;

/**
 * Created by mukesh on 30/3/16.
 */
public class ReviewsListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<ReviewsListingResponse.ReviewsEntity> mArrayList;

    public ReviewsListAdapter(Context mContext, ArrayList<ReviewsListingResponse.ReviewsEntity> mArrayList) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mArrayList = mArrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public ReviewsListingResponse.ReviewsEntity getItem(int i) {
        return mArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_reviews_row, null);
            holder.reviewContentTv = (TextView) convertView.findViewById(R.id.review_content_tv);
            holder.reviewAuthorTv = (TextView) convertView.findViewById(R.id.review_author_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.reviewContentTv.setText(mArrayList.get(i).getContent());
        holder.reviewAuthorTv.setText(mArrayList.get(i).getAuthor());
        return convertView;
    }

    private class ViewHolder {
        public TextView reviewContentTv;
        public TextView reviewAuthorTv;
    }
}