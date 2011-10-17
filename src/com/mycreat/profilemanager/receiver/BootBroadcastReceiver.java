package com.mycreat.profilemanager.receiver;

import com.mycreat.profilemanager.dao.ProfileDao;
import com.mycreat.profilemanager.data.Profile;
import com.mycreat.profilemanager.service.ProfileService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String TAG = "BootBroadcastReceiver";
			
	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION)) {
			//Log.i(TAG, "[�����㲥]���ڿ�����������");
			ProfileDao profileDao = new ProfileDao(context);
			int curId = profileDao.getActiveId();  //��õ�ǰ�����ķ���
			if (curId != -1) {
				//Log.i(TAG, "[�����㲥]׼������IdΪ " + curId + " �ķ���");
				Intent startServiceIntent = new Intent(context, ProfileService.class);
				
				Profile profile = profileDao.getProfileById(curId);
				Bundle bundle = new Bundle();
				bundle.putInt("id", profile.getId());
				bundle.putString("name", profile.getName());
				bundle.putInt("pic", profile.getPic());
				bundle.putInt("audioRing", profile.getAudioRing());
				bundle.putInt("audioCall", profile.getAudioCall());
				bundle.putInt("audioAlarm", profile.getAudioAlarm());
				bundle.putInt("audioMusic", profile.getAudioMusic());
				bundle.putInt("vibrator", profile.getVibrator());
				bundle.putInt("airplane", profile.getAirplane());
				bundle.putInt("wifi", profile.getWifi());
				bundle.putInt("bluetooth", profile.getBluetooth());
				
				startServiceIntent.putExtras(bundle);
				startServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(startServiceIntent);
				
				//Log.i(TAG, "[�����㲥]�ѳɹ���������");
			}
		}
	}
}
