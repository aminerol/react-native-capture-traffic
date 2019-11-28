package com.reactnative.capturetraffic;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.github.megatronking.netbare.NetBare;
import com.github.megatronking.netbare.ssl.JKS;

import java.io.IOException;

public class CaptureTraffic implements ActivityEventListener {


    private static final String TAG = CaptureTraffic.class.getSimpleName();
    private static final int REQUEST_CODE_PREPARE = 1;

    private final ReactApplicationContext reactContext;
    private final JKS mJKS;
    private final Certificate mCertificate;
    private final NetBare mNetBare;

    public CaptureTraffic(ReactApplicationContext context, NetBare mNetBare, Certificate certificate){
        this.reactContext = context;
        this.mNetBare = mNetBare;
        this.mCertificate = certificate;

        this.reactContext.addActivityEventListener(this);
        mJKS = new JKS(context, certificate.getAlias(), certificate.getPassword().toCharArray(), certificate.getCommonName(), certificate.getOrganization(),
                certificate.getOrganizationalUnitName(), certificate.getCertOrganization(), certificate.getCertOrganizationalUnitName());
    }

    public boolean installCertificate(){
        if (!JKS.isInstalled(this.reactContext, mCertificate.getAlias())) {
            try {
                JKS.install(this.reactContext, mCertificate.getAlias(), mCertificate.getAlias());
                return true;
            } catch(IOException e) {
                // a callback error
                return false;
            }
        }else
            return true;
    }

    public void prepareVpn(){
        Intent intent = this.mNetBare.prepare();
        if (intent != null) {
            reactContext.startActivityForResult(intent, REQUEST_CODE_PREPARE, null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PREPARE) {
            prepareVpn();
        }
    }
}
