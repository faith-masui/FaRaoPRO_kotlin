package jp.faraopro.play.domain;

import java.util.List;

import jp.faraopro.play.model.TimerInfo;

/**
 * タイマー用DBのインターフェース
 * 
 * @author AIM Corporation
 * 
 */
public interface ITimerDB {

	public long insert(TimerInfo info);

	public int update(TimerInfo info);

	public TimerInfo findNext(byte week, int time, boolean isExceptNow);

	public TimerInfo findPrevious(byte week, int time);

	public List<TimerInfo> findAll();

	public int delete(String id);

	public void deleteAll();

	public int getSize();

}
