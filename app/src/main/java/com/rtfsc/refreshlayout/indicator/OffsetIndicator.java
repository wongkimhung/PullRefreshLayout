package com.rtfsc.refreshlayout.indicator;

import android.animation.ValueAnimator;

import com.rtfsc.refreshlayout.core.PullToRefreshView;

import java.lang.ref.WeakReference;

/**
 * @author HuangJx
 *         create on 2016/5/5.
 */
public class OffsetIndicator {
	private float mZuni = 1f;
	private long mTimeMills = 500;
	private int mOffset = 0;
	private int mMaxHeight = 0;
	private ValueAnimator animator;
	private WeakReference<PullToRefreshView> mPullToRefreshView;

	public OffsetIndicator(PullToRefreshView view) {
		this.mPullToRefreshView = new WeakReference<PullToRefreshView>(view);
	}

	public void setMaxHeight(int maxHeight) {
		mMaxHeight = maxHeight;
	}

	public void calculateOffset(float offset) {
		if (offset == 0) {
			return;
		}
		if (offset > 0) {
			mOffset += mZuni * offset;
		} else {
			mOffset += offset;
		}

		if (mOffset > mMaxHeight) {
			mOffset = mMaxHeight;
		}
		if (mOffset < 0) {
			mOffset = 0;
		}
	}

	/**
	 * 关闭
	 */
	public void reverseOffset() {
		if (animator == null || animator.isRunning()) {
			return;
		}
		animator.start();
	}

	public int getOffset() {
		return mOffset;
	}

	private void requestLayout() {
		if (mPullToRefreshView.get() != null) {
			mPullToRefreshView.get().requestLayout();
		}
	}

	public void enableCloseAnim() {
		animator = ValueAnimator.ofInt(mOffset, 0);
		animator.setDuration(mTimeMills);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mOffset = (int) animation.getAnimatedValue();

				if (mPullToRefreshView.get() == null) {
					animator.cancel();
				} else {
					requestLayout();
				}
			}
		});
	}
}
