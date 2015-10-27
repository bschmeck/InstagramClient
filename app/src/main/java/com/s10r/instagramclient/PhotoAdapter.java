package com.s10r.instagramclient;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bschmeckpeper on 10/25/15.
 */
public class PhotoAdapter extends ArrayAdapter<Photo> {
    public PhotoAdapter(Context context, List<Photo> photos) {
        super(context, android.R.layout.simple_list_item_1, photos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        return populate(photo, convertView);
    }

    public View populate(Photo photo, View view) {
        TextView tvCaption = (TextView) view.findViewById(R.id.tvCaption);
        tvCaption.setText(photo.getCaption());
        tvCaption.setMaxLines(PhotosActivity.COLLAPSED_LINES);
        tvCaption.setEllipsize(TextUtils.TruncateAt.END);

        TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvUsername.setText(photo.getUsername());

        TextView tvLikes = (TextView) view.findViewById(R.id.tvLikes);
        tvLikes.setText(photo.getLikesCount() + " likes");

        TextView tvTimestamp = (TextView) view.findViewById(R.id.tvTimestamp);
        tvTimestamp.setText(relativeTime(photo.getCreatedAt()));

        ImageView ivPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        ivPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.getImageUrl()).into(ivPhoto);

        return view;
    }

    private String relativeTime(Date other) {
        long distance = Math.abs(other.getTime() - (new Date()).getTime());

        if (distance < DateUtils.MINUTE_IN_MILLIS) {
            return "just now";
        } else if (distance > DateUtils.WEEK_IN_MILLIS) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return formatter.format(other);
        }

        long threshold;
        String timeUnit;

        if (distance < DateUtils.HOUR_IN_MILLIS) {
            threshold = DateUtils.MINUTE_IN_MILLIS;
            timeUnit = "minute";
        } else if (distance < DateUtils.DAY_IN_MILLIS) {
            threshold = DateUtils.HOUR_IN_MILLIS;
            timeUnit = "hour";
        } else {
            threshold = DateUtils.DAY_IN_MILLIS;
            timeUnit = "day";
        }

        long numeral = distance / threshold;
        String pluralSuffix = "";
        if (numeral > 1) {
            pluralSuffix = "s";
        }

        return String.format("%d %s%s ago", numeral, timeUnit, pluralSuffix);
    }
}
