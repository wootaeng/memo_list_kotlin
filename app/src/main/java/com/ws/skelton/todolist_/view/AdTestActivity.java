//package com.ws.skelton.todolist_.view;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//
//import com.onnuridmc.exelbid.ExelBidAdView;
//import com.onnuridmc.exelbid.ExelBidNative;
//import com.onnuridmc.exelbid.common.ExelBidError;
//import com.onnuridmc.exelbid.common.NativeAsset;
//import com.onnuridmc.exelbid.common.NativeViewBinder;
//import com.onnuridmc.exelbid.common.OnAdNativeListener;
//import com.ws.skelton.todolist_.R;
//
//public class AdTestActivity extends AppCompatActivity {
//
//    private String mUnitId ="858eea69eb7f15f9f049152208668af9dc33832a";
//    private View mNativeRootLayout;
//    private ExelBidNative mNativeAd;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//
//        setExel();
//    }
//
//
//    private void setExel(){
//
//
//        mNativeAd = new ExelBidNative(this, mUnitId, new OnAdNativeListener() {
//            @Override
//            public void onFailed(ExelBidError error) {
//                Log.v("ExcelNative", "Call to loadAd failed");
//            }
//            @Override
//            public void onShow() {
//            }
//            @Override
//            public void onClick() {
//            }
//            @Override
//            public void onLoaded() {
//                Log.v("ExcelNative", "The Ad Loaded!");
//                mNativeRootLayout.setVisibility(View.VISIBLE);
//                mNativeAd.show();
//            }
//        });
//
//        mNativeRootLayout = findViewById(R.id.native_layout);
//        mNativeAd.setNativeViewBinder(new NativeViewBinder.Builder(mNativeRootLayout)
//                .mainImageId(R.id.native_main_image)
//                .callToActionButtonId(R.id.native_cta)
//                .titleTextViewId(R.id.native_title)
//                .textTextViewId(R.id.native_text)
//                .iconImageId(R.id.native_icon_image)
//                .adInfoImageId(R.id.native_privacy_information_icon_image)
//                .build());
//
//        mNativeAd.setRequiredAsset(new NativeAsset[] {NativeAsset.TITLE, NativeAsset.CTATEXT, NativeAsset.ICON, NativeAsset.MAINIMAGE, NativeAsset.DESC});
//        mNativeAd.loadAd();
//    }
//
//
//
//
//
//
//}
