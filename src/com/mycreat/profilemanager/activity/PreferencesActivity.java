package com.mycreat.profilemanager.activity;

import com.mycreat.profilemanager.R;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.mobclick.android.MobclickAgent;

public class PreferencesActivity extends PreferenceActivity {

	static final String TAG = "PreferencesActivity";

	String order;
	ListPreference orderListPref;
	CheckBoxPreference c;
	PreferenceScreen aboutPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		initOrderListPreference((ListPreference)findPreference("order"), "createTime DESC");
		initSoundListPreference((ListPreference)findPreference("sound"), "2");
		((CheckBoxPreference)findPreference("notif")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PreferencesActivity.this);
				builder.setTitle("提示").setIcon(R.drawable.ic_dialog_info).setMessage("您修改的通知栏状态显示，将会在下次切换情景模式时生效。").setNegativeButton("返回", null);
				AlertDialog ad = builder.create();
				ad.show();
				return true;
			}
		});
		initAboutPreference((PreferenceScreen)findPreference("about"));
	}
	
	/**
	 * 初始化关于
	 * @param preference
	 */
	private void initAboutPreference(PreferenceScreen preference) {
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PreferencesActivity.this);
				builder.setTitle("关于").setIcon(R.drawable.icon).setMessage(android.text.Html.fromHtml("<b>版本号：1.0.0.0</b><br /><br />&nbsp;&nbsp;&nbsp;&nbsp;情景模式(Profile Manager) 是一款Android平台的情景模式增强软件，能够让你随时切换多种情景模式。<br />&nbsp;&nbsp;&nbsp;&nbsp;软件内置了几种常用情景模式（标准、家庭、会议、户外等），并且可以根据需要进行修改和添加。<br />&nbsp;&nbsp;&nbsp;&nbsp;每个情景模式可以对铃声、通话音量、闹铃音量、振动、飞行模式、Wi-Fi、蓝牙等进行详细的设置。<br /><br />使用中有什么建议或问题，请反馈到：cbq926@gmail.com")).setNegativeButton("返回", null);
				AlertDialog ad = builder.create();
				ad.show();
				return false;
			}
		});
	}
	
	/**
	 * 初始化排序选项
	 * @param preference
	 * @param defaultValue
	 */
	private void initOrderListPreference(ListPreference preference, String defaultValue) {
		if (preference.getEntry() == null) {
			preference.setValue(defaultValue);
		}
		preference.setSummary(preference.getEntry());
		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference, Object newValue) {
						String value = newValue.toString();
						if (value.equals("createTime DESC"))
							preference.setSummary("按创建时间");
						else if (value.equals("updateTime DESC"))
							preference.setSummary("按修改时间");
						else if (value.equals("used DESC"))
							preference.setSummary("按使用频率时间");
						return true;
					}
				});
	}
	
	/**
	 * 初始化提示音
	 * @param preference
	 * @param defaultValue
	 */
	private void initSoundListPreference(ListPreference preference, String defaultValue) {
		if (preference.getEntry() == null) {
			preference.setValue(defaultValue);
		}
		preference.setSummary(preference.getEntry());
		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference, Object newValue) {
						String value = newValue.toString();
						if (value.equals("1"))
							preference.setSummary("开启");
						else if (value.equals("2"))
							preference.setSummary("关闭");
						else if (value.equals("3"))
							preference.setSummary("只开启声音");
						else if (value.equals("4"))
							preference.setSummary("只开启振动");
						return true;
					}
				});
	}
	
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
}
