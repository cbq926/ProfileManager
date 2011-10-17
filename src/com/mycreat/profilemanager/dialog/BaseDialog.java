package com.mycreat.profilemanager.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager.LayoutParams;

public class BaseDialog extends Dialog{

	public BaseDialog(Context context) {
		super(context);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * ×ÔÊÊÓ¦¿í¶È
	 */
	public void adjustWidth() {
		LayoutParams params = getWindow().getAttributes();   
        params.width = LayoutParams.FILL_PARENT;   
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
	}
	
	
}
