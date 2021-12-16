package com.ws.skelton.todolist_.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.byappsoft.huvleadlib.ANClickThroughAction;
import com.byappsoft.huvleadlib.AdListener;
import com.byappsoft.huvleadlib.AdView;
import com.byappsoft.huvleadlib.BannerAdView;
import com.byappsoft.huvleadlib.NativeAdResponse;
import com.byappsoft.huvleadlib.ResultCode;
import com.byappsoft.sap.launcher.Sap_act_main_launcher;
import com.byappsoft.sap.utils.Sap_Func;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.ws.skelton.todolist_.fragment.MainFragment;
import com.ws.skelton.todolist_.vo.NoteDatabase;
import com.ws.skelton.todolist_.R;

public class TodoList extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private com.google.android.gms.ads.AdView mAdView;
    private boolean loadAd = true;
    private BannerAdView bav;
    Fragment mainFragment;
    EditText inputToDo;
    Context context;

    public static NoteDatabase noteDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //기기 다크모드 앱 반영 안되게 하기
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.todo_list);

        mainFragment = new MainFragment();

        //getSupportFragmentManager 을 이용하여 이전에 만들었던 **FrameLayout**에 `fragment_main.xml`이 추가
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

        Button saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                saveToDo();

                Toast.makeText(getApplicationContext(),"추가되었습니다. 드래그해서 확인하세요!",Toast.LENGTH_SHORT).show();

            }
        });

        Button moveBtn = findViewById(R.id.moveBtn);
        moveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodoList.this, AdTestActivity.class);
                startActivity(intent);

            }
        });




        openDatabase();

        setHuvleAD(); //허블 먼저
        bav.startAd();
//        setGoogleAD(); //구글 먼저


    }

    @Override
    public void onResume() {
        super.onResume();
        // huvleView apply
        Sap_Func.setNotiBarLockScreen(this, false);
        Sap_act_main_launcher.initsapStart(this, "bynetwork", true, true);


    }


    public void setGoogleAD() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mAdView = findViewById(R.id.gadView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override public void onAdLoaded() {
                // TODO - Adknowva SDK Library
                if (loadAd) {
                    loadAd = false;
                    bav.stopAd();
                }
                Log.v("GoogleAD", "The Ad Loaded!");
                // TODO - Adknowva SDK Library
            }
            @Override public void onAdFailedToLoad(LoadAdError adError) {
                // TODO - Adknowva SDK Library
                if (loadAd) {
                    loadAd = true;
                    bav.startAd();
                }
                // TODO - Adknowva SDK Library
                Log.v("GoogleAD", "The Ad failed!");
            }
            @Override public void onAdOpened() {}
            @Override public void onAdClicked() {}
            @Override public void onAdClosed() {}
        });
    }



    //AdKnowva
    private void setHuvleAD(){

//        //정적뷰
        bav = findViewById(R.id.banner_view);
//     "Z519z8m8q7"
        bav.setPlacementID("test"); // 320*50 banner testID , 300*250 banner test ID "testbig"
        // Turning this on so we always get an ad during testing.
        bav.setShouldServePSAs(false);
        // By default ad clicks open in an in-app WebView.
        bav.setClickThroughAction(ANClickThroughAction.OPEN_DEVICE_BROWSER);
        // Get a 300x250 ad.
//        bav.setAdSize(300, 250);
        bav.setAdSize(320, 50);
        // Resizes the container size to fit the banner ad
//        bav.setResizeAdToFitContainer(true);
        bav.setExpandsToFitScreenWidth(true);
        // Set up a listener on this ad view that logs events.
        AdListener adListener = new AdListener() {
            @Override
            public void onAdRequestFailed(AdView bav, ResultCode errorCode) {
                if (errorCode == null) {
                    Log.v("SIMPLEBANNER", "Call to loadAd failed");
                } else {
                    Log.v("SIMPLEBANNER", "Ad request failed: " + errorCode);
                }
//                setGoogleAD();
            }

            @Override
            public void onAdLoaded(AdView ba) {
                Log.v("SIMPLEBANNER", "The Ad Loaded!");
            }

            @Override
            public void onAdLoaded(NativeAdResponse nativeAdResponse) {
                Log.v("SIMPLEBANNER", "Ad onAdLoaded NativeAdResponse");
            }

            @Override
            public void onAdExpanded(AdView bav) {
                Log.v("SIMPLEBANNER", "Ad expanded");
            }

            @Override
            public void onAdCollapsed(AdView bav) {
                Log.v("SIMPLEBANNER", "Ad collapsed");
            }

            @Override
            public void onAdClicked(AdView bav) {
                Log.v("SIMPLEBANNER", "Ad clicked; opening browser");
            }

            @Override
            public void onAdClicked(AdView adView, String clickUrl) {
                Log.v("SIMPLEBANNER", "onAdClicked with click URL");
            }

            @Override
            public void onLazyAdLoaded(AdView adView) {

            }
        };
        bav.setAdListener(adListener);
        bav.init(this);
//        bav.startAd();

    }

    private void saveToDo(){
        inputToDo = findViewById(R.id.inputToDo);

        //EditText에 적힌 글을 가져오기
        String todo = inputToDo.getText().toString();

        //테이블에 값을 추가하는 sql구문 insert...
        String sqlSave = "insert into " + NoteDatabase.TABLE_NOTE + " (TODO) values (" +
                "'" + todo + "')";

        //sql문 실행
        NoteDatabase database = NoteDatabase.getInstance(context);
        database.execSQL(sqlSave);

        //저장과 동시에 EditText 안의 글 초기화
        inputToDo.setText("");
    }


    public void openDatabase() {
        // open database
        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }

        noteDatabase = NoteDatabase.getInstance(this);
        boolean isOpen = noteDatabase.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (noteDatabase != null) {
            noteDatabase.close();
            noteDatabase = null;
        }
    }
}
