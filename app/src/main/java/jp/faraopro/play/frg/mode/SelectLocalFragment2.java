package jp.faraopro.play.frg.mode;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.BaseActivity;
import jp.faraopro.play.act.newone.IModeActivity;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.app.FROForm;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

public class SelectLocalFragment2 extends ListBaseFragment {
	/** const **/

	/** view **/
	private TextView txtNodata;

	/** member **/
	private ArrayList<File> dirList;
	private ArrayList<File> fileList;
	private File[] fileAry;
	private File current;

	@Override
	protected void initViews(View view) {
		super.initViews(view);

		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.new_res_plane_text, null);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setOptionalView(0, v);
		txtNodata = (TextView) v.findViewById(R.id.planetext_txt_text);
		txtNodata.setText(R.string.msg_musicfile_is_not_found);
	}

	@Override
	public void getData() {
		getFileList();
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		for (File f : dirList) {
			CustomListItem item = new CustomListItem();
			item.setmName(f.getName());
			item.setmNameEn(f.getName());
			item.setNext(true);
			list.add(item);
		}
		for (File f : fileList) {
			CustomListItem item = new CustomListItem();
			item.setmName(f.getName());
			item.setmNameEn(f.getName());
			item.setNext(false);
			list.add(item);
		}
		if (list.size() > 0) {
			setContents(list);
			txtNodata.setVisibility(View.GONE);
		} else {
			setContents(new ArrayList<CustomListItem>());
			txtNodata.setVisibility(View.VISIBLE);
		}
		show();
	}

	@Override
	public void show() {
		setClickEvent(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 >= dirList.size()) {
					((BaseActivity) getActivity()).showToast(getString(R.string.msg_description_of_saving));
				} else {
					current = dirList.get(arg2);
					getData();
				}
			}
		});
		refresh();
		((MainActivity) getActivity()).setHeaderTitle(current.getName());
	}

	private void getFileList() {
		if (current == null)
			current = Environment.getExternalStorageDirectory();
		if (current.exists()) {
			fileAry = current.listFiles();
		}
		if (dirList != null)
			dirList.clear();
		dirList = new ArrayList<File>();
		if (fileList != null)
			fileList.clear();
		fileList = new ArrayList<File>();
		if (fileAry != null && fileAry.length > 0) {
			for (int i = 0; i < fileAry.length; i++) {
				if (fileAry[i].isDirectory()) {
					dirList.add(fileAry[i]);
				} else {
					if (Utils.isMusicFile(fileAry[i])) {
						fileList.add(fileAry[i]);
					}
				}
			}
		}
	}

	public void setCurrentDirectory() {
		if (fileList != null && fileList.size() > 0) {
			FROForm.getInstance().editedTimer.setType(Consts.MUSIC_TYPE_LOCAL);
			FROForm.getInstance().editedTimer.setResource(current.getAbsolutePath());
			FROForm.getInstance().editedTimer.setResourceName(current.getName());
			((IModeActivity) getActivity()).showMode(MainActivity.TIMER_EDIT);
			reset();
		} else {
			((BaseActivity) getActivity()).showToast(getString(R.string.msg_musicfile_is_not_found));
		}
	}

	public void reset() {
		dirList = null;
		fileList = null;
		fileAry = null;
		current = null;
	}

	@Override
	public void onBackPress() {
		if (current != null && current.getParentFile() != null && current.getParentFile().exists()
				&& !current.getPath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getPath())) {
			current = current.getParentFile();
			getData();
		} else {
			((IModeActivity) getActivity()).showMode(MainActivity.TIMER_SELECT_SOURCE);
		}
	}
}
