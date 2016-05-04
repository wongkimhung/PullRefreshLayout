package com.rtfsc.refreshlayout.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rtfsc.refreshlayout.R;
import com.rtfsc.refreshlayout.handler.HeaderHandler;

/**
 * @author HuangJx
 *         create on 2016/5/3.
 */
public class BaiduGogingHeader extends RelativeLayout implements HeaderHandler {
	public BaiduGogingHeader(Context context) {
		this(context, null);
	}

	public BaiduGogingHeader(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BaiduGogingHeader(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.headview,this,false);
	}
}
