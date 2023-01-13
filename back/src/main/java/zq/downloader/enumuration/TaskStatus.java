package zq.downloader.enumuration;

public enum TaskStatus {
    WAITING(0), DOWNLOADING(1), FINISHED(2), ABORTED(3), ERROR(4);
    final int code;

    TaskStatus(int code) {
        this.code = code;
    }

}
