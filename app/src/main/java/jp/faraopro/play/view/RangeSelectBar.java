package jp.faraopro.play.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * ダブルシークバー
 * 
 * @author Aim
 * 
 */
public class RangeSelectBar extends OriginalView {

	// 定数

	// 属性名
	private final static String STR_ATTR_MAX = "max"; // 最大値
	private final static String STR_ATTR_MIN = "min"; // 最小値
	private final static String STR_ATTR_STEP = "step"; // 可変値
	private final static String STR_ATTR_DEFAULT_FIRST = "default_first"; // 左の初期値
	private final static String STR_ATTR_DEFAULT_LAST = "default_last"; // 右の初期値

	// その他
	private final static int NUM_MAX_HEIGHT = 30; // 高さの最大値(dip)
	private final static float NUM_BASE_BAR_HEIGHT_RATIO = 0.1f; // 全体のバーをどれだけ小さくするか
	private final static float NUM_SELECT_BAR_HEIGHT_RATIO = 0.3f; // 選択のバーをどれだけ小さくするか
	private final static int NUM_POINT_HIT_RADIUS = 15; // 最大最小を変更するエリアの半径(dip)
	private final static int NUM_WIDTH_KNOB = 20; // つまみの横幅(dip)
	private final static float NUM_KNOB_HEIGHT_RATIO = 0.1f; // つまみの高さをどれだけ小さくするか

	private enum HitAreaType {
		None, First // 最初
		, Last // 最後
		, Bar // バー
	}

	private int _padding = 0;
	private int _max = 2015;// + _padding; //最大値
	private int _min = 1960;// - _padding - 1; //最小値
	private int _step = 1; // 可変値

	private int _first = 1965; // 最初の値
	private int _last = 1995; // 最後の値
	private boolean _isLoop = false; // 左右がループするか

	private Point _prevDownPoint = null; // 前回押してた位置
	private HitAreaType _hitArea = HitAreaType.None; // 押してるエリア

	private int _widthKnob = 15; // つまみの横幅

	private onRangeSelectBarChangeListener _changeListener = null; // 変化発生時のリスナー

	// private int dispWidth = 100;
	private int viewWidth = 100;
	private float density;

	public void setDispWidth(float width) {
		density = width;
	}

	/**
	 * 最大値
	 * 
	 * @param _max
	 *            the _max to set
	 */
	public void setMax(int _max) {
		if (_max <= getMin()) {
			_max = getMin() + 1;
		}
		this._max = _max + 5;// (int)(_max + _padding * density);
	}

	/**
	 * 最大値
	 * 
	 * @return the _max
	 */
	public int getMax() {
		return _max;
	}

	/**
	 * 最小値
	 * 
	 * @param _min
	 *            the _min to set
	 */
	public void setMin(int _min) {
		if (_min >= getMax()) {
			_min = getMax() - 1;
		}
		if (_min < 0) {
			_min = 0;
		}
		this._min = _min - 6;// (int)(_min - _padding * density - 1);
	}

	/**
	 * 最小値
	 * 
	 * @return the _min
	 */
	public int getMin() {
		return _min;
	}

	/**
	 * 可変値
	 * 
	 * @param _step
	 *            the _step to set
	 */
	public void setStep(int _step) {
		this._step = _step;
	}

	/**
	 * 可変値
	 * 
	 * @return the _step
	 */
	public int getStep() {
		return _step;
	}

	/**
	 * 最初の値
	 * 
	 * @param _first
	 *            the _first to set
	 */
	public void setFirst(int _first) {
		this._first = _first;
	}

	/**
	 * 最初の値
	 * 
	 * @return the _first
	 */
	public int getFirst() {
		return _first;
	}

	/**
	 * 最後の値
	 * 
	 * @param _last
	 *            the _last to set
	 */
	public void setLast(int _last) {
		this._last = _last;
	}

	/**
	 * 最後の値
	 * 
	 * @return the _last
	 */
	public int getLast() {
		return _last;
	}

	/**
	 * 左右がループするか
	 * 
	 * @param _isLoop
	 *            the _isLoop to set
	 */
	public void setIsLoop(boolean _isLoop) {
		this._isLoop = _isLoop;
	}

	/**
	 * 左右がループするか
	 * 
	 * @return the _isLoop
	 */
	public boolean isLoop() {
		return _isLoop;
	}

	/**
	 * 前回押してた位置
	 * 
	 * @return the _prevDownPoint
	 */
	public Point getPrevDownPoint() {
		if (_prevDownPoint == null) {
			_prevDownPoint = new Point();
		}
		return _prevDownPoint;
	}

	/**
	 * getFirst/getLastの値を実際の横幅と最小最大値から計算するための係数を求める
	 * 
	 * @return
	 */
	public float getWidthRatio() {
		return (float) (getWidth()) / (float) (getMax() - getMin());
	}

	/**
	 * つまみの横幅
	 * 
	 * @return the _widthKnob
	 */
	public int getWidthKnob() {
		if (_widthKnob == 0) {
			_widthKnob = (int) (DtoInt(NUM_WIDTH_KNOB) * density);
		}
		return _widthKnob;
	}

	/**
	 * 変化発生時のリスナー
	 * 
	 * @param _changeListener
	 *            the _changeListener to set
	 */
	public void setOnRangeSelectBarChangeListener(onRangeSelectBarChangeListener _changeListener) {
		this._changeListener = _changeListener;
	}

	/**
	 * 変化発生時のリスナー
	 * 
	 * @return the _changeListener
	 */
	public onRangeSelectBarChangeListener getOnRangeSelectBarChangeListener() {
		return _changeListener;
	}

	/**
	 * 範囲を選択するバー
	 * 
	 * @param context
	 * @param attrs
	 */
	public RangeSelectBar(Context context, AttributeSet attrs) {
		super(context, attrs);

		String temp = null;

		// 最大値
		temp = attrs.getAttributeValue(null, STR_ATTR_MAX);
		if (temp != null) {
			setMax(DtoInt(temp));
		}
		// 最小値
		temp = attrs.getAttributeValue(null, STR_ATTR_MIN);
		if (temp != null) {
			setMin(DtoInt(temp));
		}
		// 可変値
		temp = attrs.getAttributeValue(null, STR_ATTR_STEP);
		if (temp != null) {
			setStep(DtoInt(temp));
		}

		// 最初の値の初期値
		temp = attrs.getAttributeValue(null, STR_ATTR_DEFAULT_FIRST);
		if (temp != null) {
			setFirst(DtoInt(temp));
		}
		// 最後の値の初期値
		temp = attrs.getAttributeValue(null, STR_ATTR_DEFAULT_LAST);
		if (temp != null) {
			setLast(DtoInt(temp));
		}

		// 不正な値を治す
		if (getFirst() < getMin()) {
			setFirst(getMin());
		}
		if (getLast() > getMax()) {
			setLast(getMax());
		}

	}

	/**
	 * サイズを決定する
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int spec_width = MeasureSpec.getSize(widthMeasureSpec);
		// int spec_width = dispWidth;
		int spec_height = MeasureSpec.getSize(heightMeasureSpec);

		// 一番小さいのを使う
		int mine = DtoInt(NUM_MAX_HEIGHT);
		int spec_size = Math.min(mine, spec_height);

		// サイズ設定
		setMeasuredDimension(spec_width, spec_size);
		// setMeasuredDimension(spec_width + (110 * dispWidth), spec_size);
		// viewWidth = spec_width + (110 * dispWidth);
		Log.e("spec_width", " = " + spec_width);
		Log.e("getWidth", " = " + getWidth());

	}

	public int getViewWidth() {
		return viewWidth;
	}

	/**
	 * 描画処理
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint(); // 描画フォーマット作成
		paint.setAntiAlias(true); // アンチエイリアス有効

		RectF rect;
		LinearGradient shader;

		// グラデーション色
		// int[] colors_base = {ColorToInt("#00034179")
		// , ColorToInt("#ff034179")
		// , ColorToInt("#ffffffff")};
		int[] colors_base = { ColorToInt("#00000000"), ColorToInt("#00000000"), ColorToInt("#00000000") };
		// int[] colors_select = {ColorToInt("#ffffffff")
		// , ColorToInt("#FF8DBAE2")
		// , ColorToInt("#ff034179")};
		int[] colors_select = { ColorToInt("#0d68b7"), ColorToInt("#067be2"), ColorToInt("#067be2") };
		int[] colors_point = { ColorToInt("#FFFFFFFF"), ColorToInt("#FF9EA5B6"), ColorToInt("#FFdddddd") };

		// ベース
		rect = new RectF(0, getHeight() * NUM_BASE_BAR_HEIGHT_RATIO, getWidth(),
				getHeight() * (1.0f - NUM_BASE_BAR_HEIGHT_RATIO));
		shader = new LinearGradient(0, 0, 0, getHeight(), colors_base, null, TileMode.CLAMP);
		paint.setShader(shader); // シェーダー登録
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		canvas.drawRoundRect(rect, 5, 5, paint);

		// 選択範囲
		if (getFirst() < getLast()) {
			// 通常状態
			rect = new RectF((getFirst() - getMin()) * getWidthRatio() - 1, getHeight() * NUM_SELECT_BAR_HEIGHT_RATIO,
					(getLast() - getMin()) * getWidthRatio() + 1, getHeight() * (1.0f - NUM_SELECT_BAR_HEIGHT_RATIO));
			shader = new LinearGradient(0, 0, 0, getHeight(), colors_select, null, TileMode.CLAMP);
			paint.setShader(shader); // シェーダー登録
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawRoundRect(rect, 5, 5, paint);
		} else {
			// ループ状態（左）
			rect = new RectF((getMin() - getMin()) * getWidthRatio(), getHeight() * NUM_SELECT_BAR_HEIGHT_RATIO,
					(getLast() - getMin()) * getWidthRatio(), getHeight() * (1.0f - NUM_SELECT_BAR_HEIGHT_RATIO));
			shader = new LinearGradient(0, 0, 0, getHeight(), colors_select, null, TileMode.CLAMP);
			paint.setShader(shader); // シェーダー登録
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawRoundRect(rect, 5, 5, paint);

			// ループ状態（右）
			rect = new RectF((getFirst() - getMin()) * getWidthRatio(), getHeight() * NUM_SELECT_BAR_HEIGHT_RATIO,
					(getMax() - getMin()) * getWidthRatio(), getHeight() * (1.0f - NUM_SELECT_BAR_HEIGHT_RATIO));
			canvas.drawRoundRect(rect, 5, 5, paint);
		}

		// 持つところの描画（左）
		rect = new RectF((getFirst() - getMin()) * getWidthRatio(), getHeight() * NUM_KNOB_HEIGHT_RATIO,
				(getFirst() - getMin()) * getWidthRatio(), getHeight() * (1.0f - NUM_KNOB_HEIGHT_RATIO));
		shader = new LinearGradient(0, 0, 0, getHeight(), colors_point, null, TileMode.CLAMP);
		paint.setShader(shader); // シェーダー登録
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);

		// MEMO つまみ○版
		canvas.drawCircle((getFirst() - getMin()) * getWidthRatio() - getWidthKnob() + 3,
				(getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f - NUM_KNOB_HEIGHT_RATIO)) / 2, 10 * density,
				paint);
		canvas.drawRoundRect(rect, 5, 5, paint);

		// MEMO つまみ△版
		// Path pathRight = new Path();
		// pathRight.moveTo(
		// (getFirst() - getMin()) * getWidthRatio() - getWidthKnob() + 1,
		// (getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f -
		// NUM_KNOB_HEIGHT_RATIO))/2 + 20
		// );
		// pathRight.lineTo(
		// (getFirst() - getMin()) * getWidthRatio() - getWidthKnob() + 1,
		// (getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f -
		// NUM_KNOB_HEIGHT_RATIO))/2 - 20
		// );
		// pathRight.lineTo(
		// (getFirst() - getMin()) * getWidthRatio() - getWidthKnob() + 20,
		// (getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f -
		// NUM_KNOB_HEIGHT_RATIO))/2
		// );
		// canvas.drawPath(pathRight, paint);

		// 持つところの描画（右）
		rect = new RectF((getLast() - getMin()) * getWidthRatio(), getHeight() * NUM_KNOB_HEIGHT_RATIO,
				(getLast() - getMin()) * getWidthRatio(), getHeight() * (1.0f - NUM_KNOB_HEIGHT_RATIO));

		// MEMO つまみ○版
		canvas.drawCircle((getLast() - getMin()) * getWidthRatio() + getWidthKnob() - 3,
				(getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f - NUM_KNOB_HEIGHT_RATIO)) / 2, 10 * density,
				paint);
		canvas.drawRoundRect(rect, 5, 5, paint);

		// MEMO つまみ△版
		// Path pathLeft = new Path();
		// pathLeft.moveTo(
		// (getLast() - getMin()) * getWidthRatio() + getWidthKnob() - 22,
		// (getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f -
		// NUM_KNOB_HEIGHT_RATIO)) / 2
		// );
		// pathLeft.lineTo(
		// (getLast() - getMin()) * getWidthRatio() + getWidthKnob() - 3,
		// (getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f -
		// NUM_KNOB_HEIGHT_RATIO)) / 2 + 20
		// );
		// pathLeft.lineTo(
		// (getLast() - getMin()) * getWidthRatio() + getWidthKnob() - 3,
		// (getHeight() * NUM_KNOB_HEIGHT_RATIO + getHeight() * (1.0f -
		// NUM_KNOB_HEIGHT_RATIO)) / 2 - 20
		// );
		// canvas.drawPath(pathLeft, paint);
	}

	/**
	 * タッチイベント
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean ret = false;

		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// ポイント保存
			getPrevDownPoint().set(x, y);
			// ドコを押してるか
			_hitArea = calcHitArea(x, y);
			ret = true;
			break;

		case MotionEvent.ACTION_MOVE:
			int diff = (int) ((getPrevDownPoint().x - x) / getWidthRatio());

			if (Math.abs(diff) >= getStep()) {
				// 動くときだけ

				// ステップで丸める
				diff = (diff / getStep()) * getStep();

				// 最初と最後の値を再計算
				switch (_hitArea) {
				case First:
					// 最初の値
					moveFirst(diff);
					break;
				case Last:
					// 最後の値
					moveLast(diff);
					break;
				case Bar:
					// バー
					moveBar(diff);
					break;

				default:
					break;
				}

				// 前回分として保存
				getPrevDownPoint().set(x, y);

				// 再描画
				invalidate();

				// イベント
				if (getOnRangeSelectBarChangeListener() != null) {
					getOnRangeSelectBarChangeListener().onProgressChanged(this, getFirst(), getLast());
				}
			}
			ret = true;
			break;

		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}

		if (ret) {
			return true;
		} else {
			return super.onTouchEvent(event);
		}
	}

	/**
	 * 触ってるエリアを判定する
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private HitAreaType calcHitArea(int x, int y) {
		HitAreaType ret = HitAreaType.Bar;
		int first = (int) ((getFirst() - getMin()) * getWidthRatio());
		int last = (int) ((getLast() - getMin()) * getWidthRatio());
		int range = DtoInt(NUM_POINT_HIT_RADIUS);
		int diff = 0;
		Log.v("RangeSelectHitArea", "x = " + x);
		Log.v("RangeSelectHitArea", "y = " + y);
		Log.v("RangeSelectHitArea", "first = " + first);
		Log.v("RangeSelectHitArea", "last = " + last);
		Log.v("RangeSelectHitArea", "range = " + range);

		// 最初の値のエリア
		diff = Math.abs(first - x);
		Log.v("RangeSelectHitArea", "diffFirst = " + diff);
		if (diff < range) {
			// hit
			ret = HitAreaType.First;
		}

		// 最後の値のエリア
		diff = Math.abs(last - x);
		Log.v("RangeSelectHitArea", "diffLast = " + diff);
		if (diff < range) {
			// hit
			ret = HitAreaType.Last;
		}

		return ret;
	}

	/**
	 * 最初の場所を移動させる
	 * 
	 * @param diff
	 */
	private void moveFirst(int diff) {
		if (getFirst() < getLast()) {
			// 通常の位置関係
			// if((getFirst() - diff + 5) >= getLast()){
			if ((getFirst() - diff + _padding) >= getLast()) {
				// 入れ違うのでなにもしない
			} else {
				// if( (getFirst() - diff ) < getMin() + _padding ) {
				if ((getFirst() - diff) < getMin() + 5) {
					setFirst(getMin() + 5);
				} else {
					setFirst(getFirst() - diff);
				}
			}
		} else {
			// //入れ替わってる時
			// if((getFirst() - diff) < getLast()){
			// //入れ違うのでなにもしない
			// }else{
			// setFirst(getFirst() - diff);
			// }
		}
	}

	/**
	 * 最後の場所を移動させる
	 * 
	 * @param diff
	 */
	private void moveLast(int diff) {
		if (getFirst() < getLast()) {
			// 通常の位置関係
			// if((getLast() - diff -5) <= getFirst()){
			if ((getLast() - diff - _padding) <= getFirst()) {
				// 入れ違うのでなにもしない
			} else {
				// if( (getLast() - diff ) > getMax() - _padding ) {
				if ((getLast() - diff) > getMax() - 5) {
					setLast(getMax() - 5);
				} else {
					setLast(getLast() - diff);
				}
			}
		} else {
			// 入れ替わってる時
			if ((getLast() - diff) > getFirst()) {
				// 入れ違うのでなにもしない
			} else {
				setLast(getLast() - diff);
			}
		}
	}

	/**
	 * 最初と最後の値を再計算する
	 * 
	 * @param diff
	 */
	private void moveBar(int diff) {
		// if(false){
		// //ループ
		// //左
		// if((getFirst() - diff) < getMin()){
		// //左にはみ出る
		// setFirst(getMax() - (getMin() - (getFirst() - diff)));
		// }else if((getFirst() - diff) > getMax()){
		// //右にはみ出る
		// setFirst(getMin() + ((getFirst() - diff) - getMax()));
		// }else{
		// setFirst(getFirst() - diff);
		// }
		// //右
		// if((getLast() - diff) < getMin()){
		// //左にはみ出る
		// setLast(getMax() - (getMin() - (getLast() - diff)));
		// }else if((getLast() - diff) > getMax()){
		// //右にはみ出る
		// setLast(getMin() + ((getLast() - diff) - getMax()));
		// }else{
		// setLast(getLast() - diff);
		// }
		// }else{
		// //止める
		// if((getFirst() - diff) < getMin()){
		// //左にはみ出る
		// setLast(getLast() - getFirst() + getMin());
		// setFirst(getMin());
		// }else if((getLast() - diff) > getMax()){
		// //右にはみ出る
		// setFirst(getMax() - (getLast() - getFirst()));
		// setLast(getMax());
		// }else{
		// setFirst(getFirst() - diff);
		// setLast(getLast() - diff);
		// }
		// }
	}

	public interface onRangeSelectBarChangeListener {
		void onProgressChanged(RangeSelectBar rangeSelectBar, int first, int last);

	}

}
