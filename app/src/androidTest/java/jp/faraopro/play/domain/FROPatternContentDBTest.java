package jp.faraopro.play.domain;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import jp.faraopro.play.mclient.MCAudioItem;
import jp.faraopro.play.mclient.MCDefResult;
import jp.faraopro.play.mclient.MCFrameItem;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ksu on 2016/11/24.
 */
public class FROPatternContentDBTest {
    private Context context = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() throws Exception {
        FROPatternContentDBFactory.getInstance(context).deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        FROPatternContentDBFactory.getInstance(context).deleteAll();
    }

    @Test
    public void findAllAudioNoDuplication() throws Exception {
        FROPatternContentDB contentDB = FROPatternContentDBFactory.getInstance(context);

        // データ = 0のとき
        List<MCAudioItem> audioItemList = contentDB.findAllAudioNoDuplication();
        assertThat(contentDB.getSize(), is(0));
        assertThat(audioItemList.size(), is(0));

        // 重複データ無し、3件
        MCFrameItem frameItem = createFrame("1", "1", "00:00");
        contentDB.insert(frameItem, createAudio("1", "1", "10", "hogehoge1"));
        contentDB.insert(frameItem, createAudio("2", "1", "10", "hogehoge2"));
        contentDB.insert(frameItem, createAudio("3", "1", "10", "hogehoge3"));
        audioItemList = contentDB.findAllAudioNoDuplication();
        assertThat(contentDB.getSize(), is(3)); // レコード数を事前にチェックし、正しく insert 出来ていることを確認
        assertThat(audioItemList.size(), is(3));

        // 重複データ有り、+2件
        contentDB.insert(frameItem, createAudio("2", "1", "10", "hogehoge2"));
        contentDB.insert(frameItem, createAudio("3", "1", "10", "hogehoge3"));
        audioItemList = contentDB.findAllAudioNoDuplication();
        assertThat(contentDB.getSize(), is(5));
        assertThat(audioItemList.size(), is(3));

        // 重複データ有り(別フレーム)、+2件
        frameItem = createFrame("1", "2", "10:00");
        contentDB.insert(frameItem, createAudio("2", "1", "10", "hogehoge2"));
        contentDB.insert(frameItem, createAudio("3", "1", "10", "hogehoge3"));
        audioItemList = contentDB.findAllAudioNoDuplication();
        assertThat(contentDB.getSize(), is(7));
        assertThat(audioItemList.size(), is(3));

        // 重複データ有り(別パターン)、+2件
        frameItem = createFrame("2", "2", "10:00");
        contentDB.insert(frameItem, createAudio("2", "1", "10", "hogehoge2"));
        contentDB.insert(frameItem, createAudio("3", "1", "10", "hogehoge3"));
        audioItemList = contentDB.findAllAudioNoDuplication();
        assertThat(contentDB.getSize(), is(9));
        assertThat(audioItemList.size(), is(3));

        // パターン削除、-7件
        contentDB.deletebyPatternId("1");
        audioItemList = contentDB.findAllAudioNoDuplication();
        assertThat(contentDB.getSize(), is(2));
        assertThat(audioItemList.size(), is(2));
    }

    private MCFrameItem createFrame(String pattern, String frame, String time) {
        MCFrameItem frameItem = new MCFrameItem();
        frameItem.setmPatternId(pattern);
        frameItem.setString(MCDefResult.MCR_KIND_FRAME_ID, frame);
        frameItem.setString(MCDefResult.MCR_KIND_ONAIR_TIME, time);
        return frameItem;
    }

    private MCAudioItem createAudio(String id, String version, String time, String url) {
        MCAudioItem audioItem = new MCAudioItem();
        audioItem.setString(MCDefResult.MCR_KIND_AUDIO_ID, id);
        audioItem.setString(MCDefResult.MCR_KIND_AUDIO_VERSION, version);
        audioItem.setString(MCDefResult.MCR_KIND_AUDIO_TIME, time);
        audioItem.setString(MCDefResult.MCR_KIND_AUDIO_URL, url);
        return audioItem;
    }
}