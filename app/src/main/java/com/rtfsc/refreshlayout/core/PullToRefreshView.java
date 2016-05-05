package com.rtfsc.refreshlayout.core;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.rtfsc.refreshlayout.handler.ContentDefaultHandler;
import com.rtfsc.refreshlayout.handler.ContentHandler;
import com.rtfsc.refreshlayout.handler.HeaderHandler;
import com.rtfsc.refreshlayout.handler.PullUIHandler;
import com.rtfsc.refreshlayout.indicator.OffsetIndicator;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author HuangJx
 *         create on 2016/5/3.
 */
public class PullToRefreshView extends ViewGroup {
	private final int STATE_NONE = 0;
	private final int STATE_REFRESHING = 1;
	private final int STATE_PULL_DOWN = 2;
	private final int STATE_RELEASE = 3;
	private int STATE = STATE_NONE;

	private int mHeaderId;
	private int mContentId;
	private View mHeader;
	private View mContent;
	private float mZuni = 0.3f;

	private int mHeaderViewHeight;

	private SparseArray<WeakReference<PullUIHandler>> mWeakReferenceSparseArray;    //UI事件分发
	private ContentHandler mContentHandler;    //用来判断内容页能否下滑
	private OffsetIndicator mOffsetIndicator;    //	offset代理器


	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullToRefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mContentHandler = new ContentDefaultHandler() { };
		mOffsetIndicator = new OffsetIndicator(this);
	}


	@Override
	protected void onFinishInflate() {
		final int childCount = getChildCount();
		if (childCount > 2) {
			throw new IllegalStateException("PullToRefreshView only can host 2 elements");
		} else if (childCount == 2) {
			if (mHeaderId != 0 && mHeader == null) {
				mHeader = findViewById(mHeaderId);
			}
			if (mContentId != 0 && mContent == null) {
				mContent = findViewById(mContentId);
			}

			// not specify header or content
			if (mContent == null || mHeader == null) {

				View child1 = getChildAt(0);
				View child2 = getChildAt(1);
				if (child1 instanceof HeaderHandler) {
					mHeader = child1;
					mContent = child2;
				} else if (child2 instanceof HeaderHandler) {
					mHeader = child2;
					mContent = child1;
				} else {
					// both are not specified
					if (mContent == null && mHeader == null) {
						mHeader = child1;
						mContent = child2;
					}
					// only one is specified
					else {
						if (mHeader == null) {
							mHeader = mContent == child1 ? child2 : child1;
						} else {
							mContent = mHeader == child1 ? child2 : child1;
						}
					}
				}
			}
		} else if (childCount == 1) {
			mContent = getChildAt(0);
		}
		if (mHeader != null) {
			mHeader.bringToFront();
		}
		super.onFinishInflate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//	测量header
		if (mHeader != null) {
			measureChild(mHeader, widthMeasureSpec, heightMeasureSpec);
			mHeaderViewHeight = mHeader.getMeasuredHeight();
			mOffsetIndicator.setMaxHeight(mHeaderViewHeight);
		}
		// 测量content
		if (mContent != null) {
			ViewGroup.LayoutParams layoutParams = mContent.getLayoutParams();
			int marginVertival = getMargin(layoutParams, MARGIN_TOP) + getMargin(layoutParams, MARGIN_BOTTOM);
			int marginHorizontal = getMargin(layoutParams, MARGIN_LEFT) + getMargin(layoutParams, MARGIN_RIGHT);
			int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + marginHorizontal, layoutParams.width);
			int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + marginVertival, layoutParams.height);
			mContent.measure(childWidthSpec, childHeightSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();

		if (mHeader != null) {
			LayoutParams lp = mHeader.getLayoutParams();
			int left = paddingLeft + getMargin(lp, MARGIN_LEFT);
			int top = paddingTop + getMargin(lp, MARGIN_TOP) + mOffsetIndicator.getOffset() - mHeader.getMeasuredHeight();
			int right = left + mHeader.getMeasuredWidth();
			int bottom = top + mHeader.getMeasuredHeight();
			mHeader.layout(left, top, right, bottom);
		}

		if (mContent != null) {
			LayoutParams lp = mContent.getLayoutParams();
			int left = paddingLeft + getMargin(lp, MARGIN_LEFT);
			int top = paddingTop + getMargin(lp, MARGIN_TOP) + mOffsetIndicator.getOffset();
			int right = left + mContent.getMeasuredWidth();
			int bottom = top + mContent.getMeasuredHeight();
			mContent.layout(left, top, right, bottom);
		}
	}


	public void addHeaderView(View headerView) {
		if (mHeader != null && headerView != null && mHeader != headerView) {
			removeView(mHeader);
		}
		ViewGroup.LayoutParams lp = headerView.getLayoutParams();
		if (lp == null) {
			lp = new LayoutParams(-1, -2);
			headerView.setLayoutParams(lp);
		}
		mHeader = headerView;
		addView(headerView);
	}

	public void addContentView(View headerView) {
		if (mHeader != null && headerView != null && mHeader != headerView) {
			removeView(mHeader);
		}
		ViewGroup.LayoutParams lp = headerView.getLayoutParams();
		if (lp == null) {
			lp = new LayoutParams(-1, -2);
			headerView.setLayoutParams(lp);
		}
		mHeader = headerView;
		addView(headerView);
	}


	public void setContentHandler(ContentHandler contentHandler) {
		mContentHandler = contentHandler;
	}

	/*MARGIN UTIL START*/
	public static final int MARGIN_LEFT = 0;
	public static final int MARGIN_TOP = 1;
	public static final int MARGIN_RIGHT = 2;
	public static final int MARGIN_BOTTOM = 3;

	public int getMargin(LayoutParams layoutParams, int marginFlag) {
		if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
			return 0;
		}
		MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
		switch (marginFlag) {
			case MARGIN_LEFT:
				return marginLayoutParams.leftMargin;
			case MARGIN_TOP:
				return marginLayoutParams.topMargin;
			case MARGIN_RIGHT:
				return marginLayoutParams.rightMargin;
			case MARGIN_BOTTOM:
				return marginLayoutParams.bottomMargin;
			default:
				return 0;
		}
	}
	/*MARGIN UTIL END*/

	/*TOUCH EVENT HANDLE START*/
	private float mStartY = 0;
	private float mLastY = 0;
	private final int SCROLL_FIRST_UP = 1;
	private final int SCROLL_FIRST_DOWN = 2;
	private final int SCROLL_FIRST_NONE = 3;
	private int SCROLL_FIRST_STATE;
	private boolean mRefreshAbleWhenDown = false;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mStartY = ev.getY();
				mLastY = mStartY;
				SCROLL_FIRST_STATE = SCROLL_FIRST_NONE;
				mRefreshAbleWhenDown = mContentHandler.checkCanDoRefresh(this, mContent, mHeader);
				break;
			case MotionEvent.ACTION_MOVE:
				float fingerY = ev.getY();
				float offset = fingerY - mLastY;
				mLastY = fingerY;

				if (SCROLL_FIRST_STATE == SCROLL_FIRST_NONE && fingerY - mStartY != 0) {
					SCROLL_FIRST_STATE = fingerY - mStartY > 0 ? SCROLL_FIRST_DOWN : SCROLL_FIRST_UP;
				}

				if (mOffsetIndicator.getOffset() == 0) {
					if (mContentHandler.checkCanDoRefresh(this, mContent, mHeader) && mRefreshAbleWhenDown) {
						if (SCROLL_FIRST_STATE == SCROLL_FIRST_DOWN) {
							moveOffset(offset);
							STATE = STATE_PULL_DOWN;
						}
					}
				} else {
					moveOffset(offset);
					STATE = STATE_PULL_DOWN;
				}
				break;
			case MotionEvent.ACTION_UP:
				if (STATE == STATE_PULL_DOWN) {
					STATE = STATE_RELEASE;
					notifyStateChange();
				}
		}
		return super.dispatchTouchEvent(ev);
	}
	/*TOUCH EVENT HANDLE END*/

	private void notifyStateChange() {
		switch (STATE) {
			case STATE_NONE:
				mOffsetIndicator.reverseOffset();
				break;
			case STATE_PULL_DOWN:
				break;
			case STATE_RELEASE:
				mOffsetIndicator.reverseOffset();
				break;
			case STATE_REFRESHING:
				break;
		}
	}

	private void moveOffset(float offset) {
		mOffsetIndicator.calculateOffset(offset);
		requestLayout();
	}
}
