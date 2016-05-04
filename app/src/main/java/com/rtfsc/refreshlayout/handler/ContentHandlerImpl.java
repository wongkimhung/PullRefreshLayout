package com.rtfsc.refreshlayout.handler;

import com.rtfsc.refreshlayout.core.PullToRefreshView;

/**
 * @author HuangJx
 *         create on 2016/5/3.
 */
public class ContentHandlerImpl implements ContentHandler {
	PullToRefreshView mPullToRefreshView;

	public ContentHandlerImpl(PullToRefreshView pullToRefreshView) {
		mPullToRefreshView = pullToRefreshView;
	}

	@Override
	public void onBeginRefresh() {

	}

	@Override
	public boolean canContentScroll() {
		return false;
	}
}
