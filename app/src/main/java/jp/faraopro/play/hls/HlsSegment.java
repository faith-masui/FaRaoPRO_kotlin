package jp.faraopro.play.hls;

/**
 * EXTINF
 */
public class HlsSegment {
	private long sequence;
	private double duration;
	private String title;
	private String keyMethod;
	private String keyPath;
	private byte[] keyContent;
	private byte[] keyIv;
	private String mediaPath;
	private byte[] mediaContent;
	private long rangeOffset;
	private long rangeLength;

	public HlsSegment() {
	}

	public HlsSegment(long sequence, double duration, String title, String keyMethod, String keyPath, byte[] keyContent,
			byte[] keyIv, String mediaPath, byte[] mediaContent, long rangeOffset, long rangeLength) {
		this.sequence = sequence;
		this.duration = duration;
		this.title = title;
		this.keyMethod = keyMethod;
		this.keyPath = keyPath;
		this.keyContent = keyContent;
		this.keyIv = keyIv;
		this.mediaPath = mediaPath;
		this.mediaContent = mediaContent;
		this.rangeOffset = rangeOffset;
		this.rangeLength = rangeLength;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyMethod() {
		return keyMethod;
	}

	public void setKeyMethod(String keyMethod) {
		this.keyMethod = keyMethod;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public byte[] getKeyContent() {
		return keyContent;
	}

	public void setKeyContent(byte[] keyContent) {
		this.keyContent = keyContent;
	}

	public byte[] getKeyIv() {
		return keyIv;
	}

	public void setKeyIv(byte[] keyIv) {
		this.keyIv = keyIv;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public byte[] getMediaContent() {
		return mediaContent;
	}

	public void setMediaContent(byte[] mediaContent) {
		this.mediaContent = mediaContent;
	}

	public long getRangeOffset() {
		return rangeOffset;
	}

	public void setRangeOffset(long rangeOffset) {
		this.rangeOffset = rangeOffset;
	}

	public long getRangeLength() {
		return rangeLength;
	}

	public void setRangeLength(long rangeLength) {
		this.rangeLength = rangeLength;
	}

}
