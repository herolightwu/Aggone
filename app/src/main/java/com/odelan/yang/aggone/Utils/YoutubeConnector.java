package com.odelan.yang.aggone.Utils;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.odelan.yang.aggone.Model.VideoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;

    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest hr) throws IOException {}
        }).setApplicationName("Aggone").build();

        try{
            query = youtube.search().list("id,snippet");
            query.setKey(Constants.DEVELOPER_KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");
        } catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }
    }

    public List<VideoItem> search(String keywords){
        query.setQ(keywords);
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<VideoItem> items = new ArrayList<>();
            for(SearchResult result:results){
                VideoItem item = new VideoItem();
                item.title = result.getSnippet().getTitle();
                item.description = result.getSnippet().getDescription();
                item.thumbnailURL = result.getSnippet().getThumbnails().getHigh().getUrl();
                item.id = result.getId().getVideoId();
                items.add(item);
            }
            return items;
        } catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return new ArrayList<>();
        }
    }
}
