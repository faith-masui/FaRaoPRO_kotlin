package jp.faraopro.play.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
	public interface ScrollListener {
		void onScrollToBottom(CustomScrollView scrollView);

		void onScrollToTop(CustomScrollView scrollView);
	}

	private ScrollListener listener;
	private int scrollBottomMargin = 0;

	public CustomScrollView(Context context) {
		super(context);
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defs) {
		super(context, attrs, defs);
	}

	public void setScrollListener(ScrollListener listener) {
		this.listener = listener;
	}

	public void setScrollBottomMargin(int value) {
		this.scrollBottomMargin = value;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		View content = getChildAt(0);
		if (listener == null)
			return;
		if (content == null)
			return;
		if (y + this.getHeight() >= content.getHeight() - scrollBottomMargin) {
			listener.onScrollToBottom(this);
		} else if (y == 0) {
			listener.onScrollToTop(this);
		}
	}
}
