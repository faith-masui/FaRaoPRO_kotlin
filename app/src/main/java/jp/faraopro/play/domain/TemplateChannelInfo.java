package jp.faraopro.play.domain;

import jp.faraopro.play.common.Consts;
import jp.faraopro.play.mclient.MCBookmarkItem;
import jp.faraopro.play.mclient.MCDefResult;

/**
 * 楽曲情報格納クラス
 * 
 * @author Aim
 * 
 */
public class TemplateChannelInfo extends ChannelInfo {
	private String templateId;
	private String parentId;
	private String nameEn;
	private String sourceType;
	private String sourceName;
	private String sourceUrl;

	public TemplateChannelInfo() {
	}

	public TemplateChannelInfo(MCBookmarkItem item) {
		templateId = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_ID);
		channelName = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME);
		nameEn = item.getString(MCDefResult.MCR_KIND_CHANNELITEM_NAME_EN);
		sourceType = item.getString(MCDefResult.MCR_KIND_SOURCE_TYPE);
		sourceName = item.getString(MCDefResult.MCR_KIND_SOURCE_NAME);
		sourceUrl = item.getString(MCDefResult.MCR_KIND_SOURCE_URL);

		if (sourceType.equalsIgnoreCase(Consts.TEMPLATE_SOURCE_TYPE_FARAO)) {
			String tmp = sourceUrl.substring(18);
			String[] params = tmp.split("/");
			String range = null;
			mode = params[0];
			channelId = params[1];
			this.range = range;
			this.type = Consts.MUSIC_TYPE_NORMAL;
		}
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
		if (sourceType.equalsIgnoreCase(Consts.TEMPLATE_SOURCE_TYPE_FARAO)) {
			this.type = Consts.MUSIC_TYPE_NORMAL;
		}
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
		if (sourceType != null && sourceType.equalsIgnoreCase(Consts.TEMPLATE_SOURCE_TYPE_FARAO)) {
			String tmp = sourceUrl.substring(18);
			String[] params = tmp.split("/");
			String range = null;
			mode = params[0];
			channelId = params[1];
			this.range = range;
		}
	}
}
