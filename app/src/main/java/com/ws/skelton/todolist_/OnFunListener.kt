package com.ws.skelton.todolist_

interface OnFunListener {

    fun onDeleteListener(memo: MemoEntity)

    fun onUpdateListener(memo: MemoEntity)
}