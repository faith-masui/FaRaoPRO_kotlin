package jp.faraopro.play.app;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;


/**
 * Created by ksu on 2016/09/24.
 */
public class FROPlayerTest {
    @Test
    public void getGainVolume() throws Exception {
        double volume;
        // 設定できる音量の上限値
        volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f);
        assertThat(volume, is(1.0));
        volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN, 0.9f);
        assertThat(volume, closeTo(0.839, 0.001));
        volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN, 0.8f);
        assertThat(volume, closeTo(0.690, 0.001));
        volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN, 0.7f);
        assertThat(volume, closeTo(0.552, 0.001));
        volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN, 0.6f);
        assertThat(volume, closeTo(0.428, 0.001));
        // 設定できる音量の下限値
        volume = FROPlayer.getGainVolume(FROPlayer.BASE_REPLAY_GAIN, 0.5f);
        assertThat(volume, closeTo(0.316, 0.001));
    }

    @Test
    public void getDuckVolume() throws Exception {
        float volume;
        // 設定できる duck 時の音量の上限値
        volume = FROPlayer.getDuckVolume(1.0f, 1.0f);
        assertThat(volume, is(1.0f));
        // 設定できる duck 時の音量の下限値
        volume = FROPlayer.getDuckVolume(1.0f, 0.0f);
        assertThat(volume, is(0.0f));
        // 設定できる duck 時の音量の中央値
        volume = FROPlayer.getDuckVolume(1.0f, 0.5f);
        assertThat(volume, is(0.5f));
    }

    @Test
    public void getChannelVolume() throws Exception {
        double volume;
        // duck の値が最大の場合、最終的な音量はスライダーの値にのみ依存
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f, 1.0f, true);
        assertThat(volume, is(1.0));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 0.8f, 1.0f, true);
        assertThat(volume, closeTo(0.690, 0.001));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 0.5f, 1.0f, true);
        assertThat(volume, closeTo(0.316, 0.001));

        // duck の値が最小の場合、最終的な音量は常に0.0
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f, 0.0f, true);
        assertThat(volume, is(0.0));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 0.8f, 0.0f, true);
        assertThat(volume, is(0.0));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 0.5f, 0.0f, true);
        assertThat(volume, is(0.0));

        // duck の値が半分の場合、最終的な音量は計算結果の更に半分
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f, 0.5f, true);
        assertThat(volume, is(0.5));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 0.8f, 0.5f, true);
        assertThat(volume, closeTo(0.345, 0.0005));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 0.5f, 0.5f, true);
        assertThat(volume, closeTo(0.158, 0.0005));

        // focus が奪われていない場合、最終的な音量はスライダーの値にのみ依存
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f, 1.0f, false);
        assertThat(volume, is(1.0));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f, 0.5f, false);
        assertThat(volume, is(1.0));
        volume = FROPlayer.getChannelVolume(FROPlayer.BASE_REPLAY_GAIN, 1.0f, 0.0f, false);
        assertThat(volume, is(1.0));
    }

}