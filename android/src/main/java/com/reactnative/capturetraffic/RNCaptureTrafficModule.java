package com.reactnative.capturetraffic;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.github.megatronking.netbare.NetBare;

public class RNCaptureTrafficModule extends ReactContextBaseJavaModule {

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
}