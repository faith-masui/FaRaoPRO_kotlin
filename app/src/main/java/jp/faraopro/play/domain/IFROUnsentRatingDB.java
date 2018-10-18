package jp.faraopro.play.domain;

import java.util.List;

public interface IFROUnsentRatingDB {
	public long insert(RatingInfo info);

	public int update(int id, RatingInfo info);

	public RatingInfo find(int id);

	public List<RatingInfo> findAll();

	public int delete(int id);

	public void deleteAll();

	public int getSize();
}
