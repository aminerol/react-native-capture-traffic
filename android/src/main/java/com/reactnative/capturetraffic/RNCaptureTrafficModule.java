package com.reactnative.capturetraffic;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.github.megatronking.netbare.NetBare;

public class RNCaptureTrafficModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private static final String TAG = RNCaptureTrafficModule.class.getSimpleName();
  private final NetBare mNetBare;

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
}