package jp.faraopro.play.domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.faraopro.play.common.Consts;
import jp.faraopro.play.model.TimerInfo;

public class FRODBHelper extends SQLiteOpenHelper {
    @SuppressWarnings("unused")
    private static final String TAG = FRODBHelper.class.getSimpleName();
    @SuppressWarnings("unused")
    private static final boolean D = false;

    // database info
    private static final String DB_NAME = "FaraoPRODatabase";
    private static final int DB_VERSION = 10;

    // query
    // history table
    public static final String CREATE_MUSIC_HISTORY_TABLE_SQL = "create table " + FROMusicHistoryDB.TABLE_NAME
            + " (_id integer primary key autoincrement, "
            + FROMusicHistoryDB.COLUMN_ID + " integer, "
            + FROMusicHistoryDB.COLUMN_ARTIST + " text not null, "
            + FROMusicHistoryDB.COLUMN_TITLE + " text not null, "
            + FROMusicHistoryDB.COLUMN_RELEASE + " text not null, "
            + FROMusicHistoryDB.COLUMN_GENRE + " text not null, "
            + FROMusicHistoryDB.COLUMN_INFO + " text, "
            + FROMusicHistoryDB.COLUMN_URL + " text, "
            + FROMusicHistoryDB.COLUMN_JACKET + " text, "
            + FROMusicHistoryDB.COLUMN_THUMBNAIL + " text, "
            + FROMusicHistoryDB.COLUMN_KEYWORD + " text, "
            + FROMusicHistoryDB.COLUMN_RATING + " text not null, "
            + FROMusicHistoryDB.COLUMN_TIMESTAMP + " text not null, "
            + FROMusicHistoryDB.COLUMN_ARTIST_ID + " text not null, "
            + FROMusicHistoryDB.COLUMN_PARAMS + " text)";

    // unsent rating table
    public static final String CREATE_UNSENT_RATING_TABLE_SQL = "create table " + FROUnsentRatingDB.TABLE_NAME
            + " (_id integer primary key autoincrement, "
            + FROUnsentRatingDB.COLUMN_TRACKING_KEY + " text not null, "
            + FROUnsentRatingDB.COLUMN_MODE + " text not null, "
            + FROUnsentRatingDB.COLUMN_CHANNEL_ID + " text not null, "
            + FROUnsentRatingDB.COLUMN_RANGE + " text, "
            + FROUnsentRatingDB.COLUMN_TRACK_ID + " text not null, "
            + FROUnsentRatingDB.COLUMN_DECISION + " text not null, "
            + FROUnsentRatingDB.COLUMN_COMPLETE + " text not null, "
            + FROUnsentRatingDB.COLUMN_DURATION + " text not null, "
            + FROUnsentRatingDB.COLUMN_TIMESTAMP + " text not null, "
            + FROUnsentRatingDB.COLUMN_ERROR_REASON + " text)";

    // favorite channel table
    public static final String CREATE_FAVORITE_CHANNEL_TABLE_SQL = "create table " + FROFavoriteChannelDB.TABLE_NAME
            + " (" + FROFavoriteChannelDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROFavoriteChannelDB.COLUMN_MODE + " text, "
            + FROFavoriteChannelDB.COLUMN_CHANNEL_ID + " text, "
            + FROFavoriteChannelDB.COLUMN_RANGE + " text, "
            + FROFavoriteChannelDB.COLUMN_CHANNEL_NAME + " text, "
            + FROFavoriteChannelDB.COLUMN_SOURCE + " text, "
            + FROFavoriteChannelDB.COLUMN_SORT_INDEX + " integer, "
            + FROFavoriteChannelDB.COLUMN_SOURCE_TYPE + " integer, "
            + FROFavoriteChannelDB.COLUMN_SKIP_CONTROL + " integer, "
            + FROFavoriteChannelDB.COLUMN_PERMISSION + " integer)";

    // local timer table
    public static final String CREATE_TIMER_TABLE_SQL = "create table " + FROTimerDB.TABLE_NAME
            + " (" + FROTimerDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROTimerDB.COLUMN_TIME + " integer, "
            + FROTimerDB.COLUMN_TYPE + " integer, "
            + FROTimerDB.COLUMN_INDEX + " integer, "
            + FROTimerDB.COLUMN_WEEK + " text, "
            + FROTimerDB.COLUMN_NAME + " text, "
            + FROTimerDB.COLUMN_RESOURCE + " text, "
            + FROTimerDB.COLUMN_RESOURCE_NAME + " text, "
            + FROTimerDB.COLUMN_MODE + " integer, "
            + FROTimerDB.COLUMN_CHANNEL_ID + " integer, "
            + FROTimerDB.COLUMN_RANGE + " text, "
            + FROTimerDB.COLUMN_PERMISSION + " integer)";

    public static final String CREATE_TEMPLATE_FAVORITE_LIST_TABLE_SQL = "create table " + FROTemplateFavoriteListDB.TABLE_NAME
            + " (" + FROTemplateFavoriteListDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROTemplateFavoriteListDB.COLUMN_TEMPLATE_ID + " text not null, "
            + FROTemplateFavoriteListDB.COLUMN_TEMPLATE_TYPE + " text, "
            + FROTemplateFavoriteListDB.COLUMN_NAME + " text, "
            + FROTemplateFavoriteListDB.COLUMN_NAME_EN + " text)";

    public static final String CREATE_FAVORITE_TEMPLATE_TABLE_SQL = "create table " + FROFavoriteTemplateDB.TABLE_NAME
            + " (" + FROFavoriteTemplateDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROFavoriteTemplateDB.COLUMN_TEMPLATE_ID + " text not null, "
            + FROFavoriteTemplateDB.COLUMN_NAME + " text, "
            + FROFavoriteTemplateDB.COLUMN_NAME_EN + " text, "
            + FROFavoriteTemplateDB.COLUMN_SOURCE_TYPE + " text, "
            + FROFavoriteTemplateDB.COLUMN_SOURCE_NAME + " text, "
            + FROFavoriteTemplateDB.COLUMN_SOURCE_URL + " text, "
            + FROFavoriteTemplateDB.COLUMN_PARENT_ID + " text)";

    public static final String CREATE_TEMPLATE_TIMER_AUTO_LIST_TABLE_SQL = "create table " + FROTemplateTimerAutoListDB.TABLE_NAME
            + " (" + FROTemplateTimerAutoListDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROTemplateTimerAutoListDB.COLUMN_TEMPLATE_ID + " text not null, "
            + FROTemplateTimerAutoListDB.COLUMN_TEMPLATE_TYPE + " text, "
            + FROTemplateTimerAutoListDB.COLUMN_NAME + " text, "
            + FROTemplateTimerAutoListDB.COLUMN_NAME_EN + " text, "
            + FROTemplateTimerAutoListDB.COLUMN_DIGEST + " text, "
            + FROTemplateTimerAutoListDB.COLUMN_ACTION + " text, "
            + FROTemplateTimerAutoListDB.COLUMN_RULE + " text)";

    public static final String CREATE_TIMER_AUTO_TABLE_SQL = "create table " + FROTimerAutoDB.TABLE_NAME
            + " (" + FROTimerAutoDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROTimerAutoDB.COLUMN_TIME + " integer, "
            + FROTimerAutoDB.COLUMN_TYPE + " integer, "
            + FROTimerAutoDB.COLUMN_INDEX + " integer, "
            + FROTimerAutoDB.COLUMN_WEEK + " text, "
            + FROTimerAutoDB.COLUMN_NAME + " text, "
            + FROTimerAutoDB.COLUMN_RESOURCE + " text, "
            + FROTimerAutoDB.COLUMN_RESOURCE_NAME + " text, "
            + FROTimerAutoDB.COLUMN_MODE + " integer, "
            + FROTimerAutoDB.COLUMN_CHANNEL_ID + " integer, "
            + FROTimerAutoDB.COLUMN_RANGE + " text, "
            + FROTimerAutoDB.COLUMN_PERMISSION + " integer)";

    public static final String CREATE_PATTERN_SCHEDULE_TABLE_SQL = "create table " + FROPatternScheduleDB.TABLE_NAME
            + " (" + FROPatternScheduleDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROPatternScheduleDB.COLUMN_TARGET_DATE + " text, "
            + FROPatternScheduleDB.COLUMN_SCHEDULE_RULE + " text, "
            + FROPatternScheduleDB.COLUMN_SCHEDULE_PERIOD + " text, "
            + FROPatternScheduleDB.COLUMN_PATTERN_ID + " text, "
            + FROPatternScheduleDB.COLUMN_PATTERN_DIGEST + " text, "
            + FROPatternScheduleDB.COLUMN_PATTERN_LAST_UPDATE + " text, "
            + FROPatternScheduleDB.COLUMN_PATTERN_UPDATE_STATUS + " text, "
            + FROPatternScheduleDB.COLUMN_SCHEDULE_MASK + " text)";

    public static final String CREATE_PATTERN_CONTENT_TABLE_SQL = "create table " + FROPatternContentDB.TABLE_NAME
            + " (" + FROPatternContentDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROPatternContentDB.COLUMN_PATTERN_ID + " text, "
            + FROPatternContentDB.COLUMN_FRAME_ID + " text, "
            + FROPatternContentDB.COLUMN_ONAIR_TIME + " text, "
            + FROPatternContentDB.COLUMN_AUDIO_ID + " text, "
            + FROPatternContentDB.COLUMN_AUDIO_VERSION + " text, "
            + FROPatternContentDB.COLUMN_AUDIO_TIME + " text, "
            + FROPatternContentDB.COLUMN_AUDIO_URL + " text)";

    public static final String CREATE_CHANNEL_HISTORY_TABLE_SQL = "create table " + FROChannelHistoryDB.TABLE_NAME
            + " (" + FROChannelHistoryDB.COLUMN_ID + " integer primary key autoincrement, "
            + FROChannelHistoryDB.COLUMN_CHANNEL_NAME + " text, "
            + FROChannelHistoryDB.COLUMN_CHANNEL_NAME_EN + " text, "
            + FROChannelHistoryDB.COLUMN_MODE + " text, "
            + FROChannelHistoryDB.COLUMN_CHANNEL_ID + " text, "
            + FROChannelHistoryDB.COLUMN_RANGE + " text, "
            + FROChannelHistoryDB.COLUMN_TIMESTAMP + " integer)";

    // good history table
    public static final String CREATE_GOOD_HISTORY_TABLE_SQL = "create table " + FROGoodHistoryDB.TABLE_NAME
            + " (_id integer primary key autoincrement, " + FROGoodHistoryDB.COLUMN_ID + " integer, "
            + FROGoodHistoryDB.COLUMN_ARTIST + " text not null, "
            + FROGoodHistoryDB.COLUMN_TITLE + " text not null, "
            + FROGoodHistoryDB.COLUMN_RELEASE + " text not null, "
            + FROGoodHistoryDB.COLUMN_GENRE + " text not null, "
            + FROGoodHistoryDB.COLUMN_INFO + " text, "
            + FROGoodHistoryDB.COLUMN_URL + " text, "
            + FROGoodHistoryDB.COLUMN_JACKET + " text, "
            + FROGoodHistoryDB.COLUMN_THUMBNAIL + " text, "
            + FROGoodHistoryDB.COLUMN_KEYWORD + " text, "
            + FROGoodHistoryDB.COLUMN_RATING + " text not null, "
            + FROGoodHistoryDB.COLUMN_TIMESTAMP + " text not null, "
            + FROGoodHistoryDB.COLUMN_ARTIST_ID + " text not null, "
            + FROGoodHistoryDB.COLUMN_PARAMS + " text)";

    private static final String[] TABLE_CREATE_SQLS = {CREATE_MUSIC_HISTORY_TABLE_SQL, CREATE_UNSENT_RATING_TABLE_SQL,
            CREATE_FAVORITE_CHANNEL_TABLE_SQL, CREATE_TIMER_TABLE_SQL, CREATE_TEMPLATE_FAVORITE_LIST_TABLE_SQL,
            CREATE_FAVORITE_TEMPLATE_TABLE_SQL, CREATE_TEMPLATE_TIMER_AUTO_LIST_TABLE_SQL, CREATE_TIMER_AUTO_TABLE_SQL,
            CREATE_PATTERN_SCHEDULE_TABLE_SQL, CREATE_PATTERN_CONTENT_TABLE_SQL, CREATE_CHANNEL_HISTORY_TABLE_SQL,
            CREATE_GOOD_HISTORY_TABLE_SQL,};

    public static final String DROP_TABLE_SQL = "drop table if exists ";

    public FRODBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String sql : TABLE_CREATE_SQLS) {
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2 && newVersion >= 2) {
            db.execSQL(CREATE_CHANNEL_HISTORY_TABLE_SQL);
        }
        if (oldVersion < 3 && newVersion >= 3) {
            safetyAlterTable(db, FROMusicHistoryDB.TABLE_NAME, FROMusicHistoryDB.COLUMN_PARAMS, "text");
        }
        if (oldVersion < 4 && newVersion >= 4) {
            db.execSQL(CREATE_GOOD_HISTORY_TABLE_SQL);
        }
        if (oldVersion < 5 && newVersion >= 5) {
            safetyAlterTable(db, FROFavoriteChannelDB.TABLE_NAME, FROFavoriteChannelDB.COLUMN_SKIP_CONTROL, "integer");
        }
        if (oldVersion < 8 && newVersion >= 8) {
            db.execSQL(DROP_TABLE_SQL + FROPatternScheduleDB.TABLE_NAME);
            db.execSQL(CREATE_PATTERN_SCHEDULE_TABLE_SQL);
            db.execSQL(DROP_TABLE_SQL + FROPatternContentDB.TABLE_NAME);
            db.execSQL(CREATE_PATTERN_CONTENT_TABLE_SQL);
        }
        if (oldVersion < 9 && newVersion >= 9) {
            safetyAlterTable(db, FROPatternScheduleDB.TABLE_NAME, FROPatternScheduleDB.COLUMN_SCHEDULE_MASK, "text");
        }
        if (oldVersion < 10 && newVersion >= 10) {
            safetyAlterTable(db, FROFavoriteChannelDB.TABLE_NAME, FROFavoriteChannelDB.COLUMN_PERMISSION, "integer DEFAULT " + 0xffff);
            upgrade10AtTimerDb(db);
            upgrade10AtTimerAutoDb(db);
        }
    }

    private void upgrade10AtTimerDb(SQLiteDatabase db) {
        Cursor cursor = findAll(db, FROTimerDB.TABLE_NAME, FROTimerDB.COLUMNS_VERSION_9, FROTimerDB.COLUMN_ID + " ASC");
        List<TimerInfo> localTimer = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TimerInfo info = new TimerInfo();
                    int index = 0;
                    info.setId(cursor.getInt(index++));
                    info.setTime(cursor.getInt(index++));
                    info.setType(cursor.getInt(index++));
                    info.setIndex(cursor.getInt(index++));
                    info.setWeek(Byte.parseByte(cursor.getString(index++)));
                    info.setName(cursor.getString(index++));
                    info.setResource(cursor.getString(index++));
                    info.setResourceName(cursor.getString(index++));
                    localTimer.add(info);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.execSQL(DROP_TABLE_SQL + FROTimerDB.TABLE_NAME);
        db.execSQL(CREATE_TIMER_TABLE_SQL);
        FROTimerDB localTimerDb = new FROTimerDB(db);
        if (localTimer != null && localTimer.size() > 0) {
            localTimer = splitResourceTo3Params(localTimer);
            for (TimerInfo timer : localTimer)
                localTimerDb.insert(timer);
        }
    }

    private void upgrade10AtTimerAutoDb(SQLiteDatabase db) {
        Cursor cursor = findAll(db, FROTimerAutoDB.TABLE_NAME, FROTimerAutoDB.COLUMNS_VERSION_9,
                FROTimerAutoDB.COLUMN_ID + " ASC");
        List<TimerInfo> autoTimer = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TimerInfo info = new TimerInfo();
                    int index = 0;
                    info.setId(cursor.getInt(index++));
                    info.setTime(cursor.getInt(index++));
                    info.setType(cursor.getInt(index++));
                    info.setIndex(cursor.getInt(index++));
                    info.setWeek(Byte.parseByte(cursor.getString(index++)));
                    info.setName(cursor.getString(index++));
                    info.setResource(cursor.getString(index++));
                    info.setResourceName(cursor.getString(index++));
                    autoTimer.add(info);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.execSQL(DROP_TABLE_SQL + FROTimerAutoDB.TABLE_NAME);
        db.execSQL(CREATE_TIMER_AUTO_TABLE_SQL);
        FROTimerAutoDB autoTimerDb = new FROTimerAutoDB(db);
        autoTimer = splitResourceTo3Params(autoTimer);
        if (autoTimer != null && autoTimer.size() > 0) {
            for (TimerInfo timer : autoTimer)
                autoTimerDb.insert(timer);
        }
    }

    private static Cursor findAll(SQLiteDatabase db, String tableName, String[] columns, String orderBy) {
        return db.query(tableName, columns, null, null, null, null, orderBy);
    }

    private static List<TimerInfo> splitResourceTo3Params(List<TimerInfo> oldFromat) {
        if (oldFromat == null || oldFromat.size() < 1)
            return null;
        for (TimerInfo oldItem : oldFromat) {
            if (oldItem.getType() == Consts.MUSIC_TYPE_NORMAL) {
                String resource = oldItem.getResource();
                String[] params = resource.split(",");
                oldItem.setMode(Integer.parseInt(params[0]));
                oldItem.setChannelId(Integer.parseInt(params[1]));
                if (params.length == 3) { // range 有り
                    oldItem.setRange(params[2]);
                }
            }
            oldItem.setPermission(0xffff);
        }
        return oldFromat;
    }

    /**
     * 指定したテーブルのカラム名リストを取得する
     *
     * @param db
     * @param tableName テーブル名
     * @return カラム名のリスト
     */
    private static List<String> getColumns(SQLiteDatabase db, String tableName) {
        List<String> columns = null;
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 1", null);
        if (cursor != null) {
            columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            cursor.close();
        }
        return columns;
    }

    /**
     * 指定したテーブルに指定したカラムが存在するか確認する
     *
     * @param db
     * @param tableName  テーブル名
     * @param columnName カラム名
     * @return true:カラムが存在する, false:それ以外
     */
    private static boolean isExistColumn(SQLiteDatabase db, String tableName, String columnName) {
        List<String> columns = getColumns(db, tableName);
        return columns != null && columns.contains(columnName);
    }

    /**
     * 既存テーブルに対するカラムの追加を安全に行うクラス<br>
     * 追加すべきカラムを事前にチェックし、既にテーブル内に存在している場合は alter を処理しない
     *
     * @param db
     * @param tableName  テーブル名
     * @param columnName カラム名
     * @param columnType カラムの型
     */
    private static void safetyAlterTable(SQLiteDatabase db, String tableName, String columnName, String columnType) {
        if (!isExistColumn(db, tableName, columnName)) {
            db.execSQL("alter table " + tableName + " add " + columnName + " " + columnType);
        }
    }
}
