package com.mycreat.profilemanager.activity;

import java.util.ArrayList;
import java.util.List;

import com.mycreat.profilemanager.R;
import com.mycreat.profilemanager.dao.ProfileDao;
import com.mycreat.profilemanager.data.Profile;
import com.mycreat.profilemanager.service.ProfileService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.mycreat.profilemanager.db.ProfileColumns;

import com.mobclick.android.MobclickAgent;

public class ProfileListActivity extends Activity {

	private static String TAG = "ProfileListActivity";
	
	public static final int ACTIVITY_PROFILE = 0;
	public static final int ACTIVITY_PREF = 1;
	
	ListView lvProfileList;
	ListView lvProfileList2;
	TextView tvMsgName;
	RelativeLayout relativeMsg;
	ArrayAdapter<String> adapter;
	ArrayList<Profile> profileList;
	boolean isFirstShow = true;
	int clickPosition = -1;  //��ǰѡ��������
	int curId = -1;  //��ǰ�龰ģʽId
	ProfileDao profileDao;
	Prefs prefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_list_activity);
		//Log.i(TAG, "[��������]===>onCreate");
		initPrefs();
		isFirstShow = true;
		profileDao = new ProfileDao(this);
		initView();
		getProfileList(false);
	}
	
	/**
	 * ��ʼ��ѡ��
	 */
	private void initPrefs() {
		prefs = new Prefs();
		SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);  
		prefs.setOrder(sPref.getString("order", "createTime DESC"));
		prefs.setNotif(sPref.getBoolean("notif", true));
		prefs.setSound(Integer.valueOf(sPref.getString("sound", "2")));
	}
	
	/**
	 * ��ʼ��View
	 */
	public void initView() {
		tvMsgName = (TextView)findViewById(R.id.tvMsgName);
		relativeMsg = (RelativeLayout)findViewById(R.id.relativeMsg);
		((ImageButton)findViewById(R.id.imgbtnAdd)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProfileActivity(-1);  //�½�
			}
		});
	}
	
	/**
	 * ��ȡ�龰ģʽ�б�
	 */
	public void getProfileList(boolean isChange) {
		profileList = new ArrayList<Profile>();
		clickPosition = -1;
		lvProfileList = (ListView)findViewById(R.id.profileList);
		
		Cursor c = profileDao.getProfileListCursor(prefs.getOrder());
		
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
				
				profile.setAirplane(c.getInt(ProfileColumns.PROFILE_COL_AIRPLANE));
				profile.setWifi(c.getInt(ProfileColumns.PROFILE_COL_WIFI));
				profile.setBluetooth(c.getInt(ProfileColumns.PROFILE_COL_BLUETOOTH));
				
				profile.setActive(isActive == 1 ? true : false);
				profileList.add(profile);
				if (isActive == 1) {
					curId = id;
					enableActive(id, name, true, isChange);
				}
				c.moveToNext();
			}
		}
		CursorAdapter adapter = new myCursorAdapter(this, c);
		lvProfileList.setAdapter(adapter);
		
		if (curId == -1) relativeMsg.setVisibility(View.GONE);
		
		// ����¼�
		lvProfileList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				//Log.i(TAG, "[λ��]" + position);
				clickPosition = position;
				if (curId != profileList.get(clickPosition).getId()) 
					enableActive(profileList.get(clickPosition).getId(), profileList.get(clickPosition).getName(), true, true);
			}
		});
		
		lvProfileList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
				//Log.i(TAG, "[λ��]" + position);
				clickPosition = position;
				String[] items = null;
				if (curId == profileList.get(clickPosition).getId()) items = new String[]{"����", "�༭", "ɾ��"};
				else items = new String[]{"����", "�༭", "ɾ��"};
				AlertDialog.Builder builder = new AlertDialog.Builder(ProfileListActivity.this);
				builder.setTitle(profileList.get(position).getName()).setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						switch (i) {
						case 0:
							if (curId == profileList.get(clickPosition).getId()) disableActive(profileList.get(clickPosition).getId(), profileList.get(clickPosition).getName(), true);
							else enableActive(profileList.get(clickPosition).getId(), profileList.get(clickPosition).getName(), true, true);
							break;
						case 1:
							showProfileActivity(profileList.get(clickPosition).getId());  //�༭
							break;
						case 2:
							if (curId == profileList.get(clickPosition).getId()) {
								AlertDialog.Builder builder = new AlertDialog.Builder(ProfileListActivity.this);
								builder.setTitle("ɾ��").setIcon(R.drawable.ic_dialog_alert).setMessage(profileList.get(clickPosition).getName() + " ��ǰ����������״̬�����Ƚ��ú���ɾ����").setNegativeButton("����", null);
								AlertDialog ad = builder.create();
								ad.show();
							}
							else {
								AlertDialog.Builder builder = new AlertDialog.Builder(ProfileListActivity.this);
								builder.setTitle("ɾ��").setMessage("ȷ��ɾ�� " + profileList.get(clickPosition).getName() + " ��").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int arg1) {
										profileDao.deleteProfile(profileList.get(clickPosition).getId());
										getProfileList(false);
									}
								}).setNegativeButton("ȡ��", null);
								AlertDialog ad = builder.create();
								ad.show();
							}
							break;
						}
					}
				}).setNegativeButton("ȡ��", null);
				AlertDialog ad = builder.create();
				ad.show();
				return true;
			}
		});
	}
	
	/**
	 * �����龰ģʽ
	 * @param id �龰ģʽId
	 * @param name �龰ģʽ����
	 * @param showToast �Ƿ���ʾ��ʾ��Ϣ
	 */
	public void enableActive(int id, String name, boolean showToast, boolean isChange) {
		//Log.i(TAG, "[���÷���]׼���������񣡷���Id��" + id);
		if (!profileDao.enableActive(id, curId)) return;
		else curId = id;
		if (startService(curId, isChange)) {
			//Log.i(TAG, "[���÷���]�������ɹ�������Id��" + id);
			//Log.i(TAG, "[���÷���]��ϸ��Ϣ===>Id��" + id + "�����ƣ�" + name + "���滻Id��"+ curId);
			//if (showToast) showToast("������ " + name, Toast.LENGTH_SHORT);
		}
		
		tvMsgName.setText(name);
		//imgbtnMsgEdit
		((RelativeLayout)findViewById(R.id.relativeMsg)).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showProfileActivity(curId);
					}
				});
		relativeMsg.setVisibility(View.VISIBLE);
		//Log.i(TAG, "[���÷���]��ɣ�");
	}
	
	/**
	 * �����龰ģʽ
	 * @param id
	 * @param name
	 * @param showToast �Ƿ���ʾ��ʾ��Ϣ
	 */
	public void disableActive(int id, String name, boolean showToast) {
		if (profileDao.disableActive(curId)) {
			//Log.i(TAG, "[����ģʽ]===>Id��" + id + "�����ƣ�" + name + "���滻Id��" + curId);
			curId = -1;
			//if (showToast) showToast("�ѽ��� " + name, Toast.LENGTH_SHORT);
			relativeMsg.setVisibility(View.GONE);
			stopService();
		}
	}
	
	/**
	 * ��ʾ��ʾ��
	 * @param text
	 * @param time
	 */
	public void showToast(String text, int time) {
		Toast toastActive = Toast.makeText(getApplicationContext(), text, time);
		toastActive.show();
	}
	
	/**
	 * �༭ģʽ
	 * @param id
	 */
	public void showProfileActivity(int id) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		if (id != -1) {
			bundle.putInt("id", id);
		}
		intent.putExtras(bundle);
        intent.setClass(ProfileListActivity.this, ProfileActivity.class);
        //Log.i(TAG, "[��ת�༭]===>Id��" + id);
        startActivityForResult(intent, ACTIVITY_PROFILE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_CANCELED) {
			if (data.getExtras().getBoolean("needChange")) getProfileList(true);
			else getProfileList(false);
        }
		else if(requestCode == ACTIVITY_PREF)  
        {  
			SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
			//Log.i(TAG, sPref.getString("order", "createTime DESC") + "   " + prefs.getOrder());
            if (!prefs.getOrder().equals(sPref.getString("order", "createTime DESC"))) {
            	prefs.setOrder(sPref.getString("order", "createTime DESC"));
            	getProfileList(false);
            }
            if (prefs.isNotif() != sPref.getBoolean("notif", true)) {
            	prefs.setNotif(sPref.getBoolean("notif", true));
            }
            if (prefs.getSound() != Integer.valueOf(sPref.getString("sound", "2"))) {
            	prefs.setSound(Integer.valueOf(sPref.getString("sound", "2")));
            }
        }
	}
	
	/**
	 * ��������
	 * @return
	 */
	public boolean startService(int id, boolean isChange) {
		boolean flag = false;
		try {
			//Log.i(TAG, "�÷���������Ϣ��id��" + id + "   isChange��" + isChange + "   isFirstRun��" + isFirstShow);
			if (!isServiceRun() || (!isFirstShow && isChange)) {
				/* if (!isServiceRun()) Log.i(TAG, "��������״̬===>δ����");
				else Log.i(TAG, "��������״̬===>��Ҫ����"); */
				Profile profile = profileDao.getProfileById(id);
				Intent intent = new Intent();
				intent.setClass(ProfileListActivity.this, ProfileService.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("changed", isChange);
				bundle.putInt("id", profile.getId());
				bundle.putInt("pic", profile.getPic());
				bundle.putString("name", profile.getName());
				//Log.i(TAG, "����==>�������������" + profile.getId());
				
				bundle.putInt("audioRing", profile.getAudioRing());
				bundle.putInt("audioCall", profile.getAudioCall());
				bundle.putInt("audioAlarm", profile.getAudioAlarm());
				bundle.putInt("audioMusic", profile.getAudioMusic());
				bundle.putInt("vibrator", profile.getVibrator());
				
				bundle.putInt("airplane", profile.getAirplane());
				bundle.putInt("wifi", profile.getWifi());
				bundle.putInt("bluetooth", profile.getBluetooth());
				
				bundle.putBoolean("show", prefs.isNotif());
				bundle.putInt("sound", prefs.getSound());
				
				intent.putExtras(bundle);
				startService(intent);
				//Log.i(TAG, "��������״̬===>�����ɹ�");
				flag = true;
			}
			else
			{
				//Log.i(TAG, "��������״̬===>�Ѿ�������");
				flag = false;
			}
		}
		catch (Exception ex) {
			flag = false;
		}
		if (isFirstShow) {
			isFirstShow = false;
		}
		return flag;
	}
	
	/**
	 * ֹͣ����
	 * @return
	 */
	public boolean stopService() {
		Intent intent = new Intent(ProfileListActivity.this, ProfileService.class);
		return stopService(intent);
	}
	
	/**
	 * �����Ƿ���
	 * @return
	 */
	public boolean isServiceRun(){
	     ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
	     List<RunningServiceInfo> list = am.getRunningServices(30);
	     for(RunningServiceInfo info : list){
	         if(info.service.getClassName().equals("com.mycreat.profilemanager.service.ProfileService")){
	                  return true;
	         }
	    }
	    return false;
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "�½�").setIcon(android.R.drawable.ic_menu_add); 
		menu.add(0, 2, 0, "����").setIcon(android.R.drawable.ic_menu_preferences); 
		menu.add(0, 3, 0, "�˳�").setIcon(android.R.drawable.ic_menu_close_clear_cancel); 
        return super.onCreateOptionsMenu(menu); 
    }
      
    @Override  
    public boolean onMenuItemSelected(int featureId, MenuItem item)  
    {  
        switch (item.getItemId())  
        {  
            case 1:  //�½� 
            	showProfileActivity(-1);
                break;
            case 2:  //����  
                startActivityForResult(new Intent(this, PreferencesActivity.class), ACTIVITY_PREF);  
                break;
            default:  //�˳�
            	this.finish();
                break;  
        }  
        return super.onMenuItemSelected(featureId, item);  
    } 
    
    /*
    @Override 
    public void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
    }
    
    @Override 
    public void onPause() {
    	super.onPause();
    	MobclickAgent.onPause(this);
    }
    */
}

/**
 * ѡ����
 * @author CBQ
 *
 */
class Prefs {
	public Prefs() {
	}
	
	private String order = "createTime DESC";
	private boolean notif = true;
	private int sound = 2;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	
	public boolean isNotif() {
		return notif;
	}

	public void setNotif(boolean notif) {
		this.notif = notif;
	}
	
	public int getSound() {
		return sound;
	}

	public void setSound(int sound) {
		this.sound = sound;
	}
}

/**
 * �Զ����б���ʾAdapter
 * @author CBQ
 *
 */
class myCursorAdapter extends CursorAdapter{
	private LayoutInflater mInflater;  
	private Context mContext; 
	
	public myCursorAdapter(Context context, Cursor c) {
		super(context, c);
		mContext = context;
        mInflater = LayoutInflater.from(context);  
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view.findViewById(R.id.tvName)).setText(cursor.getString(ProfileColumns.PROFILE_COL_NAME));
        ((ImageView) view.findViewById(R.id.imgPic)).setImageResource((context.getResources()).getIdentifier("icon_" + cursor.getInt(ProfileColumns.PROFILE_COL_PIC), "drawable", "com.mycreat.profilemanager"));        
        
        LinearLayout ll = ((LinearLayout) view.findViewById(R.id.profileFlag_layout));
        ll.removeAllViews();

        if (cursor.getInt(ProfileColumns.PROFILE_COL_AUDIO_RING) == 0) {
        	ImageView ii = new ImageView(context);
            ii.setImageResource(R.drawable.ic_flag_volume_off);
            ii.setPadding(0, 0, 10, 0);
            ll.addView(ii);
        } else if (cursor.getInt(ProfileColumns.PROFILE_COL_AUDIO_RING) > 0) {
        	ImageView ii = new ImageView(context);
            ii.setImageResource(R.drawable.ic_flag_volume_on);
            ii.setPadding(0, 0, 10, 0);
            ll.addView(ii);
        }
        if (cursor.getInt(ProfileColumns.PROFILE_COL_VIBRATOR) == 1) {
        	ImageView ii = new ImageView(context);
            ii.setImageResource(R.drawable.ic_flag_vibrate);
            ii.setPadding(0, 0, 10, 0);
            ll.addView(ii);
        }
        if (cursor.getInt(ProfileColumns.PROFILE_COL_AIRPLANE) == 1) {
        	ImageView ii = new ImageView(context);
            ii.setImageResource(R.drawable.ic_flag_airplane);
            ii.setPadding(0, 0, 10, 0);
            ll.addView(ii);
        }
        if (cursor.getInt(ProfileColumns.PROFILE_COL_WIFI) == 1) {
        	ImageView ii = new ImageView(context);
            ii.setImageResource(R.drawable.ic_flag_wifi);
            ii.setPadding(0, 0, 10, 0);
            ll.addView(ii);
        }
        if (cursor.getInt(ProfileColumns.PROFILE_COL_BLUETOOTH) == 1) {
        	ImageView ii = new ImageView(context);
            ii.setImageResource(R.drawable.ic_flag_bluetooth);
            ii.setPadding(0, 0, 10, 0);
            ll.addView(ii);
        }
		
        //Log.d("��ǰ����", "------------" + cursor.getInt(ProfileColumns.PROFILE_COL_ISACTIVE));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		return mInflater.inflate(R.layout.profile_list_row, arg2, false);
	}        
}  