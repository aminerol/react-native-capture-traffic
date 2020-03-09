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

    @Override
    public void onNewIntent(Intent intent){

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == activity.RESULT_OK && requestCode == REQUEST_CODE_PREPARE) {
            this.startVpn(mVpnPromise);
        }else{
            mVpnPromise.reject("E_VPN_NOT_READY", "VPN Connection Cancelled By user");
        }
    }

    public void startVpn(final Promise promise){
        mVpnPromise = promise;
        try{
            if(!this.mNetBare.isActive()){
                Intent intent = this.mNetBare.prepare();
                if (intent != null) {
                    reactContext.startActivityForResult(intent, REQUEST_CODE_PREPARE, null);
                    return;
                }
                this.mNetBare.start(NetBareConfig.defaultHttpConfig(mJKS, interceptorFactories()));
            }
            promise.resolve(null);
        }catch(Exception e){
            mVpnPromise.reject(e);
        }
    }

    public void stopVpn(final Promise promise){
        try{
            if(this.mNetBare.isActive())
                this.mNetBare.stop();
            promise.resolve(null);
        }catch(Exception e){
            mVpnPromise.reject(e);
        }
    }

    private List<HttpInterceptorFactory> interceptorFactories() {
        HttpInterceptorFactory injector1 = HttpInjector.createFactory();
        HttpInterceptorFactory testInjector = HttpInjectInterceptor.createFactory(new TestInjector());
        return Arrays.asList(injector1, testInjector);
    }
}
