package com.rtfsc.refreshlayout.handler;

import com.rtfsc.refreshlayout.core.PullToRefreshView;

/**
 * @author HuangJx
 *         create on 2016/5/5.
 */
public interface PullUIHandler {
	/**
	 * 当内容页到达顶部和刷新完成
	 *
	 * @param frame
	 */
	void onUIReset(PullToRefreshView frame);

	/**
	 * 达到刷新高度
	 *
	 * @param frame
	 */
	void onUIRefreshPrepare(PullToRefreshView frame);

	/**
	 * 开始刷新
	 */
	void onUIRefreshBegin(PullToRefreshView frame);

	/**
	 * 结束刷新
	 */
	void onUIRefreshEnd(PullToRefreshView frame);
}
