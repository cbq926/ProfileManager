package com.mycreat.profilemanager.dialog;

import com.mycreat.profilemanager.R;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.view.View;

public class ProfileNameDialog extends BaseDialog {
	
	EditText txtName;
	ImageView imgPic;
	Button btnOk;
	Button btnCancel;
	ProfileNameDialogListener listener;
	
	String name;
	int pic;
	
	public ProfileNameDialog(Context context, ProfileNameDialogListener listener, String name, int pic) {
		super(context);
		this.listener = listener;
		this.name= name;
		this.pic = pic;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_name_dialog);
		adjustWidth();
		
		initView();
		initListener();
	}
	
	/**
	 * 初始化View
	 */
	public void initView(){
		txtName = (EditText)findViewById(R.id.txtName);
		imgPic = (ImageView)findViewById(R.id.imgPic);
		btnOk = (Button)findViewById(R.id.btnOk);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		txtName.setText(name);
		
		imgPic.setImageResource((this.getContext().getResources()).getIdentifier("icon_" + pic, "drawable", "com.mycreat.profilemanager"));
		
		initPicChoice();
	}
	
	/**
	 * 初始化图片选择区
	 */
	public void initPicChoice() {
		LinearLayout layoutPicChoice = (LinearLayout)findViewById(R.id.layoutPicChoice);
		Resources res = this.getContext().getResources();
		for(int i = 1; i <= 15; i++){
			ImageView img = new ImageView(this.getContext());
			img.setImageResource(res.getIdentifier("icon_" + i, "drawable", "com.mycreat.profilemanager"));
			
			final int j = i;
			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					imgPic.setImageDrawable(((ImageView)view).getDrawable());
					pic = j;
				}
			});
			layoutPicChoice.addView(img);
		}
	}
	
	/**
	 * 初始化Listener
	 */
	public void initListener() {
		btnOk.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listener.onOkClick(txtName.getText().toString(), pic);
				ProfileNameDialog.this.dismiss();
			}
		});
		
		btnCancel.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ProfileNameDialog.this.dismiss();
				txtName.setText(name);  //恢复初始值
			}
		});
	}
}
