package jp.faraopro.play.domain;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import jp.faraopro.play.R;

public class CustomPagerAdapter extends PagerAdapter {
	private Context context;

	public CustomPagerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object instantiateItem(View collection, int position) {
		ImageView image = new ImageView(context);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		image.setScaleType(ScaleType.CENTER_CROP);
		switch (position) {
		case 0:
			// image.setImageResource(R.drawable.new_bck_boot);
			break;
		case 1:
			// image.setImageResource(R.drawable.new_bck_boot2);
			break;
		case 2:
			// image.setImageResource(R.drawable.new_bck_boot3);
			break;
		}
		((ViewPager) collection).addView(image, position);

		return image;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		// ViewPagerに登録していたTextViewを削除する
		((ViewPager) collection).removeView((ImageView) view);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((ImageView) arg1);
		// turn false;
	}

}
