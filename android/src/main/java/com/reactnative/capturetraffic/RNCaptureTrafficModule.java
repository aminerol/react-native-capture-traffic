package com.reactnative.capturetraffic;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.github.megatronking.netbare.NetBare;
import com.github.megatronking.netbare.NetBareListener;

//import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeEventListener;
//import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeHolder;
//import com.walmartlabs.electrode.reactnative.bridge.EventListenerProcessor;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeHolder;
import com.walmartlabs.electrode.reactnative.bridge.EventProcessor;
import com.walmartlabs.electrode.reactnative.bridge.None;
import com.walmartlabs.electrode.reactnative.bridge.helpers.Logger;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeEvent;

public class RNCaptureTrafficModule extends ReactContextBaseJavaModule implements NetBareListener {

    private final ReactApplicationContext reactContext;

    private static final String TAG = RNCaptureTrafficModule.class.getSimpleName();
    private final NetBare mNetBare;
    private Certificate cert;
    private CaptureTraffic mCaptureTraffic;

    public RNCaptureTrafficModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        mNetBare = NetBare.get();
    }

    @Override
    public String getName() {
        return "RNCaptureTraffic";
    }

    @Override
    public void initialize() {
        super.initialize();

        Logger.overrideLogLevel(Logger.LogLevel.DEBUG);
        this.mNetBare.attachApplication(this.reactContext, false);
        this.mNetBare.registerNetBareListener(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        this.mNetBare.unregisterNetBareListener(this);
        this.mNetBare.stop();
        this.mCaptureTraffic.destroy();
    }

    @ReactMethod
    public void setCertificate(String mAlias, String mPassword, String mCommonName, String mOrganization, String mOrganizationalUnitName,
                               String mCertOrganization, String mCertOrganizationalUnitName) {
        this.cert = new Certificate()
                .setAlias(mAlias)
                .setPassword(mPassword)
                .setCommonName(mCommonName)
                .setOrganization(mOrganization)
                .setOrganizationalUnitName(mOrganizationalUnitName)
                .setCertOrganization(mCertOrganization)
                .setCertOrganizationalUnitName(mCertOrganizationalUnitName);
        mCaptureTraffic = new CaptureTraffic(this.reactContext, this.mNetBare, this.cert);

    }

    @ReactMethod
    public void installCertificate(final Promise promise){
        mCaptureTraffic.installCertificate(promise);
    }

    @ReactMethod
    public void startVpn(final Promise promise){
        mCaptureTraffic.startVpn(promise);
    }

    @ReactMethod
    public void stopVpn(final Promise promise){
        mCaptureTraffic.stopVpn(promise);
    }

    @ReactMethod
    public void isActive(final Promise promise){
        promise.resolve(mNetBare.isActive());
    }

    @Override
    public void onServiceStarted() {
        Utils.sendEvent(this.reactContext, "onServiceStarted", null);
        new EventProcessor<>("onServiceStarted", 1).execute();
    }

    @Override
    public void onServiceStopped() {
        Utils.sendEvent(this.reactContext, "onServiceStopped", null);
        new EventProcessor<>("onServiceStopped", 1).execute();
    }
}