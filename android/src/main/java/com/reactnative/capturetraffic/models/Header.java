package com.reactnative.capturetraffic.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.walmartlabs.electrode.reactnative.bridge.Bridgeable;

import java.util.ArrayList;

import static com.walmartlabs.electrode.reactnative.bridge.util.BridgeArguments.getList;

public class Header implements Parcelable, Bridgeable {

    private String name;
    private ArrayList<String> value;

    private Header() {
    }

    private Header(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
    }

    private Header(Parcel in) {
        this(in.readBundle());
    }

    public Header(@NonNull Bundle bundle) {
        this.name = bundle.getString("name");
        this.value = bundle.getStringArrayList("value");
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    /**
     * Log ???
     *
     * @return Boolean
     */
    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Is the user a Sam&#39;s club member
     *
     * @return Boolean
     */
    @NonNull
    public ArrayList<String> getValue() {
        return value;
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
        bundle.putString("name", name);
        if (value != null && !value.isEmpty()) {
            bundle.putStringArrayList("value", value);
        }
        return bundle;
    }

    public static class Builder {
        private String name;
        private ArrayList<String> value;

        public Builder(@NonNull String name, @NonNull ArrayList<String> value) {
            this.name = name;
            this.value = value;
        }

        public Builder name(@Nullable String name) {
            this.name = name;
            return this;
        }

        public Builder value(@Nullable ArrayList<String> value) {
            this.value = value;
            return this;
        }

        @NonNull
        public Header build() {
            return new Header(this);
        }
    }
}
