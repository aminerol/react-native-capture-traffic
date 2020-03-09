package com.reactnative.capturetraffic.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.walmartlabs.electrode.reactnative.bridge.Bridgeable;

public class Body implements Parcelable, Bridgeable {
    private String content;

    private Body() {
    }

    private Body(Body.Builder builder) {
        this.content = builder.content;
    }

    private Body(Parcel in) {
        this(in.readBundle());
    }

    public Body(@NonNull Bundle bundle) {
        this.content = bundle.getString("content");
    }

    public static final Creator<Body> CREATOR = new Creator<Body>() {
        @Override
        public Body createFromParcel(Parcel in) {
            return new Body(in);
        }

        @Override
        public Body[] newArray(int size) {
            return new Body[size];
        }
    };

    @Nullable
    public String getContent() {
        return content;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(toBundle());
    }

    @NonNull
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        return bundle;
    }

    public static class Builder {
        private String content;

        public Builder(@NonNull String content) {
            this.content = content;
        }

        @NonNull
        public Body build() {
            return new Body(this);
        }
    }
}
