package jp.faraopro.play.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NetworkUtilTest {
    private Context context = InstrumentationRegistry.getTargetContext();
    private String MOCK_FILE_NAME = "test";
    private String EXTERNAL_MOCK_FILE_PATH = Utils.getExternalDirectory() + "/" + MOCK_FILE_NAME;
    private String INTERNAL_MOCK_FILE_PATH = context.getFilesDir().getAbsolutePath() + "/" + MOCK_FILE_NAME;

    private void writeToMock(File targetMock, byte[] data) throws IOException {
        FileOutputStream fos = new FileOutputStream(targetMock);
        fos.write(data);
        fos.close();
    }

    private void deleteFileIfExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    @Before
    public void setUp() throws Exception {
        deleteFileIfExists(EXTERNAL_MOCK_FILE_PATH);
        context.deleteFile(MOCK_FILE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        deleteFileIfExists(EXTERNAL_MOCK_FILE_PATH);
        context.deleteFile(MOCK_FILE_NAME);
    }

    // NetworkUtil.writeResponseBodyToDisk
    @Test(expected = IllegalArgumentException.class)
    public void writeResponseBodyToDisk_nullPath() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToDisk(null, mockStream, 0, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeResponseBodyToDisk_nullContent() throws Exception {
        NetworkUtil.writeResponseBodyToDisk(EXTERNAL_MOCK_FILE_PATH, null, 0, null);
    }

    @Test
    public void writeResponseBodyToDisk_noLength() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToDisk(EXTERNAL_MOCK_FILE_PATH, mockStream, 0, null);
        File downloaded = new File(EXTERNAL_MOCK_FILE_PATH);
        assertThat(downloaded.length(), is(1L));

        deleteFileIfExists(EXTERNAL_MOCK_FILE_PATH);

        mockStream = new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        NetworkUtil.writeResponseBodyToDisk(EXTERNAL_MOCK_FILE_PATH, mockStream, 0, null);
        downloaded = new File(EXTERNAL_MOCK_FILE_PATH);
        assertThat(downloaded.length(), is(8L));
    }

    @Test
    public void writeResponseBodyToDisk_largeStream() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        NetworkUtil.writeResponseBodyToDisk(EXTERNAL_MOCK_FILE_PATH, mockStream, 1, null);
        File downloaded = new File(EXTERNAL_MOCK_FILE_PATH);
        assertThat(downloaded.length(), is(1L));
    }

    @Test
    public void writeResponseBodyToDisk_noError() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToDisk(EXTERNAL_MOCK_FILE_PATH, mockStream, 1, null);
        File downloaded = new File(EXTERNAL_MOCK_FILE_PATH);
        assertTrue(downloaded.exists());
    }

    // NetworkUtil.writeResponseBodyToApplicationPrivate
    @Test(expected = IllegalArgumentException.class)
    public void writeResponseBodyToApplicationPrivate_nullContext() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToApplicationPrivate(null, MOCK_FILE_NAME, mockStream, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeResponseBodyToApplicationPrivate_nullPath() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToApplicationPrivate(context, null, mockStream, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void writeResponseBodyToApplicationPrivate_nullContent() throws Exception {
        NetworkUtil.writeResponseBodyToApplicationPrivate(context, MOCK_FILE_NAME, null, 0);
    }

    @Test
    public void writeResponseBodyToApplicationPrivate_noLength() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToApplicationPrivate(context, MOCK_FILE_NAME, mockStream, 0);
        File downloaded = new File(INTERNAL_MOCK_FILE_PATH);
        assertThat(downloaded.length(), is(1L));

        context.deleteFile(MOCK_FILE_NAME);

        mockStream = new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        NetworkUtil.writeResponseBodyToApplicationPrivate(context, MOCK_FILE_NAME, mockStream, 0);
        downloaded = new File(INTERNAL_MOCK_FILE_PATH);
        assertThat(downloaded.length(), is(8L));
    }

    @Test
    public void writeResponseBodyToApplicationPrivate_largeStream() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
        NetworkUtil.writeResponseBodyToApplicationPrivate(context, MOCK_FILE_NAME, mockStream, 1);
        File downloaded = new File(INTERNAL_MOCK_FILE_PATH);
        assertThat(downloaded.length(), is(1L));
    }


    @Test
    public void writeResponseBodyToApplicationPrivate_noError() throws Exception {
        ByteArrayInputStream mockStream = new ByteArrayInputStream(new byte[]{0});
        NetworkUtil.writeResponseBodyToApplicationPrivate(context, MOCK_FILE_NAME, mockStream, 1);
        File downloaded = new File(INTERNAL_MOCK_FILE_PATH);
        assertTrue(downloaded.exists());
    }

    // NetworkUtil.checkFileSize
    @Test(expected = IllegalArgumentException.class)
    public void checkFileSizeWith_nullPath() throws Exception {
        NetworkUtil.checkFileSize(null, 0);
    }

    @Test(expected = FileNotFoundException.class)
    public void checkFileSize_emptyPath() throws Exception {
        NetworkUtil.checkFileSize("", 1);
    }

    @Test(expected = IOException.class)
    public void checkFileSize_differentLength() throws Exception {
        File mock = new File(EXTERNAL_MOCK_FILE_PATH);
        writeToMock(mock, new byte[]{0});
        NetworkUtil.checkFileSize(EXTERNAL_MOCK_FILE_PATH, 2);
    }

    @Test
    public void checkFileSize_deleteFile() throws Exception {
        File mock = new File(EXTERNAL_MOCK_FILE_PATH);
        writeToMock(mock, new byte[]{0});
        try {
            NetworkUtil.checkFileSize(EXTERNAL_MOCK_FILE_PATH, 2);
        } catch (IOException ioe) {
        }
        assertTrue(!mock.exists());
    }

    @Test
    public void checkFileSize_noError() throws Exception {
        File mock = new File(EXTERNAL_MOCK_FILE_PATH);
        writeToMock(mock, new byte[]{0});
        NetworkUtil.checkFileSize(EXTERNAL_MOCK_FILE_PATH, 1);
        assertTrue(mock.exists());
    }
}