package com.ws.skelton.todolist_

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.byappsoft.huvleadlib.*
import com.byappsoft.sap.launcher.Sap_act_main_launcher
import com.byappsoft.sap.utils.Sap_Func
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.ws.skelton.todolist_.databinding.ActivityMainBinding

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() , OnFunListener{

    private lateinit var binding: ActivityMainBinding
    lateinit var db : MemoDatabase
    var memoList :List<MemoEntity> = listOf<MemoEntity>()

    //Ads
    lateinit var bav: BannerAdView
    lateinit var mAdView : AdView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //다크모드 미적용
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MemoDatabase.getInstance(this)!!

        binding.btnAdd.setOnClickListener{
            val memo = MemoEntity(null, binding.editTextMemo.text.toString())
            binding.editTextMemo.setText("")
            insertMemo(memo)
            binding.editTextMemo.requestFocus()
//            softkeyboardHide()
        }
//        binding.editTextMemo.setOnEditorActionListener { v, actionId, event ->
//            var handled = false
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                binding.editTextMemo.performClick()
//                handled = true
//            }
//            val memo = MemoEntity(null, binding.editTextMemo.text.toString())
//            binding.editTextMemo.setText("")
//            insertMemo(memo)
//            handled
//            binding.editTextMemo.requestFocus()
//        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(ItemDecorator(20))
        getAllMemos()

        setHuvleAD()
        setGoogleAD()

    }

    // 1. insert Data
    // 2. Get Data
    // 3. Delete Data

    // 4. Set RecyclerView

    override fun onResume() {
        super.onResume()
        // huvleView apply
        Sap_Func.setNotiBarLockScreen(this,false)
        Sap_act_main_launcher.initsapStart(this,"bynetwork",true,true)
    }



    fun insertMemo(memo : MemoEntity){
        //1. MainThread vs WorkerThread(Background Thread)
        // 모든 UI 관련 일들은 Main Thread 에서 진행
        // 모든 데이터 통신 관련 작업음 WorkerThred

        val insertTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                //workThread 에서 할 작업 정의
                db.memoDAO().insert(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }
        insertTask.execute()
    }


    fun getAllMemos(){
        val getTask = (object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                memoList = db.memoDAO().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }
        }).execute()
    }

    fun deleteMemo(memo: MemoEntity){
        val deleteTask = object : AsyncTask<Unit, Unit, Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }

        deleteTask.execute()
    }

    fun updateMemo(memo: MemoEntity){
        val updateTask = object : AsyncTask<Unit,Unit,Unit>(){
            override fun doInBackground(vararg p0: Unit?) {
                db.memoDAO().update(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }

        updateTask.execute()
    }

    fun setRecyclerView(memoList : List<MemoEntity>){

        binding.recyclerView.adapter = MyAdapter(this,memoList, this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }

    override fun onUpdateListener(memo: MemoEntity) {
        updateMemo(memo)
    }

    fun softkeyboardHide() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextMemo.windowToken, 0)
    }

    private fun setHuvleAD(){
        bav = binding.bannerView
        bav.setPlacementID("test") // 320*50 banner testID , 300*250 banner test ID "testbig"
        bav.setShouldServePSAs(false)
        bav.setClickThroughAction(ANClickThroughAction.OPEN_DEVICE_BROWSER)
        bav.setAdSize(320, 50) //bav.setAdSize(300, 250);
        // Resizes the container size to fit the banner ad
        bav.setResizeAdToFitContainer(true)
//        bav.setExpandsToFitScreenWidth(true)
        val adListener: AdListener = object : AdListener {
            override fun onAdRequestFailed(
                bav: com.byappsoft.huvleadlib.AdView,
                errorCode: ResultCode
            ) {
                if (errorCode == null) {
                    Log.v("HuvleBANNER", "Call to loadAd failed")
                } else {
                    Log.v("HuvleBANNER", "Ad request failed: $errorCode")
                }
//                setGoogleAD()
            }
            override fun onAdLoaded(ba: com.byappsoft.huvleadlib.AdView) {
                Log.v("HuvleBANNER", "The Ad Loaded!")
            }
            override fun onAdLoaded(nativeAdResponse: NativeAdResponse) {}
            override fun onAdExpanded(bav: com.byappsoft.huvleadlib.AdView) {}
            override fun onAdCollapsed(bav: com.byappsoft.huvleadlib.AdView) {}
            override fun onAdClicked(bav: com.byappsoft.huvleadlib.AdView) {}
            override fun onAdClicked(adView: com.byappsoft.huvleadlib.AdView, clickUrl: String) {}
            override fun onLazyAdLoaded(adView: com.byappsoft.huvleadlib.AdView) {}
        }
        bav.setAdListener(adListener)
        bav.init(this)
    }
    private fun setGoogleAD(){
        MobileAds.initialize(this) {}
        mAdView = binding.gadView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object: com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                Log.v("GoogleAD", "The Ad Loaded!")
            }
            override fun onAdFailedToLoad(adError : LoadAdError) {
                // TODO - Adknowva SDK Library
                bav.startAd()
                // TODO - Adknowva SDK Library
                Log.v("GoogleAD", "The Ad failed!")
            }
            override fun onAdOpened() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {}
        }
    }

}