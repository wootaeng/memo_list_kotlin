//package com.ws.skelton.todolist_
//
//import android.app.Application
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//
//class MemoRepository(private val memoDAO: MemoDAO) {
//
//    val searchDatas = MutableLiveData<List<MemoEntity>>()
//
//
//    suspend fun addMemo(memo: MemoEntity){
//        memoDAO.insert(memo)
//    }
//
//    suspend fun updateMemo(memo: MemoEntity){
//        memoDAO.update(memo)
//    }
//
//    suspend fun deleteMemo(memo: MemoEntity){
//        memoDAO.delete(memo)
//    }
//
//
//
//}