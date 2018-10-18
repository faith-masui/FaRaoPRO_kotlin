package jp.faraopro.play.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FROTaskQueue {

	public enum TASK_RESULT {
		NOTADDED, COMPLETE, LOADING, FAILED;
	}

	private static final Map<String, TASK_RESULT> MAP_RESULT = new HashMap<String, TASK_RESULT>();

	synchronized public static boolean isAdded(String url) {

		return MAP_RESULT.containsKey(url);
	}

	synchronized public static void addTask(String url) {
		MAP_RESULT.put(url, TASK_RESULT.LOADING);
	}

	synchronized public static void addTask(String url, Runnable runnable) {
		MAP_RESULT.put(url, TASK_RESULT.LOADING);
		runnable.run();
	}

	synchronized public static void remove(String url) {
		if (MAP_RESULT.containsKey(url)) {
			MAP_RESULT.remove(url);
		}
	}

	private static final int limit_runnable = 10;

	public static void addRunnable(final Runnable runnable) {
		waitRunnables.add(new Runnable() {
			@Override
			public void run() {
				runnable.run();
				if (execRunnables.contains(this)) {
					execRunnables.remove(this);
				}
				checkRunnable();
			}
		});
		checkRunnable();
	}

	private static void checkRunnable() {
		// if(execRunnables.size() > limit_runnable) {
		if (MAP_RESULT.size() > limit_runnable) {
			// Log.e("checkRunnable", "+++++++++++++++++ wait, size = " +
			// MAP_RESULT.size());
			return;
		}
		if (waitRunnables.size() <= 0) {
			return;
		}
		// Log.e("checkRunnable", "+++++++++++++++++ go, size = " +
		// MAP_RESULT.size());
		Runnable runnable = waitRunnables.get(0);
		waitRunnables.remove(runnable);
		execRunnables.add(runnable);
		runnable.run();

	}

	private static final List<Runnable> execRunnables = new ArrayList<Runnable>();
	private static final List<Runnable> waitRunnables = new ArrayList<Runnable>();

}