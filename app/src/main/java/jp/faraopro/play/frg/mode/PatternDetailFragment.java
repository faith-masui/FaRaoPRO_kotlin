package jp.faraopro.play.frg.mode;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.faraopro.play.R;
import jp.faraopro.play.act.newone.MainActivity;
import jp.faraopro.play.common.Consts;
import jp.faraopro.play.domain.FROPatternContentDBFactory;
import jp.faraopro.play.domain.FROPatternScheduleDBFactory;
import jp.faraopro.play.domain.MainPreference;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCFrameItem;
import jp.faraopro.play.mclient.MCScheduleItem;
import jp.faraopro.play.util.FROUtils;
import jp.faraopro.play.util.Utils;
import jp.faraopro.play.view.CustomListItem;

public class PatternDetailFragment extends ListBaseFragment {
	public static final String BUNDLE_TAG_DAY_TYPE = "DAY_TYPE";
	public static final int BUNDLE_PARAM_TODAY = 0;
	public static final int BUNDLE_PARAM_TOMORROW = 1;
	private int day;

	public static PatternDetailFragment newInstance(int dayType) {
		if (dayType != BUNDLE_PARAM_TODAY && dayType != BUNDLE_PARAM_TOMORROW)
			dayType = BUNDLE_PARAM_TODAY;
		PatternDetailFragment fragment = new PatternDetailFragment();
		Bundle args = new Bundle();
		args.putInt(LIST_TYPE, LIST_TYPE_PATTERN_DETAIL);
		args.putInt(BUNDLE_TAG_DAY_TYPE, dayType);
		fragment.setArguments(args);
		return fragment;
	}

	public static PatternDetailFragment newInstance() {
		return PatternDetailFragment.newInstance(BUNDLE_PARAM_TODAY);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			day = getArguments().getInt(BUNDLE_TAG_DAY_TYPE, BUNDLE_PARAM_TODAY);
		}
		view.findViewById(R.id.simplelist_lnr_margin).setVisibility(View.GONE);
	}

	@Override
	public void onStart() {
		super.onStart();
		getData();
	}

	@Override
	public void getData() {
		Calendar calender = Calendar.getInstance();
		if (day == BUNDLE_PARAM_TOMORROW)
			calender.add(Calendar.DAY_OF_MONTH, 1);
		MCScheduleItem schedule = FROPatternScheduleDBFactory.getInstance(getActivity())
				.findByDate(Utils.getDateString(calender, "yyyyMMdd"));
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		List<CustomListItem> frameList = null;
		if (schedule != null && !Consts.PATTERN_UPDATE_STATUS_NOT_BLONG.equals(schedule.getUpdateStatus())) {
			list.addAll(createPatternSection(getActivity(), calender, schedule));
			String patternId = schedule.getString(MCDefResult.MCR_KIND_PATTERN_ID);
			frameList = createFrameSection(getActivity(), calender,
					FROPatternContentDBFactory.getInstance(getActivity()).findSpecificDateFrameList(patternId));
			if (frameList != null)
				list.addAll(frameList);
		}
		list.add(0, createStatusSection(getActivity(), calender, schedule, frameList));
		setContents(list);
		setClickEvent(null);
		refresh();
	}

	private static CustomListItem createStatusSection(Context context, Calendar calender, MCScheduleItem schedule,
			List<CustomListItem> frameList) {
		CustomListItem element = new CustomListItem();
		if (schedule == null) {
			element.setLock(4);
			String notAcquired = context.getString(R.string.msg_pattern_is_not_acquired,
					Utils.getDateString(calender, "yyyy/MM/dd E "));
			element.setText(notAcquired);
			return element;
		}

		element.setLock(3);
		String patternText = null;
		if (Consts.PATTERN_UPDATE_STATUS_NOT_BLONG.equals(schedule.getUpdateStatus())) {
			patternText = context.getString(R.string.msg_interrupt_not_set);
			element.setText(patternText);
			element.setLock(4);
			return element;
		} else if (Consts.PATTERN_UPDATE_STATUS_OK.equals(schedule.getUpdateStatus())) {
			patternText = context.getString(R.string.msg_synchronized);
		} else if (Consts.PATTERN_UPDATE_STATUS_NG.equals(schedule.getUpdateStatus())) {
			patternText = context.getString(R.string.msg_not_synchronized);
		} else {
			patternText = context.getString(R.string.msg_failed_to_synchronize);
		}
		String frameText = null;
		if (frameList != null && frameList.size() > 1) {
			boolean isFailed = frameList.get(1).isNext();
			for (int i = 1; i < frameList.size(); i++) {
				CustomListItem item = frameList.get(i);
				if (isFailed != item.isNext()) {
					frameText = context.getString(R.string.msg_can_play_part);
					break;
				}
				frameText = (isFailed) ? context.getString(R.string.msg_can_not_play_all)
						: context.getString(R.string.msg_can_play_all);
			}
		} else {
			frameText = (Consts.PATTERN_UPDATE_STATUS_ALL_NG.equals(schedule.getUpdateStatus())
					|| Consts.PATTERN_UPDATE_STATUS_NG.equals(schedule.getUpdateStatus()))
							? context.getString(R.string.msg_can_not_play_all)
							: context.getString(R.string.msg_frame_is_not_registered);
		}
		element.setmContentTitle(patternText);
		element.setmContentTitleEn(frameText);
		return element;
	}

	private static ArrayList<CustomListItem> createPatternSection(Context context, Calendar targetDay,
			MCScheduleItem schedule) {
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		// section title
		list.add(createHeader(context.getString(R.string.cap_interrupt_pattern)));
		// target day
		list.add(createContent(context.getString(R.string.cap_target_date),
				Utils.getDateString(targetDay, "yyyy/MM/dd E")));
		String tmp = schedule.getString(MCDefResult.MCR_KIND_PATTERN_ID);
		String patternId = (!TextUtils.isEmpty(tmp)) ? tmp : context.getString(R.string.cap_none);
		String lastUpdateTime = schedule.getLastUpdate();
		String lastUpdateStatus = schedule.getUpdateStatus();
		boolean isFailed = (Consts.PATTERN_UPDATE_STATUS_NG.equals(lastUpdateStatus)
				|| Consts.PATTERN_UPDATE_STATUS_ALL_NG.equals(lastUpdateStatus));
		Calendar lastUpdateDay = Calendar.getInstance();
		lastUpdateDay.setTimeInMillis(Long.parseLong(lastUpdateTime));
		lastUpdateTime = Utils.getDateString(lastUpdateDay, "yyyy/MM/dd E kk:mm");
		// pattern id
		list.add(createContent(context.getString(R.string.cap_pattern_id), patternId));
		// last update
		list.add(createContent(context.getString(R.string.cap_last_update_time), lastUpdateTime, isFailed));
		// next update
		list.add(createContent(context.getString(R.string.cap_next_update_time),
				MainPreference.getInstance(context).getNextPatternUpdate()));
		return list;
	}

	private static ArrayList<CustomListItem> createFrameSection(Context context, Calendar targetDay,
			List<MCFrameItem> frameList) {
		ArrayList<CustomListItem> list = new ArrayList<CustomListItem>();
		// section title
		list.add(createHeader(context.getString(R.string.cap_interrupt_frame)));
		if (frameList == null || frameList.size() <= 0)
			return list;

		for (MCFrameItem frame : frameList) {
			List<MCAudioItem> audioList = frame.getItem(MCDefResult.MCR_KIND_PATTERN_AUDIO);
			Set<String> idSet = new HashSet<>();
			// Set に突っ込んで重複を排除
			for (MCAudioItem audioItem : audioList) {
				idSet.add(audioItem.getString(MCDefResult.MCR_KIND_AUDIO_ID));
			}
			int numberOfAll = idSet.size();
			int numberOfDownloaded = 0;
			for (String audioId : idSet) {
				if (FROUtils.existPackageFile(context, audioId))
					numberOfDownloaded++;
			}
			String numberOfFile = numberOfDownloaded + "/" + numberOfAll + context.getString(R.string.cap_file);
			list.add(createContent(context.getString(R.string.cap_frame_id),
					frame.getString(MCDefResult.MCR_KIND_FRAME_ID), context.getString(R.string.cap_number_of_files),
					numberOfFile, (numberOfAll != numberOfDownloaded)));
		}

		return list;
	}

	private static CustomListItem createHeader(String title) {
		CustomListItem element = new CustomListItem();
		element.setLock(0);
		element.setText(title);
		return element;
	}

	private static CustomListItem createContent(String title, String value) {
		return createContent(title, value, false);
	}

	private static CustomListItem createContent(String title, String value, boolean isFailed) {
		CustomListItem element = new CustomListItem();
		element.setLock(1);
		element.setmContentTitle(title);
		element.setmContentTitleEn(value);
		element.setNext(isFailed);
		return element;
	}

	private static CustomListItem createContent(String title, String value, String title2, String value2,
			boolean isFailed) {
		CustomListItem element = new CustomListItem();
		element.setLock(2);
		element.setmContentTitle(title);
		element.setmContentTitleEn(value);
		element.setmContentText(title2);
		element.setmContentTextEn(value2);
		element.setNext(isFailed);
		return element;
	}

	public void forceUpdate() {
		getData();
	}

	@Override
	public void onBackPress() {
		((MainActivity) getActivity()).showMode(MainActivity.SETTING);
	}
}
