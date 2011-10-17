package com.mycreat.profilemanager.dao;

import static android.provider.BaseColumns._ID;
import static com.mycreat.profilemanager.db.ProfileTable.TB_NAME;

import java.util.ArrayList;

import com.mycreat.profilemanager.data.Profile;
import com.mycreat.profilemanager.db.ProfileTable;
import com.mycreat.profilemanager.db.ProfileColumns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProfileDao extends SQLiteOpenHelper {
	
	private static String TAG = "ProfileDao";
	
	private static final String DB_NAME = "profile.db";  //���ݿ�����
	private static final int DB_VERSION = 1;
	
	SQLiteDatabase db;
	
	public ProfileDao(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TB_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ProfileTable.NAME + " TEXT NOT NULL," + ProfileTable.PIC + " INTEGER," + ProfileTable.AUDIO_RING + " INTEGER," + ProfileTable.AUDIO_CALL + " INTEGER," + ProfileTable.AUDIO_ALARM + " INTEGER," + ProfileTable.AUDIO_MUSIC + " INTEGER," + ProfileTable.VIBRATOR + " INTEGER," + ProfileTable.AIRPLANE + " INTEGER," + ProfileTable.WIFI + " INTEGER," + ProfileTable.BLUETOOTH + " INTEGER," + ProfileTable.CREATETIME + " LONG," + ProfileTable.UPDATETIME + " LONG," + ProfileTable.ISACTIVE + " INTEGER," + ProfileTable.USED + " INTEGER)";
		//Log.i(TAG, "ִ�н���sql��" + sql);
		db.execSQL(sql);  //�������SQL���
		
		ContentValues values = new ContentValues();
		values.put("name", "����"); values.put("pic", 9);
		values.put("audioRing", 5); values.put("audioCall", 4); 
		values.put("audioAlarm", 5); values.put("audioMusic", 10);
		values.put("vibrator", 1); values.put("airplane", 1);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		
		values = new ContentValues();
		values.put("name", "˯��"); values.put("pic", 7);
		values.put("audioRing", 0); values.put("audioCall", 2); 
		values.put("audioAlarm", 5); values.put("audioMusic", 3);
		values.put("vibrator", 1); values.put("airplane", 0);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		
		values = new ContentValues();
		values.put("name", "����"); values.put("pic", 5);
		values.put("audioRing", 7); values.put("audioCall", 5); 
		values.put("audioAlarm", 7); values.put("audioMusic", 15);
		values.put("vibrator", 1); values.put("airplane", 0);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		
		/*
		values = new ContentValues();
		values.put("name", "��ͥ"); values.put("pic", 4);
		values.put("audioRing", 3); values.put("audioCall", 3); 
		values.put("audioAlarm", 3); values.put("audioMusic", 6);
		values.put("vibrator", 2); values.put("airplane", 0);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		*/
		
		values = new ContentValues();
		values.put("name", "����"); values.put("pic", 11);
		values.put("audioRing", 0); values.put("audioCall", 2); 
		values.put("audioAlarm", 0); values.put("audioMusic", 0);
		values.put("vibrator", 1); values.put("airplane", 0);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		
		values = new ContentValues();
		values.put("name", "����"); values.put("pic", 2);
		values.put("audioRing", 0); values.put("audioCall", 0); 
		values.put("audioAlarm", 0); values.put("audioMusic", 0);
		values.put("vibrator", 2); values.put("airplane", 0);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		
		values = new ContentValues();
		values.put("name", "��׼"); values.put("pic", 1);
		values.put("audioRing", 5); values.put("audioCall", 4); 
		values.put("audioAlarm", 5); values.put("audioMusic", 12);
		values.put("vibrator", 1); values.put("airplane", 0);
		values.put("wifi", 0); values.put("bluetooth", 0);
		values.put("isActive", 0); values.put("createTime", System.currentTimeMillis());
		values.put("updateTime", System.currentTimeMillis()); values.put("used", 0);
		db.insert(TB_NAME, null, values);
		
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);  //ɾ���ɱ�
		onCreate(db);  //���´�����
	}
	
	/**
	 * ��id���Profile����
	 * @param id
	 * @return
	 */
	public Profile getProfileById(int id) {
		db = getReadableDatabase();
		Profile profile = new Profile();
		String selection = "_id=?";
		String[] selectionArgs = {String.valueOf(id)};
		Cursor c = db.query(TB_NAME, null, selection, selectionArgs, null, null, null);
		if (c != null && c.moveToFirst()) {
			profile.setId(id);
			profile.setPic(c.getInt(ProfileColumns.PROFILE_COL_PIC));
			profile.setName(c.getString(ProfileColumns.PROFILE_COL_NAME));
			profile.setAudioRing(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_RING));
			profile.setAudioCall(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_CALL));
			profile.setAudioAlarm(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_ALARM));
			profile.setAudioMusic(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_MUSIC));
			profile.setVibrator(c.getInt(ProfileColumns.PROFILE_COL_VIBRATOR));
			profile.setAirplane(c.getInt(ProfileColumns.PROFILE_COL_AIRPLANE));
			profile.setWifi(c.getInt(ProfileColumns.PROFILE_COL_WIFI));
			profile.setBluetooth(c.getInt(ProfileColumns.PROFILE_COL_BLUETOOTH));
			profile.setActive(c.getInt(ProfileColumns.PROFILE_COL_ISACTIVE) == 1 ? true : false);
		}
		c.close();
		db.close();
		return profile;
	}
	
	/**
	 * ����
	 * @param values
	 */
	public void insertProfile(ContentValues values) {
		db = getWritableDatabase();
		values.put("used", 0);
		if (values.getAsInteger("isActive") == 1) resetActive();
		db.insert(TB_NAME, null, values);
		db.close();
	}
	
	/**
	 * �޸�
	 * @param values
	 * @param id
	 */
	public void updateProfile(ContentValues values, int id) {
		db = getWritableDatabase();
		if (values.getAsInteger("isActive") == 1) resetActive();
		String whereClause = "_id=?";
		String[] whereArgs = {String.valueOf(id)};
		db.update(TB_NAME, values, whereClause, whereArgs);
		db.close();
	}
	
	/**
	 * ɾ��
	 * @param id
	 */
	public void deleteProfile(int id) {
		db = getWritableDatabase();
		String whereClause = "_id=?";
		String[] whereArgs = {String.valueOf(id)};
		db.delete(TB_NAME, whereClause, whereArgs);
		db.close();
	}
	
	/**
	 * ����龰ģʽ�б�
	 * @param sOrderBy
	 * @return
	 */
	public ArrayList<Profile> getProfileList(String sOrderBy) {
		db = getReadableDatabase();
		String orderBy = null;
		if (sOrderBy != null) orderBy = sOrderBy;
		Cursor c = db.query(TB_NAME, null, null, null, null, null, orderBy);
		ArrayList<Profile> profileList = new ArrayList<Profile>();
		if (c != null && c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				int id = c.getInt(ProfileColumns.PROFILE_COL_ID);
				int isActive = c.getInt(ProfileColumns.PROFILE_COL_ISACTIVE);
				final String name = c.getString(ProfileColumns.PROFILE_COL_NAME);
				Profile profile = new Profile();
				profile.setId(id);
				profile.setPic(c.getInt(ProfileColumns.PROFILE_COL_PIC));
				profile.setName(name);
				profile.setAudioRing(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_RING));
				profile.setAudioCall(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_CALL));
				profile.setAudioAlarm(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_ALARM));
				profile.setAudioMusic(c.getInt(ProfileColumns.PROFILE_COL_AUDIO_MUSIC));
				profile.setVibrator(c.getInt(ProfileColumns.PROFILE_COL_VIBRATOR));
				profile.setActive(isActive == 1 ? true : false);
				profileList.add(profile);
				c.moveToNext();
			}
		}
		//Log.i(TAG, "��ѯ����������" + profileList.size() + "��");
		return profileList;
	}
	
	/**
	 * ����龰ģʽ�α�
	 * @param sOrderBy ����ʽ
	 * @return
	 */
	public Cursor getProfileListCursor(String sOrderBy) {
		db = getReadableDatabase();
		String orderBy = null;
		if (sOrderBy != null) orderBy = sOrderBy;
		Cursor c = db.query(TB_NAME, null, null, null, null, null, orderBy);
		//db.close();
		return c;
	}
	
	/**
	 * ���ģʽ
	 * @return
	 */
	public boolean resetActive() {
		try {
			db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("isActive", 0);
			db.update(TB_NAME, values, null, null);
			//db.close();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * ����ģʽ
	 * @param new_id
	 * @param old_id
	 * @return
	 */
	public boolean enableActive(int new_id, int old_id) {
		try {
			db = getWritableDatabase();
			if (old_id != -1 || new_id != old_id) {  //���֮ǰ��״̬
				ContentValues values = new ContentValues();
				values.put("isActive", 0);
				String whereClause = "_id=?";
				String[] whereArgs = {String.valueOf(old_id)};
				db.update(TB_NAME, values, whereClause, whereArgs);
			}
			
			int num = 0;
			Cursor c = db.query(TB_NAME, new String[]{"used"}, "_id=?", new String[]{String.valueOf(new_id)}, null, null, null);
			if (c != null && c.moveToFirst()) 
				num = c.getInt(0) + 1;
			c.close();
			
			//Log.i(TAG, "ʹ�ô�����" + num);
			
			ContentValues values = new ContentValues();
			values.put("isActive", 1);
			if (num != 0) values.put("used", num);
			String whereClause = "_id=?";
			String[] whereArgs = {String.valueOf(new_id)};
			db.update(TB_NAME, values, whereClause, whereArgs);
			db.close();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * ����ģʽ
	 * @param new_id
	 * @param old_id
	 * @param isActive
	 * @return
	 */
	public boolean disableActive(int id) {
		try {
			db = getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("isActive", 0);
			String whereClause = "_id=?";
			String[] whereArgs = {String.valueOf(id)};
			db.update(TB_NAME, values, whereClause, whereArgs);
			db.close();
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * ��õ�ǰģʽId
	 * @return
	 */
	public int getActiveId() {
		int id = -1;
		db = getReadableDatabase();
		String selection = "isActive=?";
		String[] selectionArgs = {"1"};
		Cursor c = db.query(TB_NAME, null, selection, selectionArgs, null, null, null);
		if (c != null && c.moveToFirst()) {
			id = c.getInt(ProfileColumns.PROFILE_COL_ID);
		}
		//Log.i(TAG, "��ǰ����ģʽId" + id);
		db.close();
		return id;
	}
}
