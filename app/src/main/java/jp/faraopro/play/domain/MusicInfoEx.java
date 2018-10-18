package jp.faraopro.play.domain;

public class MusicInfoEx {
	private MusicInfo musicInfo;
	private int id;
	private String value;
	private long timestamp;
	private String params;

	public MusicInfo getMusicInfo() {
		return musicInfo;
	}

	public void setMusicInfo(MusicInfo musicInfo) {
		this.musicInfo = musicInfo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long time) {
		this.timestamp = time;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}
