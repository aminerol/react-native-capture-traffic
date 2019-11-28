package com.reactnative.capturetraffic;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.github.megatronking.netbare.NetBare;
import com.github.megatronking.netbare.NetBareListener;

public class RNCaptureTrafficModule extends ReactContextBaseJavaModule implements NetBareListener {

  private final ReactApplicationContext reactContext;

  private static final String TAG = RNCaptureTrafficModule.class.getSimpleName();
  private final NetBare mNetBare;
  private CaptureTraffic mCaptureTraffic;

  public RNCaptureTrafficModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    mNetBare = NetBare.get();
  }

  @Override
  public String getName() {
    return TAG;
  }

  @Override
  public void initialize() {
    super.initialize();

    this.mNetBare.attachApplication(this.reactContext, BuildConfig.DEBUG);
    this.mNetBare.registerNetBareListener(this);
  }

  @Override
  public void onCatalystInstanceDestroy() {
    super.onCatalystInstanceDestroy();
    this.mNetBare.unregisterNetBareListener(this);
    this.mNetBare.stop();
    this.mCaptureTraffic.destroy();
  }

  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  @ReactMethod
  public void setCertificate(String mAlias, String mPassword, String mCommonName, String mOrganization, String mOrganizationalUnitName,
                             String mCertOrganization, String mCertOrganizationalUnitName) {
    Certificate cert = new Certificate()
            .setAlias(mAlias)
            .setPassword(mPassword)
            .setCommonName(mCommonName)
            .setOrganization(mOrganization)
            .setOrganizationalUnitName(mOrganizationalUnitName)
            .setCertOrganization(mCertOrganization)
            .setCertOrganizationalUnitName(mCertOrganizationalUnitName);
    mCaptureTraffic = new CaptureTraffic(this.reactContext, this.mNetBare, cert);
  }

  @ReactMethod
  public void installCertificate(Callback success, Callback error){
    mCaptureTraffic.installCertificate(success, error);
  }

  @ReactMethod
  public void prepareVpn(){
    mCaptureTraffic.prepareVpn();
  }

  @ReactMethod
  public void startVpn(){
    mCaptureTraffic.startVpn();
  }

  @Override
  public void onServiceStarted() {
    sendEvent(this.reactContext, "onServiceStarted", null);
  }

  @Override
  public void onServiceStopped() {
    sendEvent(this.reactContext, "onServiceStopped", null);
  }
}