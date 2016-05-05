package com.rtfsc.refreshlayout.handler;

import android.os.Build;
import android.view.View;
import android.widget.AbsListView;

import com.rtfsc.refreshlayout.core.PullToRefreshView;

/**
 * @author HuangJx
 *         create on 2016/5/5.
 */
public abstract class ContentDefaultHandler implements ContentHandler {
	@Override
	public boolean checkCanDoRefresh(PullToRefreshView frame, View content, View header) {
		return !canChildScroll(content);
	}

	private boolean canChildScroll(View view) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			if (view instanceof AbsListView) {
				AbsListView absListView = (AbsListView) view;
				return absListView.getChildCount() > 0
						&& (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
			} else {
				return view.getScrollY() > 0;
			}
		} else {
			return view.canScrollVertically(-1);
		}
	}
}
