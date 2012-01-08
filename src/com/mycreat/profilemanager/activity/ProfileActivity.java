package com.mycreat.profilemanager.activity;

import com.mycreat.profilemanager.R;
import com.mycreat.profilemanager.dao.ProfileDao;
import com.mycreat.profilemanager.data.Profile;
import com.mycreat.profilemanager.dialog.ProfileNameDialogListener;
import com.mycreat.profilemanager.dialog.ProfileNameDialog;
import com.mycreat.profilemanager.dialog.ProfileAudioDialog;
import com.mycreat.profilemanager.dialog.ProfileAudioDialogListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobclick.android.MobclickAgent;

public class ProfileActivity extends Activity {
	
	private static String TAG = "ProfileActivity";
	
	ImageButton imgbtnBack;
	ImageButton imgbtnSave;
	
	TextView tvName;  //名称
	ImageView imgPic;  //图片
	TextView tvAudioRing;  //铃声音量
	TextView tvAudioCall;  //通话音量
	TextView tvAudioAlarm;  //闹钟音量
	TextView tvAudioMusic;  //媒体音量
	
	ImageView imgAudioRing;  //铃声音量
	ImageView imgAudioCall;  //通话音量
	ImageView imgAudioAlarm;  //闹钟音量
	ImageView imgAudioMusic;  //媒体音量
	
	//TextView tvVolume;  //音量
	TextView tvVibrator;  //震动
	
	TextView tvAirplane;  //飞行模式
	TextView tvWifi;  //无线
	TextView tvBluetooth;  //蓝牙
	
	Profile profile = new Profile();
	ProfileDao profileDao = new ProfileDao(this);
	
	int mode = 0;  //0-新建  1-编辑
	int editId = -1;
	boolean isEdited = false;
	
	private final static int AUDIO_RING = 1;
	private final static int AUDIO_CALL = 2;
	private final static int AUDIO_ALARM = 3;
	private final static int AUDIO_MUSIC = 4;
	
	private final static int VIBRATOR = 5;
	private final static int AIRPLANE = 6;
	private final static int WIFI = 7;
	private final static int BLUETOOTH = 8;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		
		initDate();  //初始化数据
		initView();  //初始化组件
		initName();

		initAudio(AUDIO_RING,  (RelativeLayout)findViewById(R.id.relativeAudioRing), new ProfileAudioDialogListener() {
			@Override
			public void onOkClick(int volume) {
				if (profile.getAudioRing() != volume) {
					isEdited = true;
					profile.setAudioRing(volume);
					//Log.i(TAG, "铃声音量返回值：" + profile.getAudioRing());
					setVolume(volume, 7, imgAudioRing, tvAudioRing);	
				}
			}
		});
		
		initAudio(AUDIO_CALL, (RelativeLayout)findViewById(R.id.relativeAudioCall), new ProfileAudioDialogListener() {
			@Override
			public void onOkClick(int volume) {
				if (profile.getAudioCall() != volume) {
					isEdited = true;
					profile.setAudioCall(volume);
					//Log.i(TAG, "通话音量返回值：" + profile.getAudioCall());
					setVolume(volume, 5, imgAudioCall, tvAudioCall);
				}
			}
		});
		
		initAudio(AUDIO_ALARM, (RelativeLayout)findViewById(R.id.relativeAudioAlarm), new ProfileAudioDialogListener() {
			@Override
			public void onOkClick(int volume) {
				if (profile.getAudioAlarm() != volume) {
					isEdited = true;
					profile.setAudioAlarm(volume);
					//Log.i(TAG, "闹钟音量返回值：" + profile.getAudioAlarm());
					setVolume(volume, 7, imgAudioAlarm, tvAudioAlarm);
				}
			}
		});
		
		initAudio(AUDIO_MUSIC, (RelativeLayout)findViewById(R.id.relativeAudioMusic), new ProfileAudioDialogListener() {
			@Override
			public void onOkClick(int volume) {
				if (profile.getAudioMusic() != volume) {
					isEdited = true;
					profile.setAudioMusic(volume);
					//Log.i(TAG, "媒体音量返回值：" + profile.getAudioMusic());
					setVolume(volume, 15, imgAudioMusic, tvAudioMusic);
				}
			}
		});
		
		initSingleChoiceDialog(VIBRATOR, (RelativeLayout)findViewById(R.id.relativeVibrator),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						if (profile.getVibrator() != i) {
							isEdited = true;
							profile.setVibrator(i);
							switch (i) {
							case 0:
								tvVibrator.setText("[保持不变]");
								break;
							case 1:
								tvVibrator.setText("开启");
								break;
							case 2:
								tvVibrator.setText("关闭");
								break;
							}
						}
						dialog.dismiss();
					}
				});
		
		initSingleChoiceDialog(AIRPLANE, (RelativeLayout)findViewById(R.id.relativeAirplane),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						if (profile.getAirplane() != i) {
							isEdited = true;
							profile.setAirplane(i);
							switch (i) {
							case 0:
								tvAirplane.setText("[保持不变]");
								break;
							case 1:
								tvAirplane.setText("开启");
								break;
							case 2:
								tvAirplane.setText("关闭");
								break;
							}
						}
						dialog.dismiss();
					}
				});
		
		initSingleChoiceDialog(WIFI, (RelativeLayout)findViewById(R.id.relativeWifi),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						if (profile.getWifi() != i) {
							isEdited = true;
							profile.setWifi(i);
							switch (i) {
							case 0:
								tvWifi.setText("[保持不变]");
								break;
							case 1:
								tvWifi.setText("开启");
								break;
							case 2:
								tvWifi.setText("关闭");
								break;
							}
						}
						dialog.dismiss();
					}
				});
		
		initSingleChoiceDialog(BLUETOOTH, (RelativeLayout)findViewById(R.id.relativeBluetooth),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i) {
						if (profile.getBluetooth() != i) {
							isEdited = true;
							switch (i) {
							case 0:
								profile.setBluetooth(0);
								tvBluetooth.setText("[保持不变]");
								break;
							case 1:
								profile.setBluetooth(12);
								tvBluetooth.setText("开启");
								break;
							case 2:
								profile.setBluetooth(10);
								tvBluetooth.setText("关闭");
								break;
							}
						}
						dialog.dismiss();
					}
				});
	}
	
	/**
	 * 初始化数据
	 */
	public void initDate() {
		Bundle bundle = this.getIntent().getExtras();
		if (bundle.isEmpty()) {
			profile.setName("新情景模式");
			profile.setPic(1);
			profile.setAudioRing(6);
			profile.setAudioCall(4);
			profile.setAudioAlarm(5);
			profile.setAudioMusic(12);
			profile.setVibrator(1);
			profile.setAirplane(0);
			profile.setWifi(0);
			profile.setBluetooth(0);
			profile.setActive(false);
		}
		else {  //修改
			mode = 1;
			editId = bundle.getInt("id");
			profile = profileDao.getProfileById(editId);
		}
	}
	
	/**
	 * 初始化组件
	 */
	public void initView()
	{
		tvName = (TextView)findViewById(R.id.tvName);
		tvName.setText(profile.getName());
		
		imgPic = (ImageView)findViewById(R.id.imgPic);
		
		tvAudioRing = (TextView)findViewById(R.id.tvAudioRing);
		tvAudioCall = (TextView)findViewById(R.id.tvAudioCall);
		tvAudioAlarm = (TextView)findViewById(R.id.tvAudioAlarm);
		tvAudioMusic = (TextView)findViewById(R.id.tvAudioMusic);
		
		imgAudioRing = (ImageView)findViewById(R.id.imgAudioRing);
		imgAudioCall = (ImageView)findViewById(R.id.imgAudioCall);
		imgAudioAlarm = (ImageView)findViewById(R.id.imgAudioAlarm);
		imgAudioMusic = (ImageView)findViewById(R.id.imgAudioMusic);
		
		setVolume(profile.getAudioRing(), 7, imgAudioRing, tvAudioRing);
		setVolume(profile.getAudioCall(), 5, imgAudioCall, tvAudioCall);
		setVolume(profile.getAudioAlarm(), 7, imgAudioAlarm, tvAudioAlarm);
		setVolume(profile.getAudioMusic(), 15, imgAudioMusic, tvAudioMusic);

		tvVibrator = (TextView)findViewById(R.id.tvVibrator);
		switch (profile.getVibrator()) {
		case 0:
			tvVibrator.setText("[保持不变]");
			break;
		case 1:
			tvVibrator.setText("开启");
			break;
		case 2:
			tvVibrator.setText("关闭");
			break;
		}
		
		tvAirplane = (TextView)findViewById(R.id.tvAirplane);
		tvWifi = (TextView)findViewById(R.id.tvWifi);
		tvBluetooth = (TextView)findViewById(R.id.tvBluetooth);
		
		switch (profile.getAirplane()) {
		case 0:
			tvAirplane.setText("[保持不变]");
			break;
		case 1:
			tvAirplane.setText("开启");
			break;
		case 2:
			tvAirplane.setText("关闭");
			break;
		}
		
		switch (profile.getWifi()) {
		case 0:
			tvWifi.setText("[保持不变]");
			break;
		case 1:
			tvWifi.setText("开启");
			break;
		case 2:
			tvWifi.setText("关闭");
			break;
		}
		
		switch (profile.getBluetooth()) {
		case 0:
			tvBluetooth.setText("[保持不变]");
			break;
		case 12:
			tvBluetooth.setText("开启");
			break;
		case 10:
			tvBluetooth.setText("关闭");
			break;
		}
		
		imgPic.setImageResource(getResources().getIdentifier("icon_" + profile.getPic(), "drawable", "com.mycreat.profilemanager"));

		//返回
		((ImageButton)findViewById(R.id.imgbtnBack)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				cancel();
			}
		});
		
		//保存
		((ImageButton)findViewById(R.id.imgbtnSave)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				save();
			}
		});
		
		if (mode == 0) ((TextView)findViewById(R.id.tvTitle)).setText("新建");
		else ((TextView)findViewById(R.id.tvTitle)).setText("编辑");
	}
	
	/**
	 * 初始化名称对话框
	 */
	public void initName() {
		final ProfileNameDialog profileNameDialog = new ProfileNameDialog(this, new ProfileNameDialogListener() {
			@Override
			public void onOkClick(String name, int pic) {
				if (!profile.getName().equals(name)) {
					isEdited = true;
					profile.setName(name);
					tvName.setText(name);
				}
				if (profile.getPic() != pic) {
					isEdited = true;
					imgPic.setImageResource(getResources().getIdentifier("icon_" + pic, "drawable", "com.mycreat.profilemanager"));
					profile.setPic(pic);
				}
			}
		}, tvName.getText().toString(), profile.getPic());
		RelativeLayout profile_item_name = (RelativeLayout)findViewById(R.id.relativeName);  
		profile_item_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				profileNameDialog.setTitle("情景模式名称");
				profileNameDialog.show();
			}
		});
	}
	
	/**
	 * 初始化声音对话框
	 * @param title
	 * @param relativeClick
	 * @param volume
	 * @param listener
	 */
	public void initAudio(final int type, RelativeLayout relativeClick, ProfileAudioDialogListener listener) {
		final ProfileAudioDialog dialog = new ProfileAudioDialog(this, listener);
		relativeClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String title = "";
				switch (type) {
				case AUDIO_RING:
					title = "铃声音量";
					dialog.setData(profile.getAudioRing(), 7);
					break;
				case AUDIO_CALL:
					title = "通话音量";
					dialog.setData(profile.getAudioCall(), 5);
					break;
				case AUDIO_ALARM:
					title = "闹钟音量";
					dialog.setData(profile.getAudioAlarm(), 7);
					break;
				case AUDIO_MUSIC:
					title = "媒体音量";
					dialog.setData(profile.getAudioMusic(), 15);
					break;
				}
				dialog.setTitle(title);
				dialog.show();
			}
		});
	}

	/**
	 * 初始化单选对话框
	 * @param title
	 * @param relativeClick
	 * @param value
	 * @param listener
	 */
	public void initSingleChoiceDialog(final int type, RelativeLayout relativeClick, final DialogInterface.OnClickListener listener) {
		relativeClick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int value = 0;
				String title = "";
				switch (type) {
				case VIBRATOR:
					title = "振动";
					value = profile.getVibrator();
					break;
				case AIRPLANE:
					title = "飞行模式";
					value = profile.getAirplane();
					break;
				case WIFI:
					title = "Wi-Fi开关";
					value = profile.getWifi();
					break;
				case BLUETOOTH:
					title = "蓝牙开关";
					if (profile.getBluetooth() == 0) value = 0;
					else if (profile.getBluetooth() == 12) value = 1;
					else value = 2;
					break;
				}
				AlertDialog ad = (new AlertDialog.Builder(ProfileActivity.this))
						.setTitle(title)
						.setSingleChoiceItems(
								new String[] { "保持不变", "开启", "关闭" },
								value,
								listener).create();
				ad.show();
			}
		});
	}

	/**
	 * 保存到数据库
	 * @param isActive
	 * @return
	 */
	public boolean saveProfile(boolean isActive) {
		try {
			ContentValues values = new ContentValues();
			values.put("name", profile.getName());
			values.put("pic", profile.getPic());
			values.put("audioRing", profile.getAudioRing());
			values.put("audioCall", profile.getAudioCall());
			values.put("audioAlarm", profile.getAudioAlarm());
			values.put("audioMusic", profile.getAudioMusic());
			values.put("vibrator", profile.getVibrator());
			values.put("airplane", profile.getAirplane());
			values.put("wifi", profile.getWifi());
			values.put("bluetooth", profile.getBluetooth());
			values.put("isActive", isActive ? 1 : 0);
			
			values.put("updateTime", System.currentTimeMillis());
			
			if (mode == 0) {
				values.put("createTime", System.currentTimeMillis());
				profileDao.insertProfile(values);
			}
			else profileDao.updateProfile(values, editId);
			return true;
		}
		catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 设置声音
	 * @param value
	 * @param max
	 * @param imageView
	 * @param txtView
	 */
	private void setVolume(int value, int max, ImageView imageView, TextView txtView) {
		if (value == 0) {
			txtView.setText("静音");
			imageView.setVisibility(View.GONE);
			txtView.setVisibility(View.VISIBLE);
		}
		else if (value == -1) {
			txtView.setText("[保持不变]");
			imageView.setVisibility(View.GONE);
			txtView.setVisibility(View.VISIBLE);
		}
		else if (max == value) {
			imageView.setImageResource(R.drawable.ic_volume);
			txtView.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}
		else {
			if (max == 7) {
				switch (value) {
				case 6: imageView.setImageResource(R.drawable.ic_volume_7_6); break;
				case 5: imageView.setImageResource(R.drawable.ic_volume_7_5); break;
				case 4: imageView.setImageResource(R.drawable.ic_volume_7_4); break;
				case 3: imageView.setImageResource(R.drawable.ic_volume_7_3); break;
				case 2: imageView.setImageResource(R.drawable.ic_volume_7_2); break;
				case 1: imageView.setImageResource(R.drawable.ic_volume_7_1); break;
				}
			}
			else if (max == 5) {
				switch (value) {
					case 4: imageView.setImageResource(R.drawable.ic_volume_5_4); break;
					case 3: imageView.setImageResource(R.drawable.ic_volume_5_3); break;
					case 2: imageView.setImageResource(R.drawable.ic_volume_5_2); break;
					case 1: imageView.setImageResource(R.drawable.ic_volume_5_1); break;
				}
			}
			else if (max == 15) {
				switch (value) {
				case 14: imageView.setImageResource(R.drawable.ic_volume_15_14); break;
				case 13: imageView.setImageResource(R.drawable.ic_volume_15_13); break;
				case 12: imageView.setImageResource(R.drawable.ic_volume_15_12); break;
				case 11: imageView.setImageResource(R.drawable.ic_volume_15_11); break;
				case 10: imageView.setImageResource(R.drawable.ic_volume_15_10); break;
				case 9: imageView.setImageResource(R.drawable.ic_volume_15_9); break;
				case 8: imageView.setImageResource(R.drawable.ic_volume_15_8); break;
				case 7: imageView.setImageResource(R.drawable.ic_volume_15_7); break;
				case 6: imageView.setImageResource(R.drawable.ic_volume_15_6); break;
				case 5: imageView.setImageResource(R.drawable.ic_volume_15_5); break;
				case 4: imageView.setImageResource(R.drawable.ic_volume_15_4); break;
				case 3: imageView.setImageResource(R.drawable.ic_volume_15_3); break;
				case 2: imageView.setImageResource(R.drawable.ic_volume_15_2); break;
				case 1: imageView.setImageResource(R.drawable.ic_volume_15_1); break;
				}
			}
			txtView.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 0, "保存").setIcon(android.R.drawable.ic_menu_save);
		menu.add(0, 2, 0, "返回").setIcon(android.R.drawable.ic_menu_revert);
        return super.onCreateOptionsMenu(menu); 
    }
	
    @Override  
    public boolean onMenuItemSelected(int featureId, MenuItem item)  
    {  
        switch (item.getItemId())  
        {  
            case 1:  //保存 
            	save();
                break;
            case 2:  //返回  
                cancel();
                break;
        }  
        return super.onMenuItemSelected(featureId, item);  
    }
    
    /**
     * 保存
     */
    private void save() {
    	if (!profile.isActive()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
			builder.setTitle("立即启用").setIcon(R.drawable.ic_dialog_info).setMessage(profile.getName() + " 已保存，是否立即启用该模式？").setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					saveProfile(true);
					Intent intent = new Intent();
					intent.putExtra("needChange", true);
					setResult(Activity.RESULT_OK, intent); 
					ProfileActivity.this.finish();
				}
				
			}).setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					saveProfile(false);
					Intent intent = new Intent();
					intent.putExtra("needChange", false);
					setResult(Activity.RESULT_OK, intent); 
					ProfileActivity.this.finish();
				}
			});
			AlertDialog ad = builder.create();
			ad.show();
		}
		else {
			saveProfile(true);
			Intent intent = new Intent();
			intent.putExtra("needChange", true);
			setResult(Activity.RESULT_OK, intent); 
			ProfileActivity.this.finish();
		}
    }
    
    /**
     * 舍弃返回
     */
    private void cancel() {
    	if (isEdited) {
    		AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
			builder.setTitle("提示").setMessage("您的内容已修改，是否保存？").setIcon(R.drawable.ic_dialog_info)
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							save();
						}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							setResult(RESULT_CANCELED);
							ProfileActivity.this.finish();
						}
				});
			AlertDialog ad = builder.create();
			ad.show();
    	}
    	else {
    		setResult(RESULT_CANCELED);
			ProfileActivity.this.finish();
    	}
    }
    
    /**
     * 捕获返回按键
     */
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	cancel();
            return false; 
        }
        return false; 
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
