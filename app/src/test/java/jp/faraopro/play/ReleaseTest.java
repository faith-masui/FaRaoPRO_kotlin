package jp.faraopro.play;

import org.junit.Test;

import java.net.MalformedURLException;

import jp.faraopro.play.common.Flavor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ksu on 2016/09/23.
 */
public class ReleaseTest {

    @Test
    public void checkUrlHome() throws MalformedURLException {
        assertThat(Flavor.URL_TOP, is("http://www.faraopro.jp/mobile_pro_app%s/index.html"));
    }

    @Test
    public void checkUrlPlayScheme() throws MalformedURLException {
        assertThat(Flavor.PLAY_LINK, is("http://www.faraopro.jp/banner"));
    }

    @Test
    public void checkUrlApi() throws MalformedURLException {
        assertThat(Flavor.MC_URL_BASE, is("https://bpi.faraoradio.jp/v1/rest"));
    }

    @Test
    public void checkUrlContact() throws MalformedURLException {
        assertThat(Flavor.CONTACT_US_URL, is("http://www.faraopro.jp/other_app/call.html"));
    }

    @Test
    public void checkUrlFaq() throws MalformedURLException {
        assertThat(Flavor.FAQ_URL, is("http://www.faraopro.jp/other_app/faq.html"));
    }

}