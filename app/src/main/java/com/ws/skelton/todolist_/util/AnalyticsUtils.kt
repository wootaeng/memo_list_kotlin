package com.ws.skelton.todolist_.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase

object AnalyticsUtils {


    fun sendScreen( screen: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen)
//        mTracker.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun setEvent(mTracker: FirebaseAnalytics, category: String?, action: String?) {
        val params = Bundle()
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category)
        params.putString(FirebaseAnalytics.Param.ITEM_ID, action)
        mTracker.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params)

    }
}