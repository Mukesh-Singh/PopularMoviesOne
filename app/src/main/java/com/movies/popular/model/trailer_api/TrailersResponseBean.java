package com.movies.popular.model.trailer_api;

import java.util.ArrayList;

/**
 * Created by mukesh on 30/3/16.
 */
public class TrailersResponseBean {

    long id;
    ArrayList<ResultEntity> results;

    public ArrayList<ResultEntity> getResults() {
        return results;
    }

    public long getId() {
        return id;
    }

    public static class ResultEntity {
        String id, iso_639_1, iso_3166_1, key, name, site, size, type;

        public String getId() {
            return id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getIso_3166_1() {
            return iso_3166_1;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public String getSize() {
            return size;
        }

        public String getType() {
            return type;
        }
    }
}
