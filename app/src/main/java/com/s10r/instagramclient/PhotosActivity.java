package com.s10r.instagramclient;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;

    public static final String CLIENT_ID = "f727b8c4de2e49d08d590dc4646a5e3f";
    public static final int COLLAPSED_LINES = 3;
    private ArrayList<Photo> popularPhotos;
    private PhotoAdapter photoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        popularPhotos = new ArrayList<>();
        photoAdapter = new PhotoAdapter(this, popularPhotos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photoAdapter);
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;
                try {
                    photoAdapter.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        String caption = "";
                        if (!photoJSON.isNull("caption")) {
                            caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        popularPhotos.add(new Photo(
                                photoJSON.getJSONObject("user").getString("username"),
                                caption,
                                photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"),
                                photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"),
                                photoJSON.getJSONObject("likes").getInt("count"),
                                Long.valueOf(photoJSON.getString("created_time")).longValue()  * DateUtils.SECOND_IN_MILLIS
                        ));

                    }
                    photoAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void toggleFullCaption(View v) {
        TextView tvCaption = (TextView) findViewById(R.id.tvCaption);
        boolean isCollapsed = tvCaption.getMaxLines() == COLLAPSED_LINES;
        if (isCollapsed) {
            tvCaption.setEllipsize(null);
            tvCaption.setMaxLines(Integer.MAX_VALUE);
        } else {
            tvCaption.setEllipsize(TextUtils.TruncateAt.END);
            tvCaption.setMaxLines(COLLAPSED_LINES);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
