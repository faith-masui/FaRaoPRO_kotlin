package jp.faraopro.play.model;

public class ChannelParams {
	private static ChannelMode mode;
	private static int channelId;
	private static String range;
	private static int permission;

	public static void setParams(int mode, int channelId, String range, int permission) {
		ChannelParams.mode = ChannelMode.valueOfInt(mode);
		ChannelParams.channelId = channelId;
		ChannelParams.range = range;
		ChannelParams.permission = permission;
	}

	public static void setParams(int mode, int channelId, String range) {
		ChannelParams.mode = ChannelMode.valueOfInt(mode);
		ChannelParams.channelId = channelId;
		ChannelParams.range = range;
		ChannelParams.permission = 0xffff;
	}

	public static ChannelMode getMode() {
		return mode;
	}

	public static int getChaneelId() {
		return channelId;
	}

	public static String getChannelIdString() {
		return Integer.toString(channelId);
	}

	public static String getRange() {
		return range;
	}

	public static int getPermission() {
		return permission;
	}

	public static String getPermissionString() {
		return Integer.toString(permission);
	}
}
