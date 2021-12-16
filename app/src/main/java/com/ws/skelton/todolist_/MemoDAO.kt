package com.ws.skelton.todolist_

import androidx.room.*

@Dao
interface MemoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //insert 시 프라이머리키 동일시 덮어 씌운다
    fun insert(memo : MemoEntity)

    @Query("SELECT * FROM memo")
    fun getAll() : List<MemoEntity>


    @Delete
    fun delete(memo: MemoEntity)

    @Update
    fun update(memo: MemoEntity)

}