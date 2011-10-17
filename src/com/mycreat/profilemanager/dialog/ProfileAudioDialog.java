package com.mycreat.profilemanager.dialog;

import com.mycreat.profilemanager.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;

public class ProfileAudioDialog extends BaseDialog {

	private static String TAG = "ProfileAudioDialog";
	
	SeekBar sbarAudio;
	CheckBox chkKeep;
	Button btnOk;
	Button btnCancel;
	
	ProfileAudioDialogListener listener;
	
	int volume_pre = -1;
	int volume;
	int max;
	
	public ProfileAudioDialog(Context context, ProfileAudioDialogListener listener) {
		super(context);
		this.listener = listener;
	}
	
	public void setData(int volume, int max) {
		this.max = max;
		this.volume = volume;
	}
	
	@Override
	public void show() {
		super.show();
		initView();
		initListener();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_audio_dialog);
		adjustWidth();
	}
	
	private OnCheckedChangeListener onChkChangedListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton btnView, boolean isChecked) {
			 switch(btnView.getId()){ 
			    case R.id.chkKeep:
			    	if (isChecked) {
			    		sbarAudio.setVisibility(View.GONE);
			    		if (volume == -1) volume = adjustInitVolume(max);  //初始设定一个值
			    		sbarAudio.setProgress(volume);
			    	}
			    	else {
			    		sbarAudio.setVisibility(View.VISIBLE);
			    	}
			    break; 
			    }     
		}
	};
	
	/**
	 * 初始化View
	 */
	public void initView(){
		sbarAudio = (SeekBar)findViewById(R.id.sbarVolume);
		chkKeep = (CheckBox)findViewById(R.id.chkKeep);
		btnOk = (Button)findViewById(R.id.btnOk);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		
		sbarAudio.setMax(max); 
		
		chkKeep.setOnCheckedChangeListener(onChkChangedListener);
		
		if (volume == -1) {  //保持不变
			chkKeep.setChecked(true);
			sbarAudio.setVisibility(View.GONE);
		}
		else{
			chkKeep.setChecked(false);
			sbarAudio.setVisibility(View.VISIBLE);
			sbarAudio.setProgress(volume);
		}
	}
	
	/**
	 * 初始化Listener
	 */
	public void initListener() {
		btnOk.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (chkKeep.isChecked()) listener.onOkClick(-1);
				else listener.onOkClick(sbarAudio.getProgress());
				ProfileAudioDialog.this.dismiss();
			}
		});
		
		btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ProfileAudioDialog.this.dismiss();
				sbarAudio.setProgress(volume);  
			}
		});
	}
	
	/**
	 * 默认音量
	 * @param max 最大值
	 * @return
	 */
	private int adjustInitVolume(int max) {
		if (max == 7) return 5;
		if (max == 5) return 4;
		if (max == 15) return 12;
		return 7;
	}
}
