package jp.faraopro.play.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.domain.UserDataPreference;
import jp.faraopro.play.mclient.IMCResultInfo;
import jp.faraopro.play.mclient.MCDefResult;

public class FROUserData implements Parcelable {
	private static final boolean DEBUG = true;

	private long lastUpdate;
	private int serviceType;
	private int subscriptionType;
	private long remainingTime;
	private int playableTracks;
	private String trackingKey;
	private int serviceLevel;
	private int features;
	private int permissions;
	private int marketType;
	private long expiresDate;
	private long expiresLimit;

	public static final Parcelable.Creator<FROUserData> CREATOR = new Creator<FROUserData>() {
		@Override
		public FROUserData[] newArray(int size) {
			return new FROUserData[size];
		}

		@Override
		public FROUserData createFromParcel(Parcel source) {
			return new FROUserData(source);
		}
	};

	private FROUserData(Parcel source) {
		lastUpdate = source.readLong();
		serviceType = source.readInt();
		subscriptionType = source.readInt();
		remainingTime = source.readLong();
		playableTracks = source.readInt();
		trackingKey = source.readString();
		serviceLevel = source.readInt();
		features = source.readInt();
		permissions = source.readInt();
		marketType = source.readInt();
		expiresDate = source.readLong();
		expiresLimit = source.readLong();
	}

	public FROUserData(IMCResultInfo xmlResponse, long lastUpdate) {
		if (xmlResponse == null)
			return;

		this.lastUpdate = lastUpdate;
		String tmp = xmlResponse.getString(MCDefResult.MCR_KIND_SERVICE_TYPE);
		if (canParseToDigits(tmp))
			serviceType = Integer.parseInt(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_SUBSCRIPTION_TYPE);
		if (canParseToDigits(tmp))
			subscriptionType = Integer.parseInt(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_SUBSCRIPTION_STATUS);
		if (canParseToDigits(tmp))
			remainingTime = Long.parseLong(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_PLAYABLE_TRACKS);
		if (canParseToDigits(tmp))
			playableTracks = Integer.parseInt(tmp);
		trackingKey = xmlResponse.getString(MCDefResult.MCR_KIND_TRACKING_KEY);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_SERVICE_LEVEL);
		if (canParseToDigits(tmp))
			serviceLevel = Integer.parseInt(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_FEATURES);
		if (canParseToDigits(tmp))
			features = Integer.parseInt(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_PERMISSIONS);
		if (canParseToDigits(tmp))
			permissions = Integer.parseInt(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_MARKET_TYPE);
		if (canParseToDigits(tmp))
			marketType = Integer.parseInt(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_EXPIRES_DATE);
		if (canParseToDigits(tmp))
			expiresDate = Long.parseLong(tmp);
		tmp = xmlResponse.getString(MCDefResult.MCR_KIND_EXPIRES_LIMIT);
		if (canParseToDigits(tmp))
			expiresLimit = Long.parseLong(tmp);
	}

	private static boolean canParseToDigits(String str) {
		return !TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str);
	}

	public FROUserData(Context context) {
		lastUpdate = UserDataPreference.getLastUpdate(context);
		serviceType = UserDataPreference.getServiceType(context);
		subscriptionType = UserDataPreference.getSubscriptionType(context);
		remainingTime = UserDataPreference.getRemainingTime(context);
		playableTracks = UserDataPreference.getPlayableTracks(context);
		trackingKey = UserDataPreference.getTrackingKey(context);
		serviceLevel = UserDataPreference.getServiceLevel(context);
		features = UserDataPreference.getFeatures(context);
		permissions = UserDataPreference.getPermissions(context);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(lastUpdate);
		dest.writeInt(serviceType);
		dest.writeInt(subscriptionType);
		dest.writeLong(remainingTime);
		dest.writeInt(playableTracks);
		dest.writeString(trackingKey);
		dest.writeInt(serviceLevel);
		dest.writeInt(features);
		dest.writeInt(permissions);
		dest.writeInt(marketType);
		dest.writeLong(expiresDate);
		dest.writeLong(expiresLimit);
	}

	public void showAll() {
		FRODebug.logD(getClass(), "lastUpdate = " + lastUpdate, DEBUG);
		FRODebug.logD(getClass(), "serviceType = " + serviceType, DEBUG);
		FRODebug.logD(getClass(), "subscriptionType = " + subscriptionType, DEBUG);
		FRODebug.logD(getClass(), "remainingTime = " + remainingTime, DEBUG);
		FRODebug.logD(getClass(), "playableTracks = " + playableTracks, DEBUG);
		FRODebug.logD(getClass(), "trackingKey = " + trackingKey, DEBUG);
		FRODebug.logD(getClass(), "serviceLevel = " + serviceLevel, DEBUG);
		FRODebug.logD(getClass(), "features = " + features, DEBUG);
		FRODebug.logD(getClass(), "permissions = " + permissions, DEBUG);
		FRODebug.logD(getClass(), "marketType = " + marketType, DEBUG);
		FRODebug.logD(getClass(), "expiresDate = " + expiresDate, DEBUG);
		FRODebug.logD(getClass(), "expiresLimit = " + expiresLimit, DEBUG);
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public int getServiceType() {
		return serviceType;
	}

	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}

	public int getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(int subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public long getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}

	public int getPlayableTracks() {
		return playableTracks;
	}

	public void setPlayableTracks(int playableTracks) {
		this.playableTracks = playableTracks;
	}

	public String getTrackingKey() {
		return trackingKey;
	}

	public void setTrackingKey(String trackingKey) {
		this.trackingKey = trackingKey;
	}

	public int getServiceLevel() {
		return serviceLevel;
	}

	public void setServiceLevel(int serviceLevel) {
		this.serviceLevel = serviceLevel;
	}

	public int getFeatures() {
		return features;
	}

	public void setFeatures(int features) {
		this.features = features;
	}

	public int getPermissions() {
		return permissions;
	}

	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	public int getMarketType() {
		return marketType;
	}

	public void setMarketType(int marketType) {
		this.marketType = marketType;
	}

	public long getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(long expiresDate) {
		this.expiresDate = expiresDate;
	}

	public long getExpiresLimit() {
		return expiresLimit;
	}

	public void setExpiresLimit(long expiresLimit) {
		this.expiresLimit = expiresLimit;
	}

}
