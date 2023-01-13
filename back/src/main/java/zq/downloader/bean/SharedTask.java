package zq.downloader.bean;

import zq.downloader.enumuration.TaskStatus;
import zq.downloader.utils.TimeUtils;

public class SharedTask {

    private final Task task;

    private final long validTime;

    private boolean expired = false;

    public SharedTask(Task task, int validMinutes) {
        if (task.getStatus() != TaskStatus.FINISHED) throw new RuntimeException("Only finished tasks can be shared.");
        this.task = task;
        this.validTime = System.currentTimeMillis() + TimeUtils.convertMinutesToMills(validMinutes);
        task.setShared(true);
    }

    public Task getTask() {
        return task;
    }

    public boolean isExpired() {
        if (expired) return true;
        if (System.currentTimeMillis() > validTime) {
            task.setShared(false);
            expired = true;
            return true;
        }
        return false;
    }

    public String getUUID() {
        return task.getUUID();
    }

}
