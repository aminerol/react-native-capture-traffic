package com.reactnative.capturetraffic;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.github.megatronking.netbare.http.HttpMethod;
import com.github.megatronking.netbare.http.HttpProtocol;
import com.github.megatronking.netbare.http.HttpRequest;
import com.github.megatronking.netbare.http.HttpRequestHeaderPart;
import com.github.megatronking.netbare.http.HttpResponse;
import com.github.megatronking.netbare.http.HttpResponseHeaderPart;
import com.reactnative.capturetraffic.models.Header;
import com.reactnative.capturetraffic.models.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    public static Request serializeHttpRequest(@NonNull HttpRequest request) {
        ArrayList<Header> headers = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : request.requestHeaders().entrySet())
        {
            headers.add(new Header.Builder(entry.getKey(), new ArrayList(entry.getValue())).build());
        }
        return new Request.Builder(request.id(), request.host(), request.host(),
                request.protocol().name(), request.isHttps(), request.method().name(),
                request.path(), request.port(), headers,
                request.requestBodyOffset(), request.requestStreamEnd(),
                request.streamId(), request.time(), request.uid(), request.url()).build();
    }

    public static HttpRequestHeaderPart buildHeader(HttpRequest request) {
        return new HttpRequestHeaderPart.Builder(request.httpProtocol(), Uri.parse(request.url()),
                request.requestHeaders(), request.method())
                .build();
    }
    public static HttpRequestHeaderPart buildHeader(Request request) {
        HashMap<String, List<String>> headers= new HashMap<String, List<String>>();
        for(Header header: request.getHeaders()){
            headers.put(header.getName(), header.getValue());
        }
        return new HttpRequestHeaderPart.Builder(HttpProtocol.parse(request.getProtocol()), Uri.parse(request.getUrl()),
                headers, HttpMethod.parse(request.getMethod()))
                .build();
    }

    public static HttpResponseHeaderPart buildHeader(HttpResponse response) {
        return new HttpResponseHeaderPart.Builder(response.httpProtocol(), Uri.parse(response.url()),
                response.responseHeaders(), response.code(), response.message())
                .build();
    }
}