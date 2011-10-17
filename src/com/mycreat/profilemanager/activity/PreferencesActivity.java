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
				builder.setTitle("��ʾ").setIcon(R.drawable.ic_dialog_info).setMessage("���޸ĵ�֪ͨ��״̬��ʾ���������´��л��龰ģʽʱ��Ч��").setNegativeButton("����", null);
				AlertDialog ad = builder.create();
				ad.show();
				return true;
			}
		});
		initAboutPreference((PreferenceScreen)findPreference("about"));
	}
	
	/**
	 * ��ʼ������
	 * @param preference
	 */
	private void initAboutPreference(PreferenceScreen preference) {
		preference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(PreferencesActivity.this);
				builder.setTitle("����").setIcon(R.drawable.icon).setMessage(android.text.Html.fromHtml("<b>�汾�ţ�1.0.0.0</b><br /><br />&nbsp;&nbsp;&nbsp;&nbsp;�龰ģʽ(Profile Manager) ��һ��Androidƽ̨���龰ģʽ��ǿ������ܹ�������ʱ�л������龰ģʽ��<br />&nbsp;&nbsp;&nbsp;&nbsp;��������˼��ֳ����龰ģʽ����׼����ͥ�����顢����ȣ������ҿ��Ը�����Ҫ�����޸ĺ���ӡ�<br />&nbsp;&nbsp;&nbsp;&nbsp;ÿ���龰ģʽ���Զ�������ͨ�������������������񶯡�����ģʽ��Wi-Fi�������Ƚ�����ϸ�����á�<br /><br />ʹ������ʲô��������⣬�뷴������cbq926@gmail.com")).setNegativeButton("����", null);
				AlertDialog ad = builder.create();
				ad.show();
				return false;
			}
		});
	}
	
	/**
	 * ��ʼ������ѡ��
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
							preference.setSummary("������ʱ��");
						else if (value.equals("updateTime DESC"))
							preference.setSummary("���޸�ʱ��");
						else if (value.equals("used DESC"))
							preference.setSummary("��ʹ��Ƶ��ʱ��");
						return true;
					}
				});
	}
	
	/**
	 * ��ʼ����ʾ��
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
							preference.setSummary("����");
						else if (value.equals("2"))
							preference.setSummary("�ر�");
						else if (value.equals("3"))
							preference.setSummary("ֻ��������");
						else if (value.equals("4"))
							preference.setSummary("ֻ������");
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
