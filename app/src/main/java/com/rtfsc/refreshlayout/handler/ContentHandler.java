package com.rtfsc.refreshlayout.handler;

/**
 * @author HuangJx
 *         create on 2016/5/3.
 */
public interface ContentHandler {
	void onBeginRefresh();

	boolean canContentScroll();
}
