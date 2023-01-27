package com.ws.skelton.todolist_.view


import android.annotation.SuppressLint
import android.content.Context
import android.os.*
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.util.CollectionUtils.listOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kakao.adfit.ads.ba.BannerAdView
import com.ws.skelton.todolist_.BuildConfig.ADFIT_KEY
import com.ws.skelton.todolist_.MyAdapter
import com.ws.skelton.todolist_.databinding.ActivityMainBinding
import com.ws.skelton.todolist_.room.MemoDatabase
import com.ws.skelton.todolist_.room.MemoEntity
import com.ws.skelton.todolist_.util.AnalyticsUtils
import com.ws.skelton.todolist_.util.ItemDecorator
import com.ws.skelton.todolist_.util.OnFunListener
import java.util.*


@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity() , OnFunListener {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    lateinit var db : MemoDatabase
    var memoList :List<MemoEntity> = listOf<MemoEntity>()

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    //Ads
    lateinit var mAdView : com.google.android.gms.ads.AdView
    var mAdfitView: BannerAdView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //다크모드 미적용
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        AnalyticsUtils.sendScreen("Main")

//        launchInterstitialAD()


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

//        binding.moveBtn.setOnClickListener {
//            val intent = Intent(this, AdTestActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(ItemDecorator(20))
        getAllMemos()

        setGoogleAD()


    }

    // 1. insert Data
    // 2. Get Data
    // 3. Delete Data

    // 4. Set RecyclerView

    override fun onResume() {
        super.onResume()
        mAdView.resume()
        mAdfitView?.resume()


    }



    override fun onDestroy() {
        super.onDestroy()
        mAdView.destroy()
        mAdfitView?.destroy()

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

//    fun updateMemo(memo: MemoEntity){
//        val updateTask = object : AsyncTask<Unit,Unit,Unit>(){
//            override fun doInBackground(vararg p0: Unit?) {
//                db.memoDAO().update(memo)
//            }
//
//            override fun onPostExecute(result: Unit?) {
//                super.onPostExecute(result)
//                getAllMemos()
//            }
//        }
//
//        updateTask.execute()
//    }

    fun setRecyclerView(memoList : List<MemoEntity>){

        binding.recyclerView.adapter = MyAdapter(this,memoList, this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }

//    override fun onUpdateListener(memo: MemoEntity) {
//        updateMemo(memo)
//    }


    fun softkeyboardHide() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editTextMemo.windowToken, 0)
    }

//    private val mDelayHandler: Handler by lazy {
//        Handler()
//    }


//    private fun hideAdView() {
//        if(mAdView != null) {
//            mAdView.visibility = View.GONE
//        }
//        if (mAdfitView != null) {
//            mAdfitView.visibility = View.GONE
//        }
//    }

    private fun setGoogleAD(){
        MobileAds.initialize(this) {}
        mAdView = binding.gadView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        mAdView.adListener = object: com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                Log.e("GoogleAD", "The Ad Loaded!")
                mAdView.visibility = View.VISIBLE

            }
            override fun onAdFailedToLoad(adError : LoadAdError) {
                mAdView.visibility = View.GONE
                setAdfit()
                val responseInfo = adError.responseInfo
                Log.e("GoogleAd", responseInfo.toString())

            }
            override fun onAdOpened() {}
            override fun onAdClicked() {}
            override fun onAdClosed() {}
        }
    }

    private fun setAdfit(){
        mAdfitView = binding.adView // 배너 광고 뷰
        mAdfitView!!.setClientId(ADFIT_KEY)  // 광고단위 ID 설정
        mAdfitView!!.setAdListener(object  : com.kakao.adfit.ads.AdListener{
            override fun onAdLoaded() {
                // 배너 광고 노출 완료 시 호출
                Log.e("Adfit Banner","Adfit loaded")
                mAdfitView!!.visibility = View.VISIBLE
                AnalyticsUtils.setEvent(firebaseAnalytics,"adfit","adLoad")
            }

            override fun onAdFailed(errorCode: Int) {
                // 배너 광고 노출 실패 시 호출
                Log.e("Adfit Banner","Failed to load banner :: errorCode = $errorCode")
            }

            override fun onAdClicked() {
                // 배너 광고 클릭 시 호출
                Log.e("Adfit Banner","Banner is clicked")
            }
        })
        mAdfitView!!.loadAd()
    }


}


