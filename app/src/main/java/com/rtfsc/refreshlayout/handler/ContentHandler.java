package com.rtfsc.refreshlayout.handler;

import android.view.View;

import com.rtfsc.refreshlayout.core.PullToRefreshView;

/**
 * @author HuangJx
 *         create on 2016/5/3.
 */
public interface ContentHandler {

	boolean checkCanDoRefresh(PullToRefreshView frame, View content, View header);
}
