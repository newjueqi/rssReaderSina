/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newjueqi.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 操作数据表Channels的类
 */

public class ChannelsDbAdapter {

	//数据库名称
    private static final String DATABASE_NAME = "data";
    
    //数据表名称
    private static final String DATABASE_TABLE = "Channels";
    //数据库版本
    private static final int DATABASE_VERSION = 2;

    /**
     * 数据表的4个字段
     */
    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LINK = "link";
    public static final String KEY_DESCRIPTION = "description";

    private final Context mCtx;
    private static final String TAG = "ChannelsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    /**
     * 创建数据表的SQL语句
     */
    private static final String DATABASE_CREATE =
		"CREATE TABLE " + DATABASE_TABLE + " ("
		+ KEY_ROWID + " INTEGER PRIMARY KEY,"
		+ KEY_TITLE + " TEXT," 
		+ KEY_LINK+ " TEXT," 
		+ KEY_DESCRIPTION + " TEXT" + ");";


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE );
            onCreate(db);
        }
    }

    /**
     * 传入数据库操作所需要的上下文对象
     * @param ctx 上下文
     */
    public ChannelsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * 打开数据库。如果不能打开数据库，尝试创建一个新的数据库。如果创建数据库失败，
     * 抛出SQLException异常信息
     * @return this (自引用, 可以创建一个链式)
     * @throws SQLException 如果数据库打开或创建失败就抛出异常
     */
    public ChannelsDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }


    /**
     * 根据标题，链接，描述信息插入一个新的频道记录。如果插入成功就返回记录的ID，
     * 否则返回—1表示创建失败
     * @param title 频道的标题
     * @param link 频道的链接
     * @param description 频道的描述信息
     * @return 如果成功就返回ID，失败就返回-1
     */
    public long createChannel(String title, String link, String description ) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_LINK, link);
        initialValues.put(KEY_DESCRIPTION, description);
        

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Delete the Channel with the given rowId
     * 根据给定的ID删除频道记录
     * 
     * @param rowId 需要删除的记录ID
     * @return boolean 删除成功true,删除失败false
     */
    public boolean deleteChannel(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * 返回一个指向结果集的游标
     * @return Cursor 返回一个指向结果集的游标
     */
    public Cursor fetchAllChannels() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_LINK,KEY_DESCRIPTION}, null, null, null, null, null);
    }

    /**
     * 返回一个指向特定行的游标
     * @param rowId 记录的ID号
     * @return 如果找到，返回一个指向特定行的游标
     * @throws SQLException 查找失败抛出异常信息
     */
    public Cursor fetchChannel(long rowId) throws SQLException {

        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                        KEY_TITLE, KEY_LINK, KEY_DESCRIPTION }, KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * 根据给定ID更新title，link，description 信息
     * @param rowId 需要更新的记录ID
     * @param title 更新的title
     * @param link 更新的link信息
     * @param description 更新的描述信息
     * @return boolean 更新成功返回true，否则返回false
     */
    public boolean updateChannel(long rowId, String title, 
    		String link, String description ) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_LINK, link);
        args.put(KEY_DESCRIPTION, description);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
