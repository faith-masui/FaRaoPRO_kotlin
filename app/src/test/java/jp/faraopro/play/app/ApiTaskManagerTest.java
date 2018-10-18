package jp.faraopro.play.app;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.faraopro.play.mclient.MCDefAction;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ksu on 2016/11/02.
 */
public class ApiTaskManagerTest {
    ApiTaskManager apiManager;

    @Before
    public void setUp() throws Exception {
        apiManager = new ApiTaskManager();
    }

    @After
    public void tearDown() throws Exception {
        apiManager.clearTask();
        apiManager = null;
    }

    @Test
    public void getTrackTaskSize() throws Exception {
        // 初期状態、期待値0
        assertThat(apiManager.getTrackTaskSize(), is(0));

        // チャンネル再生に関わらないタスクを追加、期待値+0
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_LIST, null, null));
        assertThat(apiManager.getTrackTaskSize(), is(0));

        // radio/rating を追加、期待値+1
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_RATING, null, null));
        assertThat(apiManager.getTrackTaskSize(), is(1));

        // download/cdn を追加、期待値+1
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_DOWNLOAD_CDN, null, null));
        assertThat(apiManager.getTrackTaskSize(), is(2));

        // GET CDN CONTENT を追加、期待値+1
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_TRACK_DL, null, null));
        assertThat(apiManager.getTrackTaskSize(), is(3));

        // GET CDN CONTENT を追加、期待値+1
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_ARTWORK_DL, null, null));
        assertThat(apiManager.getTrackTaskSize(), is(4));

        // チャンネル再生に関わらないタスクを追加、期待値+0
        apiManager.setTask(new ApiTask(MCDefAction.MCA_KIND_PATTERN_SCHEDULE, null, null));
        assertThat(apiManager.getTrackTaskSize(), is(4));

        // チャンネル再生に関わらないタスクを削除、期待値+0
        apiManager.poll(MCDefAction.MCA_KIND_PATTERN_SCHEDULE);
        assertThat(apiManager.getTrackTaskSize(), is(4));

        // radio/rating を削除、期待値-1
        apiManager.poll(MCDefAction.MCA_KIND_RATING);
        assertThat(apiManager.getTrackTaskSize(), is(3));

        // 初期化後、期待値0
        apiManager.clearTask();
        assertThat(apiManager.getTrackTaskSize(), is(0));
    }

}