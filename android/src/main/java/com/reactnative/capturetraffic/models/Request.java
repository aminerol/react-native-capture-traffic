package com.reactnative.capturetraffic.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.megatronking.netbare.http.HttpRequest;
import com.walmartlabs.electrode.reactnative.bridge.Bridgeable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.walmartlabs.electrode.reactnative.bridge.util.BridgeArguments.bridgeablesToBundleArray;
import static com.walmartlabs.electrode.reactnative.bridge.util.BridgeArguments.getList;
import static com.walmartlabs.electrode.reactnative.bridge.util.BridgeArguments.getNumberValue;

public class Request implements Bridgeable, Parcelable {

    private String id;
    private String host;
    private String protocol;
    private String ip;
    private Boolean isHttps;
    private String method;
    private String path;
    private Integer port;
    private List<Header> headers;
    private Integer bodyOffset;
    private Boolean streamEnd;
    private Integer streamId;
    private Long time;
    private Integer uid;
    private String url;

    private Request(Builder builder) {
        this.id = builder.id;
        this.host = builder.host;
        this.protocol = builder.protocol;
        this.ip = builder.ip;
        this.isHttps = builder.isHttps;
        this.method = builder.method;
        this.path = builder.path;
        this.port = builder.port;
        this.headers = builder.headers;
        this.bodyOffset = builder.bodyOffset;
        this.streamEnd = builder.streamEnd;
        this.streamId = builder.streamId;
        this.time = builder.time;
        this.uid = builder.uid;
        this.url = builder.url;
    }

    protected Request(Parcel in) {
        this(in.readBundle());
    }

    public Request(@NonNull Bundle bundle) {
        this.id = bundle.getString("id");
        this.host = bundle.getString("host");
        this.protocol = bundle.getString("protocol");
        this.ip = bundle.getString("ip");
        this.isHttps = bundle.getBoolean("isHttps");
        this.method = bundle.getString("method");
        this.path = bundle.getString("path");
        this.port = getNumberValue(bundle, "port") == null ? null : getNumberValue(bundle, "bodyOffset").intValue();
        this.bodyOffset = getNumberValue(bundle, "bodyOffset") == null ? null : getNumberValue(bundle, "bodyOffset").intValue();
        this.streamEnd = bundle.getBoolean("streamEnd");
        this.streamId = getNumberValue(bundle, "streamId") == null ? null : getNumberValue(bundle, "streamId").intValue();
        this.time = getNumberValue(bundle, "time") == null ? null : getNumberValue(bundle, "time").longValue();
        this.uid = getNumberValue(bundle, "uid") == null ? null : getNumberValue(bundle, "uid").intValue();
        this.url = bundle.getString("url");
        this.headers = bundle.containsKey("headers") ? getList(bundle.getParcelableArray("headers"), Header.class) : new ArrayList<Header>();

    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(toBundle());
    }


    @NonNull
    @Override
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        if (getId() != null) {
            bundle.putString("id", getId());
        }
        if (getHost() != null) {
            bundle.putString("host", getHost());
        }
        if (getProtocol() != null) {
            bundle.putString("protocol", getProtocol());
        }
        if (getIp() != null) {
            bundle.putString("ip", getIp());
        }
        if (getHttps() != null) {
            bundle.putBoolean("isHttps", getHttps());
        }
        if (getMethod() != null) {
            bundle.putString("method", getMethod());
        }
        if (getPath() != null) {
            bundle.putString("path", getPath());
        }
        if (getPort() != null) {
            bundle.putInt("port", getPort());
        }

        if (getBodyOffset() != null) {
            bundle.putInt("bodyOffset", getBodyOffset());
        }
        if (getStreamEnd() != null) {
            bundle.putBoolean("streamEnd", getStreamEnd());
        }
        if (getStreamId() != null) {
            bundle.putInt("streamId", getStreamId());
        }
        if (getTime() != null) {
            bundle.putLong("time", getTime());
        }
        if (getUid() != null) {
            bundle.putInt("uid", getUid());
        }
        if (getUrl() != null) {
            bundle.putString("url", getUrl());
        }

        if (headers != null && !headers.isEmpty()) {
            bundle.putParcelableArray("headers", bridgeablesToBundleArray(headers));
        }

        return bundle;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getHost() {
        return host;
    }

    @Nullable
    public String getProtocol() {
        return protocol;
    }

    @Nullable
    public String getIp() {
        return ip;
    }

    @Nullable
    public Boolean getHttps() {
        return isHttps;
    }

    @Nullable
    public String getMethod() {
        return method;
    }

    @Nullable
    public String getPath() {
        return path;
    }

    @Nullable
    public Integer getPort() {
        return port;
    }

    @Nullable
    public List<Header> getHeaders() {
        return headers;
    }

    @Nullable
    public Integer getBodyOffset() {
        return bodyOffset;
    }

    @Nullable
    public Boolean getStreamEnd() {
        return streamEnd;
    }

    @Nullable
    public Integer getStreamId() {
        return streamId;
    }

    @Nullable
    public Long getTime() {
        return time;
    }

    @Nullable
    public Integer getUid() {
        return uid;
    }

    @Nullable
    public String getUrl() {
        return url;
    }

    public static class Builder {
        private final String id;
        private final String host;
        private final String protocol;
        private final String ip;
        private final Boolean isHttps;
        private final String method;
        private final String path;
        private final Integer port;
        private final List<Header> headers;
        private final Integer bodyOffset;
        private final Boolean streamEnd;
        private final Integer streamId;
        private final Long time;
        private final Integer uid;
        private final String url;

        public Builder(@NonNull String id, @NonNull String host, @NonNull String protocol, @NonNull String ip,
                       @NonNull Boolean isHttps, @NonNull String method, @NonNull String path, @NonNull Integer port,
                       @NonNull List<Header> headers, @NonNull Integer bodyOffset, @NonNull Boolean streamEnd, @NonNull Integer streamId,
                       @NonNull Long time, @NonNull Integer uid, @NonNull String url) {
            this.id = id;
            this.host = host;
            this.protocol = protocol;
            this.ip = ip;
            this.isHttps = isHttps;
            this.method = method;
            this.path = path;
            this.port = port;
            this.headers = headers;
            this.bodyOffset = bodyOffset;
            this.streamEnd = streamEnd;
            this.streamId = streamId;
            this.time = time;
            this.uid = uid;
            this.url = url;

        }


        @NonNull
        public Request build() {
            return new Request(this);
        }
    }
}
