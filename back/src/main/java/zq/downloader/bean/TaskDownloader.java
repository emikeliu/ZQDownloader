package zq.downloader.bean;

import zq.downloader.utils.DownloadUtils;

import static zq.downloader.enumuration.TaskStatus.DOWNLOADING;
import static zq.downloader.enumuration.TaskStatus.WAITING;

public class TaskDownloader {
    private final Task task;

    public TaskDownloader(Task task) {
        this.task = task;
    }

    public boolean start() {
        if (!checkAvailable()) return false;
        markTaskStatus();
        return DownloadUtils.download(task);
    }

    private boolean checkAvailable() {
        if (task == null) return false;
        return task.getStatus() == WAITING;
    }

    private void markTaskStatus() {
        task.setStatus(DOWNLOADING);
    }
}
