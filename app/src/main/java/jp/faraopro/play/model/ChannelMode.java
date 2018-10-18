package jp.faraopro.play.model;

public enum ChannelMode {
	NULL(0, "0"), GENRE(1, "1"), ARTIST(2, "2"), RELEASE(3, "3"), HOT(4, "4"), MYCHANNEL(5, "5"), SHARELINK(6,
			"6"), SHUFFLE(7, "7"), BENIFIT(8, "8"), BUSINESS(11, "11"),;

	public final int id;
	public final String text;

	ChannelMode(int id, String text) {
		this.id = id;
		this.text = text;
	}

	public static ChannelMode valueOfInt(int id) {
		for (ChannelMode mode : values()) {
			if (mode.id == id) {
				return mode;
			}
		}
		return NULL;
	}

	public static ChannelMode valueOfString(String text) {
		for (ChannelMode mode : values()) {
			if (mode.text.equals(text)) {
				return mode;
			}
		}
		return NULL;
	}
}
