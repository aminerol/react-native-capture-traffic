package com.reactnative.capturetraffic;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.github.megatronking.netbare.NetBare;
import com.github.megatronking.netbare.NetBareConfig;
import com.github.megatronking.netbare.http.HttpInjectInterceptor;
import com.github.megatronking.netbare.http.HttpInterceptorFactory;
import com.github.megatronking.netbare.ssl.JKS;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CaptureTraffic implements ActivityEventListener {


    private static final String TAG = CaptureTraffic.class.getSimpleName();
    private static final int REQUEST_CODE_PREPARE = 1;

    private final ReactApplicationContext reactContext;
    private final JKS mJKS;
    private final Certificate mCertificate;
    private final NetBare mNetBare;
    private Promise mVpnPromise;

    public CaptureTraffic(ReactApplicationContext context, NetBare mNetBare, Certificate certificate){
        this.reactContext = context;
        this.mNetBare = mNetBare;
        this.mCertificate = certificate;

        this.reactContext.addActivityEventListener(this);
        mJKS = new JKS(context, certificate.getAlias(), certificate.getPassword().toCharArray(), certificate.getCommonName(), certificate.getOrganization(),
                certificate.getOrganizationalUnitName(), certificate.getCertOrganization(), certificate.getCertOrganizationalUnitName());
    }

    public void destroy(){
        this.reactContext.removeActivityEventListener(this);
    }

    public void installCertificate(final Promise promise){
        if (!JKS.isInstalled(this.reactContext, mCertificate.getAlias())) {
            try {
                JKS.install(this.reactContext, mCertificate.getAlias(), mCertificate.getAlias());
                promise.resolve(true);
            } catch(IOException e) {
                promise.reject(e);
            }
        }else
            promise.resolve(false);
    }

    public void prepareVpn(){
        Intent intent = this.mNetBare.prepare();
        if (intent != null) {
            reactContext.startActivityForResult(intent, REQUEST_CODE_PREPARE, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PREPARE) {
            prepareVpn();
        }
    }

    public void startVpn(){
        this.mNetBare.start(NetBareConfig.defaultHttpConfig(mJKS, interceptorFactories()));
    }

    private List<HttpInterceptorFactory> interceptorFactories() {
        HttpInterceptorFactory injector1 = HttpInjectInterceptor.createFactory(new HttpInjector());
        return Arrays.asList(injector1);
    }
}
