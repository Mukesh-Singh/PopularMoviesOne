package com.movies.popular.model.review_api;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by mukesh on 30/3/16.
 */
public class ReviewsListingResponse {
    long id, page, total_pages, total_results;

    ArrayList<ReviewsEntity> results;

    public long getId() {
        return id;
    }

    public long getPage() {
        return page;
    }

    public long getTotal_pages() {
        return total_pages;
    }

    public long getTotal_results() {
        return total_results;
    }

    public ArrayList<ReviewsEntity> getResults() {
        return results;
    }

    public static class ReviewsEntity implements Parcelable{
        String id, author, content, url;

        public ReviewsEntity(){

        }
        protected ReviewsEntity(Parcel in) {
            id = in.readString();
            author = in.readString();
            content = in.readString();
            url = in.readString();
        }

        public static final Creator<ReviewsEntity> CREATOR = new Creator<ReviewsEntity>() {
            @Override
            public ReviewsEntity createFromParcel(Parcel in) {
                return new ReviewsEntity(in);
            }

            @Override
            public ReviewsEntity[] newArray(int size) {
                return new ReviewsEntity[size];
            }
        };

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(id);
            parcel.writeString(author);
            parcel.writeString(content);
            parcel.writeString(url);
        }
    }
}
