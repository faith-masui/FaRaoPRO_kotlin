package jp.faraopro.play.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCFrameItem;
import jp.faraopro.play.mclient.MCFrameItemList;

/**
 * 再生履歴テーブルへのアクセスを行うクラス
 * 
 * @author AIM
 * 
 */
public class FROPatternContentDB {
	@SuppressWarnings("unused")
	private static final String TAG = FROPatternContentDB.class.getSimpleName();
	@SuppressWarnings("unused")
	private static final boolean DEBUG = true;

	static final String TABLE_NAME = "PATTERN_CONTENT_TABLE";
	static final String COLUMN_ID = "ID";
	static final String COLUMN_PATTERN_ID = "PATTERN_ID";
	static final String COLUMN_FRAME_ID = "FRAME_ID";
	static final String COLUMN_ONAIR_TIME = "ONAIR_TIME";
	static final String COLUMN_AUDIO_ID = "AUDIO_ID";
	static final String COLUMN_AUDIO_VERSION = "AUDIO_VERSION";
	static final String COLUMN_AUDIO_TIME = "AUDIO_TIME";
	static final String COLUMN_AUDIO_URL = "AUDIO_URL";
	// static final String COLUMN_TARGET_DATE = "TARGET_DATE";

	private static final String[] COLUMNS = { COLUMN_ID, COLUMN_PATTERN_ID, COLUMN_FRAME_ID, COLUMN_ONAIR_TIME,
			COLUMN_AUDIO_ID, COLUMN_AUDIO_VERSION, COLUMN_AUDIO_TIME, COLUMN_AUDIO_URL, };

	private SQLiteDatabase database;

	/**
	 * コンストラクタ
	 * 
	 * @param database
	 */
	public FROPatternContentDB(SQLiteDatabase database) {
		this.database = database;
	}

	public long insertAll(MCFrameItemList list) {
		long rowId = -1;
		if (list != null && list.size() > 0) {
			for (MCFrameItem mcfi : list) {
				List<MCAudioItem> audioList = mcfi.getItem(MCDefResult.MCR_KIND_PATTERN_AUDIO);
				if (audioList != null && audioList.size() > 0) {
					for (MCAudioItem mcai : audioList) {
						insert(mcfi, mcai);
					}
				}
			}
		}

		return rowId;
	}

	public long insert(MCFrameItem frame, MCAudioItem audio) {
		long rowId = -1;
		ContentValues values = new ContentValues();

		values.put(COLUMN_PATTERN_ID, frame.getmPatternId());
		values.put(COLUMN_FRAME_ID, frame.getString(MCDefResult.MCR_KIND_FRAME_ID));
		values.put(COLUMN_ONAIR_TIME, frame.getString(MCDefResult.MCR_KIND_ONAIR_TIME));
		values.put(COLUMN_AUDIO_ID, audio.getString(MCDefResult.MCR_KIND_AUDIO_ID));
		values.put(COLUMN_AUDIO_VERSION, audio.getString(MCDefResult.MCR_KIND_AUDIO_VERSION));
		values.put(COLUMN_AUDIO_TIME, audio.getString(MCDefResult.MCR_KIND_AUDIO_TIME));
		values.put(COLUMN_AUDIO_URL, audio.getString(MCDefResult.MCR_KIND_AUDIO_URL));

		rowId = database.insert(TABLE_NAME, null, values);

		return rowId;
	}

	// TODO update frame 複数の要素を同時に更新

	// TODO update audio 単一のオーディオデータだけ更新

	// public int update(MCFrameItem frame, MCAudioItem audio) {
	// String selection = COLUMN_TARGET_DATE + " = ?";
	// String param = item.getmTargetDate();
	// ContentValues values = new ContentValues();
	//
	// values.put(COLUMN_TARGET_DATE, item.getmTargetDate());
	// values.put(COLUMN_SCHEDULE_RULE,
	// item.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE));
	// values.put(COLUMN_SCHEDULE_PERIOD,
	// item.getString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD));
	// values.put(COLUMN_PATTERN_ID,
	// item.getString(MCDefResult.MCR_KIND_PATTERN_ID));
	// values.put(COLUMN_PATTERN_DIGEST,
	// item.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST));
	//
	// FRODebug.logD(getClass(), "---------- UPDATE START ----------", DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_TARGET_DATE = " +
	// item.getmTargetDate(), DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_SCHEDULE_RULE = " +
	// item.getString(MCDefResult.MCR_KIND_SCHEDULE_RULE), DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_SCHEDULE_PERIOD = " +
	// item.getString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD),
	// DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_PATTERN_ID = " +
	// item.getString(MCDefResult.MCR_KIND_PATTERN_ID), DEBUG);
	// FRODebug.logD(getClass(), "COLUMN_PATTERN_DIGEST = " +
	// item.getString(MCDefResult.MCR_KIND_PATTERN_DIGEST),
	// DEBUG);
	// FRODebug.logD(getClass(), "---------- UPDATE END ----------", DEBUG);
	//
	// return database.update(TABLE_NAME, values, selection, new String[] {
	// param });
	// }

	// public MCFrameItemList findFramesByPatternId(String patternId) {
	// MCFrameItemList frameList = null;
	// ArrayList<String> frameIdList = new ArrayList<String>();
	// String selection = COLUMN_PATTERN_ID + " = ?";
	// Cursor cursor = database.query(TABLE_NAME, new String[] { COLUMN_FRAME_ID
	// }, selection,
	// new String[] { patternId }, null, null, COLUMN_ONAIR_TIME + " ASC");
	// if (cursor != null) {
	// if (cursor.moveToFirst()) {
	// do {
	// String frameId = cursor.getString(0);
	// if (!frameIdList.contains(frameId)) {
	// frameIdList.add(frameId);
	// }
	// } while (cursor.moveToNext());
	// }
	// cursor.close();
	// }
	// if (frameIdList.size() > 0) {
	// frameList = new MCFrameItemList();
	// for (String s : frameIdList) {
	// MCFrameItem item = findFrameById(s);
	// frameList.add(item);
	// }
	// }
	//
	// return frameList;
	// }

	// public MCFrameItem findFrameById(String frameId, String onairTime) {
	// MCFrameItem item = null;
	// // String date = Utils.getNowDateString("yyyyMMdd");
	// String selection = COLUMN_FRAME_ID + " = ? AND " + COLUMN_ONAIR_TIME + "
	// = ?";
	// Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new
	// String[] { frameId, onairTime }, null, null,
	// COLUMN_ONAIR_TIME + " ASC");
	// if (cursor != null) {
	// if (cursor.moveToFirst()) {
	// item = setFrameDataFromCursor(cursor);
	//
	// // FRODebug.logD(getClass(), "---------- FIND BY FRAME ID START
	// ----------", DEBUG);
	// // FRODebug.logD(getClass(), "COLUMN_PATTERN_ID = " +
	// item.getmPatternId(), DEBUG);
	// // FRODebug.logD(getClass(), "COLUMN_FRAME_ID = " +
	// item.getString(MCDefResult.MCR_KIND_FRAME_ID),
	// // DEBUG);
	// // FRODebug.logD(getClass(), "COLUMN_ONAIR_TIME = " +
	// item.getString(MCDefResult.MCR_KIND_ONAIR_TIME),
	// // DEBUG);
	// // FRODebug.logD(getClass(), "COLUMN_TARGET_DATE = " +
	// item.getmTargetDate(), DEBUG);
	//
	// do {
	// MCAudioItem audioItem = setAudioDataFromCursor(cursor);
	// item.setItem(MCDefResult.MCR_KIND_PATTERN_AUDIO, audioItem);
	//
	// // FRODebug.logD(getClass(),
	// // "COLUMN_AUDIO_ID = " +
	// audioItem.getString(MCDefResult.MCR_KIND_AUDIO_ID), DEBUG);
	// // FRODebug.logD(getClass(),
	// // "COLUMN_AUDIO_VERSION = " +
	// audioItem.getString(MCDefResult.MCR_KIND_AUDIO_VERSION), DEBUG);
	// // FRODebug.logD(getClass(),
	// // "COLUMN_AUDIO_TIME = " +
	// audioItem.getString(MCDefResult.MCR_KIND_AUDIO_TIME), DEBUG);
	// // FRODebug.logD(getClass(),
	// // "COLUMN_AUDIO_URL = " +
	// audioItem.getString(MCDefResult.MCR_KIND_AUDIO_URL), DEBUG);
	//
	// } while (cursor.moveToNext());
	//
	// // FRODebug.logD(getClass(), "---------- FIND BY FRAME ID END
	// ----------", DEBUG);
	// }
	// cursor.close();
	// }
	//
	// return item;
	// }

	public MCAudioItem findAudioById(String audioId) {
		MCAudioItem item = null;
		String selection = COLUMN_AUDIO_ID + " = ?";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { audioId }, null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setAudioDataFromCursor(cursor);

				// FRODebug.logD(getClass(), "---------- FIND BY AUDIO ID START
				// ----------", DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_AUDIO_ID = " +
				// item.getString(MCDefResult.MCR_KIND_AUDIO_ID),
				// DEBUG);
				// FRODebug.logD(getClass(),
				// "COLUMN_AUDIO_VERSION = " +
				// item.getString(MCDefResult.MCR_KIND_AUDIO_VERSION), DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_AUDIO_TIME = " +
				// item.getString(MCDefResult.MCR_KIND_AUDIO_TIME),
				// DEBUG);
				// FRODebug.logD(getClass(), "COLUMN_AUDIO_URL = " +
				// item.getString(MCDefResult.MCR_KIND_AUDIO_URL),
				// DEBUG);
				// FRODebug.logD(getClass(), "---------- FIND BY AUDIO ID END
				// ----------", DEBUG);
			}
			cursor.close();
		}

		return item;
	}

	/*
	 * 特定パターンの割り込みフレームから、現在時刻に最も近い未来の割り込みフレームを返す
	 * 
	 * @param patternId 該当日のパターン ID
	 * 
	 * @return 割り込みフレーム
	 */
	// public MCFrameItem findNearestFrame(String patternId) {
	// Calendar current = Calendar.getInstance();
	// current.add(Calendar.MINUTE, 1);
	// return findSpecificDateFrame(patternId, Utils.getDateString(current,
	// "kk:mm"));
	// }

	/**
	 * パラメータの日付で最も早い時刻のパターンを返す
	 * 
	 * @param date
	 *            日付
	 * @return 1フレーム(パターン)
	 */
	public MCFrameItem findSpecificDateFrame(String patternId, String time) {
		if (TextUtils.isEmpty(patternId))
			return null;

		MCFrameItem item = null;
		String selection = COLUMN_PATTERN_ID + " = ? AND " + COLUMN_ONAIR_TIME + " >= ?";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { patternId, time }, null, null,
				COLUMN_ONAIR_TIME + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				item = setFrameDataFromCursor(cursor);
				do {
					MCFrameItem item2 = setFrameDataFromCursor(cursor);
					if (item2.getString(MCDefResult.MCR_KIND_FRAME_ID)
							.equalsIgnoreCase(item.getString(MCDefResult.MCR_KIND_FRAME_ID))
							&& item2.getString(MCDefResult.MCR_KIND_ONAIR_TIME)
									.equalsIgnoreCase(item.getString(MCDefResult.MCR_KIND_ONAIR_TIME))) {
						MCAudioItem audioItem = setAudioDataFromCursor(cursor);
						item.setItem(MCDefResult.MCR_KIND_PATTERN_AUDIO, audioItem);
					} else {
						break;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return item;
	}

	public List<MCFrameItem> findSpecificDateFrameList(String patternId) {
		if (TextUtils.isEmpty(patternId))
			return null;

		MCFrameItemList frameList = new MCFrameItemList();
		MCFrameItem frame = null;
		MCAudioItem audio = null;
		String selection = COLUMN_PATTERN_ID + " = ?";
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, selection, new String[] { patternId }, null, null,
				COLUMN_FRAME_ID + " ASC");
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					frame = setFrameDataFromCursor(cursor);
					audio = setAudioDataFromCursor(cursor);
					MCFrameItem parent = (frameList.size() > 0) ? frameList.get(frameList.size() - 1) : null;
					if (parent == null || !parent.getString(MCDefResult.MCR_KIND_FRAME_ID)
							.equals(frame.getString(MCDefResult.MCR_KIND_FRAME_ID))) {
						frameList.add(frame);
						parent = frame;
					}
					parent.setItem(MCDefResult.MCR_KIND_PATTERN_AUDIO, audio);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return frameList;
	}

    public List<MCAudioItem> findAllAudioNoDuplication() {
        List<MCAudioItem> audioItems = new ArrayList<>();
        Set<String> idSet = new HashSet<>();
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_PATTERN_ID + " ASC");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MCAudioItem audio = setAudioDataFromCursor(cursor);
                    if (!idSet.contains(audio.getString(MCDefResult.MCR_KIND_AUDIO_ID))) {
                        idSet.add(audio.getString(MCDefResult.MCR_KIND_AUDIO_ID));
                        audioItems.add(audio);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return audioItems;
    }

	public void showAllData() {
		Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, COLUMN_PATTERN_ID + " ASC");
		FRODebug.logI(getClass(), "---------- SHOW ALL START ----------", DEBUG);
		if (cursor != null) {
			FRODebug.logI(getClass(), "SIZE = " + cursor.getCount(), DEBUG);
			if (cursor.moveToFirst()) {
				do {
					MCFrameItem frame = setFrameDataFromCursor(cursor);
					FRODebug.logI(getClass(), "COLUMN_PATTERN_ID = " + frame.getmPatternId(), DEBUG);
					FRODebug.logI(getClass(), "COLUMN_FRAME_ID = " + frame.getString(MCDefResult.MCR_KIND_FRAME_ID),
							DEBUG);
					FRODebug.logI(getClass(), "COLUMN_ONAIR_TIME = " + frame.getString(MCDefResult.MCR_KIND_ONAIR_TIME),
							DEBUG);
					// FRODebug.logD(getClass(), "COLUMN_TARGET_DATE = " +
					// frame.getmTargetDate(), DEBUG);

					MCAudioItem audio = setAudioDataFromCursor(cursor);
					FRODebug.logI(getClass(), "COLUMN_AUDIO_ID = " + audio.getString(MCDefResult.MCR_KIND_AUDIO_ID),
							DEBUG);
					FRODebug.logI(getClass(),
							"COLUMN_AUDIO_VERSION = " + audio.getString(MCDefResult.MCR_KIND_AUDIO_VERSION), DEBUG);
					FRODebug.logI(getClass(), "COLUMN_AUDIO_TIME = " + audio.getString(MCDefResult.MCR_KIND_AUDIO_TIME),
							DEBUG);
					FRODebug.logI(getClass(), "COLUMN_AUDIO_URL = " + audio.getString(MCDefResult.MCR_KIND_AUDIO_URL),
							DEBUG);
					FRODebug.logI(getClass(), "---------- SEPARATE ----------", DEBUG);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		FRODebug.logI(getClass(), "---------- SHOW ALL END ----------", DEBUG);
	}

	// TODO delete frame frame単位で消去

	// TODO delete audio audio単位で消去

	public int deletebyPatternId(String patternId) {
		int ret = 0;
		String selection = COLUMN_PATTERN_ID + " = ?";
		ret = database.delete(TABLE_NAME, selection, new String[] { patternId });

		return ret;
	}

	public void deleteUnusingPatters(List<String> usingPatterns) {
		// 使用しているパターンが0以下なら全て消す
		if (usingPatterns == null || usingPatterns.size() < 1) {
			FRODebug.logD(getClass(), "delete all", DEBUG);
			deleteAll();
			return;
		}

		// そうでなければ個数に合わせて selection を作成
		String selection = "(" + COLUMN_PATTERN_ID + " != ?)";
		if (usingPatterns.size() > 1) {
			for (int i = 0; i < usingPatterns.size() - 1; i++) {
				selection += (" AND (" + COLUMN_PATTERN_ID + " != ?)");
			}
		}
		// 以下のような selection が pattern のサイズに合わせて作成される
		// (PATTERN_ID != ?) AND (PATTERN_ID != ?) AND (PATTERN_ID != ?) AND ...

		FRODebug.logD(getClass(), "selection = " + selection, DEBUG);
		for (String pattern : usingPatterns) {
			FRODebug.logD(getClass(), "pattern = " + pattern, DEBUG);
		}

		int deleted = database.delete(TABLE_NAME, selection, usingPatterns.toArray(new String[0]));
		FRODebug.logD(getClass(), "deleted = " + deleted, DEBUG);
	}

	public void deleteAll() {
		database.execSQL(FRODBHelper.DROP_TABLE_SQL + TABLE_NAME);
		database.execSQL(FRODBHelper.CREATE_PATTERN_CONTENT_TABLE_SQL);
    }

    public int getSize() {
        int size = 0;
        Cursor cursor = database.query(TABLE_NAME, COLUMNS, null, null, null, null, null);
        if (cursor != null) {
            size = cursor.getCount();
            cursor.close();
        }
        return size;
	}

	private MCFrameItem setFrameDataFromCursor(Cursor cursor) {
		MCFrameItem item = new MCFrameItem();
		int index = 0;
		index++; // COLUMN_ID
		item.setmPatternId(cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_FRAME_ID, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_ONAIR_TIME, cursor.getString(index++));
		index++; // MCR_KIND_AUDIO_ID
		index++; // MCR_KIND_AUDIO_VERSION
		index++; // MCR_KIND_AUDIO_TIME
		index++; // MCR_KIND_AUDIO_URL
		// item.setmTargetDate(cursor.getString(index++));

		return item;
	}

	private MCAudioItem setAudioDataFromCursor(Cursor cursor) {
		MCAudioItem item = new MCAudioItem();
		int index = 0;
		index++; // COLUMN_ID
		index++; // COLUMN_PATTERN_ID
		index++; // COLUMN_FRAME_ID
		index++; // COLUMN_ONAIR_TIME
		item.setString(MCDefResult.MCR_KIND_AUDIO_ID, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_AUDIO_VERSION, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_AUDIO_TIME, cursor.getString(index++));
		item.setString(MCDefResult.MCR_KIND_AUDIO_URL, cursor.getString(index++));

		return item;
	}

	// private MCFrameItem setDataFromCursor(Cursor cursor) {
	// MCScheduleItem item = new MCScheduleItem();
	// int index = 0;
	// // DB内のIDは不要のためスルー
	// index++;
	// item.setmTargetDate(cursor.getString(index++));
	// item.setString(MCDefResult.MCR_KIND_SCHEDULE_RULE,
	// cursor.getString(index++));
	// item.setString(MCDefResult.MCR_KIND_SCHEDULE_PERIOD,
	// cursor.getString(index++));
	// item.setString(MCDefResult.MCR_KIND_PATTERN_ID,
	// cursor.getString(index++));
	// item.setString(MCDefResult.MCR_KIND_PATTERN_DIGEST,
	// cursor.getString(index++));
	//
	// return item;
	// }

}
