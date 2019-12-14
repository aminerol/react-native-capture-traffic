package com.reactnative.capturetraffic;

import androidx.annotation.NonNull;

import com.github.megatronking.netbare.http.HttpBody;
import com.github.megatronking.netbare.http.HttpRequest;
import com.github.megatronking.netbare.http.HttpRequestHeaderPart;
import com.github.megatronking.netbare.http.HttpResponse;
import com.github.megatronking.netbare.http.HttpResponseHeaderPart;
import com.github.megatronking.netbare.injector.InjectorCallback;
import com.github.megatronking.netbare.injector.SimpleHttpInjector;

import java.io.IOException;

class HttpInjector extends SimpleHttpInjector {
    @Override
    public boolean sniffRequest(@NonNull HttpRequest request) {
        return false;
    }

    @Override
    public boolean sniffResponse(@NonNull HttpResponse response) {
        return false;
    }

    @Override
    public void onRequestInject(@NonNull HttpRequestHeaderPart header,
                                @NonNull InjectorCallback callback) throws IOException {
        callback.onFinished(header);
    }

    @Override
    public void onResponseInject(@NonNull HttpResponseHeaderPart header,
                                 @NonNull InjectorCallback callback) throws IOException {
        callback.onFinished(header);
    }

    @Override
    public void onRequestInject(@NonNull HttpRequest request, @NonNull HttpBody body,
                                @NonNull InjectorCallback callback) throws IOException {
        callback.onFinished(body);
    }

    @Override
    public void onResponseInject(@NonNull HttpResponse response, @NonNull HttpBody body,
                                 @NonNull InjectorCallback callback) throws IOException {
        callback.onFinished(body);
    }

    @Override
    public void onRequestFinished(@NonNull HttpRequest request) {
    }

    @Override
    public void onResponseFinished(@NonNull HttpResponse response) {
    }
}
