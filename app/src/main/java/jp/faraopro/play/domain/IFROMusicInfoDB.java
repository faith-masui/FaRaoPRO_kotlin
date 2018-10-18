package jp.faraopro.play.domain;

import java.util.List;

public interface IFROMusicInfoDB {
	public long insert(MusicInfoEx app);

	public int update(int id, MusicInfoEx app);

	public MusicInfoEx find(int id);

	public List<MusicInfoEx> findAll();

	public int delete(int id);

	public void deleteAll();

	public int getSize();
}
