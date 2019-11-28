package com.reactnative.capturetraffic;

import com.facebook.react.bridge.ReactApplicationContext;
import com.github.megatronking.netbare.NetBare;
import com.github.megatronking.netbare.ssl.JKS;


public class CaptureTraffic {

    private static final String TAG = CaptureTraffic.class.getSimpleName();

    private final ReactApplicationContext reactContext;
    private final JKS mJKS;
    private final Certificate mCertificate;
    private final NetBare mNetBare;

    public CaptureTraffic(ReactApplicationContext context, NetBare mNetBare, Certificate certificate){
        this.reactContext = context;
        this.mNetBare = mNetBare;
        this.mCertificate = certificate;

        mJKS = new JKS(context, certificate.getAlias(), certificate.getPassword().toCharArray(), certificate.getCommonName(), certificate.getOrganization(),
                certificate.getOrganizationalUnitName(), certificate.getCertOrganization(), certificate.getCertOrganizationalUnitName());
    }
}
