package com.rtfsc.refreshlayout.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.rtfsc.refreshlayout.handler.HeaderHandler;

/**
 * @author HuangJx
 *         create on 2016/5/3.
 */
public class PullToRefreshView extends ViewGroup implements AbsListView.OnScrollListener {
	private final int STATE_NONE = 0;
	private final int STATE_REFRESHING = 1;
	private final int STATE_PULL_DOWN = 2;
	private final int STATE_RELEASE = 3;
	private final int STATE = STATE_NONE;

	private int mHeaderId;
	private int mContentId;
	private View mHeader;
	private View mContent;

	private int mHeaderViewHeight;


	private ImageView mWheel1, mWheel2;    //轮组图片组件
	private ImageView mRider;  //骑手图片组件
	private ImageView mSun, mBack1, mBack2;    //太阳、背景图片1、背景图片2

	private int mFirstItemIndex = 0;

	public PullToRefreshView(Context context) {
		super(context);
	}

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
		}
		// 测量content
		if (mContent != null) {
			ViewGroup.LayoutParams layoutParams = mContent.getLayoutParams();
			int marginVertival = getMargin(layoutParams, MARGIN_TOP) + getMargin(layoutParams, MARGIN_BOTTOM);
			int marginHorizontal = getMargin(layoutParams, MARGIN_LEFT) + getMargin(layoutParams, MARGIN_RIGHT);
			int childWidthSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingLeft() + getPaddingRight() + marginHorizontal, layoutParams.width);
			int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + marginVertival, layoutParams.height);
			mContent.measure(childWidthSpec, childHeightSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int offsetX = getCurrentX();
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();

		if (mHeader != null) {
			LayoutParams lp = mHeader.getLayoutParams();
			int left = paddingLeft + getMargin(lp, MARGIN_LEFT);
			int top = paddingTop + getMargin(lp, MARGIN_TOP) + offsetX - mHeader.getMeasuredHeight();
			int right = left + mHeader.getMeasuredWidth();
			int bottom = top + mHeader.getMeasuredHeight();
			mHeader.layout(left, top, right, bottom);
		}

		if (mContent != null) {
			LayoutParams lp = mContent.getLayoutParams();
			int left = paddingLeft + getMargin(lp, MARGIN_LEFT);
			int top = paddingTop + getMargin(lp, MARGIN_TOP) + offsetX;
			int right = left + mContent.getMeasuredWidth();
			int bottom = top + mContent.getMeasuredHeight();
			mContent.layout(left, top, right, bottom);
		}
	}


	private int getCurrentX() {
		return 0;
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


	private void refreshState() {
		switch (STATE) {
			case STATE_NONE:
				scrollTo(0, mHeaderViewHeight);
				break;
			case STATE_PULL_DOWN:
				break;
			case STATE_RELEASE:
				break;
			case STATE_REFRESHING:
				break;
		}
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		this.mFirstItemIndex = firstVisibleItem;
	}


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
}
