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
 * �������ݱ�Channels����
 */

public class ChannelsDbAdapter {

	//���ݿ�����
    private static final String DATABASE_NAME = "data";
    
    //���ݱ�����
    private static final String DATABASE_TABLE = "Channels";
    //���ݿ�汾
    private static final int DATABASE_VERSION = 2;

    /**
     * ���ݱ��4���ֶ�
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
     * �������ݱ��SQL���
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
     * �������ݿ��������Ҫ�������Ķ���
     * @param ctx ������
     */
    public ChannelsDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * �����ݿ⡣������ܴ����ݿ⣬���Դ���һ���µ����ݿ⡣����������ݿ�ʧ�ܣ�
     * �׳�SQLException�쳣��Ϣ
     * @return this (������, ���Դ���һ����ʽ)
     * @throws SQLException ������ݿ�򿪻򴴽�ʧ�ܾ��׳��쳣
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
     * ���ݱ��⣬���ӣ�������Ϣ����һ���µ�Ƶ����¼���������ɹ��ͷ��ؼ�¼��ID��
     * ���򷵻ء�1��ʾ����ʧ��
     * @param title Ƶ���ı���
     * @param link Ƶ��������
     * @param description Ƶ����������Ϣ
     * @return ����ɹ��ͷ���ID��ʧ�ܾͷ���-1
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
     * ���ݸ�����IDɾ��Ƶ����¼
     * 
     * @param rowId ��Ҫɾ���ļ�¼ID
     * @return boolean ɾ���ɹ�true,ɾ��ʧ��false
     */
    public boolean deleteChannel(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * ����һ��ָ���������α�
     * @return Cursor ����һ��ָ���������α�
     */
    public Cursor fetchAllChannels() {

        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
                KEY_LINK,KEY_DESCRIPTION}, null, null, null, null, null);
    }

    /**
     * ����һ��ָ���ض��е��α�
     * @param rowId ��¼��ID��
     * @return ����ҵ�������һ��ָ���ض��е��α�
     * @throws SQLException ����ʧ���׳��쳣��Ϣ
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
     * ���ݸ���ID����title��link��description ��Ϣ
     * @param rowId ��Ҫ���µļ�¼ID
     * @param title ���µ�title
     * @param link ���µ�link��Ϣ
     * @param description ���µ�������Ϣ
     * @return boolean ���³ɹ�����true�����򷵻�false
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
