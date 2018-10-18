package jp.faraopro.play.app;

import java.util.LinkedList;

import jp.faraopro.play.BuildConfig;
import jp.faraopro.play.common.FRODebug;
import jp.faraopro.play.mclient.MCDefAction;
import jp.faraopro.play.util.FROUtils;

/**
 * 実行中のWebAPIのタスクを管理するクラス
 *
 * @author AIM Corporation
 */
public class ApiTaskManager {
    private static final boolean DEBUG = true;
    public static final Object QUEUE_LOCK = new Object();
    private LinkedList<ApiTask> taskList;
    public boolean isTerminate = false;

    public ApiTaskManager() {
        isTerminate = false;
        taskList = new LinkedList<>();
    }

    /**
     * タスクをキューに追加する
     *
     * @param task 追加するタスク
     * @return false:実行中のタスクがある場合, true:その他
     */
    public boolean setTask(ApiTask task) {
        synchronized (QUEUE_LOCK) {
            FRODebug.logD(getClass(), "setTask : " + MCDefAction.getApi(task.getAction()), DEBUG);
            boolean noTask = taskList.isEmpty();
            taskList.add(task);

            // showQueue();

            return noTask;
        }
    }

    public void setPeekTask(ApiTask task) {
        synchronized (QUEUE_LOCK) {
            FRODebug.logD(getClass(), "setPeekTask : " + MCDefAction.getApi(task.getAction()), DEBUG);
            taskList.addFirst(task);

            // showQueue();
        }
    }

    public void doTask() {
        synchronized (QUEUE_LOCK) {
            if (taskList != null && taskList.size() > 0) {
                ApiTask task = taskList.getFirst();
                task.getProcessing().run();
            }
        }
    }

    /**
     * キューの先頭のタスクを返す
     *
     * @return 先頭タスク
     */
    public ApiTask peek() {
        synchronized (QUEUE_LOCK) {
            return taskList.peek();
        }
    }

    public ApiTask poll(int when) {
        FRODebug.logD(getClass(), "poll", DEBUG);
        synchronized (QUEUE_LOCK) {
            ApiTask task = null;
            for (ApiTask t : taskList) {
                if (t.getAction() == when) {
                    task = t;
                    taskList.remove(t);
                    break;
                }
            }

            // showQueue();

            return task;
        }
    }

    /**
     * 引数で与えられたタスクより優先度の高いタスクを返す
     *
     * @param action タスク種別
     * @return 引数で与えられたタスクより優先度の高いタスク、該当がない場合は null を返す
     */
    public ApiTask hasHighPriorityTask(int action) {
        FRODebug.logD(getClass(), "hasHighPriorityTask", DEBUG);
        synchronized (QUEUE_LOCK) {
            ApiTask high = null;
            for (ApiTask task : taskList) {
                if (task.compare(action)) {
                    high = task;
                    taskList.remove(task);
                    break;
                }
            }

            return high;
        }
    }

    public boolean isContained(int when) {
        synchronized (QUEUE_LOCK) {
            boolean contain = false;
            for (ApiTask t : taskList) {
                if (t.getAction() == when) {
                    contain = true;
                    break;
                }
            }

            return contain;
        }
    }

    public boolean isFirst(ApiTask task) {
        synchronized (QUEUE_LOCK) {
            boolean isFirst = false;
            if (taskList != null) {
                ApiTask top = peek();
                if (top != null && top.equals(task))
                    isFirst = true;
            }

            return isFirst;
        }
    }

    public void clearTask() {
        synchronized (QUEUE_LOCK) {
            if (taskList != null)
                taskList.clear();
            taskList = new LinkedList<>();

            FRODebug.logD(getClass(), "clearTask", DEBUG);
            // showQueue();
        }
    }

    /**
     * キューのサイズを返す
     *
     * @return キューのサイズ
     */
    public int getSize() {
        synchronized (QUEUE_LOCK) {
            if (taskList != null)
                return taskList.size();
            else
                return -1;
        }
    }

    /**
     * 通常チャンネルの再生に関わるタスク(radio/rating, download/cdn, GET contents)の数を返却する
     *
     * @return 該当するタスクの数
     */
    public int getTrackTaskSize() {
        synchronized (QUEUE_LOCK) {
            if (taskList == null || taskList.size() < 1)
                return 0;
            int count = 0;
            for (ApiTask task : taskList) {
                if (task.getAction() == MCDefAction.MCA_KIND_RATING
                        || task.getAction() == MCDefAction.MCA_KIND_DOWNLOAD_CDN
                        || task.getAction() == MCDefAction.MCA_KIND_TRACK_DL
                        || task.getAction() == MCDefAction.MCA_KIND_ARTWORK_DL) {
                    count++;
                }
            }
            return count;
        }
    }

    public void showQueue(String message) {
        if (!BuildConfig.DEBUG)
            return;

        FROUtils.outputLog("message : " + message);
        if (taskList.size() > 0) {
            FRODebug.logI(getClass(), "---------- QUEUE START ----------", DEBUG);
            FROUtils.outputLog("---------- QUEUE START ----------");
        } else {
            FRODebug.logI(getClass(), " ", DEBUG);
            FRODebug.logI(getClass(), "queue size is zero", DEBUG);
            FRODebug.logI(getClass(), " ", DEBUG);
            FROUtils.outputLog(" \nqueue size is zero\n ");
        }
        for (ApiTask t : taskList) {
            FRODebug.logI(getClass(), "[ " + MCDefAction.getApi(t.getAction()) + " ]", DEBUG);
            FROUtils.outputLog("[ " + MCDefAction.getApi(t.getAction()) + " ]");
        }
        if (taskList.size() > 0) {
            FRODebug.logI(getClass(), "---------- QUEUE   END ----------", DEBUG);
            FROUtils.outputLog("---------- QUEUE   END ----------");
        }
    }
}
