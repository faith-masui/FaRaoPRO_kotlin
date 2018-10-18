package jp.faraopro.play.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {
	private Paint paint;

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
	}

	public CircleView(Context context) {
		super(context);
		paint = new Paint();
	}

	public void setColorResource(int color) {
		paint.setColor(getResources().getColor(color));
		invalidate();
	}

	public void setColor(int color) {
		paint.setColor(color);
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setAntiAlias(true);
		// 塗りつぶし無しにする
		paint.setStyle(Paint.Style.STROKE);
		final float scale = getContext().getResources().getDisplayMetrics().density;
		int strokePx = (int) (3 * scale + 0.5f);
		paint.setStrokeWidth(strokePx);
		canvas.drawCircle(canvas.getHeight() / 2, canvas.getHeight() / 2, (canvas.getWidth() / 2) - 3, paint);
	}
}