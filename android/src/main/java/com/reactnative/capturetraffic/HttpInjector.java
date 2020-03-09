package com.reactnative.capturetraffic;

import android.net.Uri;
import android.os.Process;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.react.bridge.CallbackImpl;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.PromiseImpl;
import com.facebook.react.bridge.WritableMap;
import com.github.megatronking.netbare.http.HttpIndexedInterceptor;
import com.github.megatronking.netbare.http.HttpInjectInterceptor;
import com.github.megatronking.netbare.http.HttpInterceptor;
import com.github.megatronking.netbare.http.HttpInterceptorFactory;
import com.github.megatronking.netbare.http.HttpMethod;
import com.github.megatronking.netbare.http.HttpProtocol;
import com.github.megatronking.netbare.http.HttpRawBody;
import com.github.megatronking.netbare.http.HttpRequestChain;
import com.github.megatronking.netbare.http.HttpRequestInjectorCallback;
import com.github.megatronking.netbare.http.HttpResponseChain;
import com.github.megatronking.netbare.http.HttpResponseInjectorCallback;
import com.reactnative.capturetraffic.Encoding.Charsets;
import com.reactnative.capturetraffic.Encoding.StringUtils;
import com.reactnative.capturetraffic.models.Body;
import com.reactnative.capturetraffic.models.Header;
import com.reactnative.capturetraffic.models.Request;

import com.github.megatronking.netbare.http.HttpBody;
import com.github.megatronking.netbare.http.HttpRequest;
import com.github.megatronking.netbare.http.HttpRequestHeaderPart;
import com.github.megatronking.netbare.http.HttpResponse;
import com.github.megatronking.netbare.http.HttpResponseHeaderPart;
import com.github.megatronking.netbare.injector.InjectorCallback;

import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeResponseListener;
import com.walmartlabs.electrode.reactnative.bridge.EventProcessor;
import com.walmartlabs.electrode.reactnative.bridge.FailureMessage;
import com.walmartlabs.electrode.reactnative.bridge.RequestProcessor;
import com.walmartlabs.electrode.reactnative.bridge.helpers.Logger;


import static com.reactnative.capturetraffic.Utils.serializeHttpRequest;
import static com.reactnative.capturetraffic.Utils.buildHeader;

class HttpInjector extends HttpIndexedInterceptor {

    private final String TAG = HttpInjector.class.getSimpleName();
    private boolean mShouldInjectRequest;
    private boolean mShouldInjectResponse;

    @Override
    protected final void intercept(@NonNull final HttpRequestChain chain,
                                   @NonNull final ByteBuffer buffer, final int index) throws IOException {
        if (chain.request().uid() == Process.myUid()) {
            chain.process(buffer);
            return;
        }
        if (index == 0) {
            final Request req = serializeHttpRequest(chain.request());
            new RequestProcessor<>("shouldIntercept", req, Boolean.class, new ElectrodeBridgeResponseListener<Boolean>() {

                @Override
                public void onFailure(@NonNull FailureMessage failureMessage) {
                    Log.e(TAG, "Sending Request on shouldIntercept to JS Failed due to "+failureMessage.getMessage());
                    failureMessage.getException().printStackTrace();
                }

                @Override
                public void onSuccess(@Nullable Boolean shouldSniff){

                    try {

                        mShouldInjectRequest = shouldSniff;
                        if (!mShouldInjectRequest) {
                            chain.process(buffer);
                            return;
                        }

                        new RequestProcessor<>("onRequestHeaders", req, Request.class, new ElectrodeBridgeResponseListener<Request>() {
                            @Override
                            public void onSuccess(@Nullable Request responseData) {
                                try {
                                    new HttpRequestInjectorCallback(chain).onFinished(buildHeader(responseData));
                                } catch (IOException exception){
                                    exception.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull FailureMessage failureMessage) {
                                Log.e(TAG, "Sending Request on onRequestHeaders to JS Failed due to "+failureMessage.getMessage());
                                failureMessage.getException().printStackTrace();

                            }
                        }).execute();


                    } catch (IOException exception){
                        exception.printStackTrace();
                    }
                }
            }).execute();
        } else {
            final HttpBody httpBody = new HttpRawBody(buffer);
            final Body body = new Body.Builder(StringUtils.bufferToString(httpBody.toBuffer(), Charsets.DEFAULT)).build();
            new RequestProcessor<>("onRequestBody", body, Body.class, new ElectrodeBridgeResponseListener<Body>() {
                @Override
                public void onSuccess(@Nullable Body responseData) {
                    try {
                        new HttpRequestInjectorCallback(chain).onFinished(new HttpRawBody(StringUtils.stringToBuffer(body.getContent(), Charsets.DEFAULT)));
                    } catch (IOException exception){
                        exception.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull FailureMessage failureMessage) {
                    Log.e(TAG, "Sending Request on onRequestBody to JS Failed due to "+failureMessage.getMessage());
                    failureMessage.getException().printStackTrace();

                }
            }).execute();
        }

    }

    @Override
    protected final void intercept(@NonNull final HttpResponseChain chain,
                                   @NonNull ByteBuffer buffer, int index) throws IOException {
        if (chain.response().uid() == Process.myUid()) {
            chain.process(buffer);
            return;
        }
        if (index == 0) {
            mShouldInjectResponse = true;
        }
        if (!mShouldInjectResponse) {
            chain.process(buffer);
            return;
        }
        if (index == 0) {
            new HttpResponseInjectorCallback(chain).onFinished(buildHeader(chain.response()));
        } else {
            new HttpResponseInjectorCallback(chain).onFinished(new HttpRawBody(buffer));
        }
    }

    @Override
    public void onRequestFinished(@NonNull HttpRequest request) {
        super.onRequestFinished(request);
        if (mShouldInjectRequest) {
            Log.e(TAG, "onRequestFinished");
        }
        mShouldInjectRequest = false;
    }

    @Override
    public void onResponseFinished(@NonNull HttpResponse response) {
        super.onResponseFinished(response);
        if (mShouldInjectResponse) {
            Log.e(TAG, "onResponseFinished");
        }
        mShouldInjectResponse = false;
    }



    /**
     * A factory produces {@link HttpInjector} instance.
     *
     * @return An instance of {@link HttpInjector}.
     */
    public static HttpInterceptorFactory createFactory() {
        return new HttpInterceptorFactory() {

            @NonNull
            @Override
            public HttpInterceptor create() {
                return new HttpInjector();
            }

        };
    }


}
