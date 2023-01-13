package zq.downloader.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zq.downloader.bean.Callback;
import zq.downloader.bean.Task;
import zq.downloader.bean.TaskDownloader;
import zq.downloader.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;

@Component
public class DownloadManager {

    private final ExecutorService pool;

    private final Map<String, Task> taskMap = new TreeMap<>();

    private final ShareManager manager;

    @Autowired
    public DownloadManager(ExecutorService pool, ShareManager manager) {
        this.pool = pool;
        this.manager = manager;
    }

    public synchronized void submit(Task task) {
        if (contains(task)) return;
        pool.submit(() -> {
            new TaskDownloader(task).start();
        });
        taskMap.put(task.getUUID(), task);
    }

    public synchronized Task queryTask(String UUID) {
        return taskMap.get(UUID);
    }

    public synchronized Set<Map.Entry<String, Task>> queryAll() {
        return taskMap.entrySet();
    }

    private synchronized boolean contains(Task task) {
        return taskMap.containsValue(task) && taskMap.containsKey(task.getUUID());
    }

    public synchronized void removeTask(Task task) {
        taskMap.remove(task.getUUID());
    }

    public Callback[] queryAllAsApiCallbacks() {
        manager.checkAllExpired();
        Set<Map.Entry<String, Task>> set = queryAll();
        Callback[] callbacks = new Callback[set.size()];
        int i = 0;
        for (Map.Entry<String, Task> entry : set) {
            Task task = entry.getValue();
            Callback callback = new Callback();
            callback.setSize(task.getFile().length());
            callback.setPercent(task.getPercent());
            callback.setStatus(task.getStatus().toString());
            callback.setTaskName(task.getTaskName());
            callback.setUrl(task.getUrl());
            callback.setSha256(task.getSha256());
            callback.setUuid(task.getUUID());
            callback.setShared(task.getSharedStatus());
            callbacks[i++] = callback;
        }
        return callbacks;
    }

    public void serveToResponse(HttpServletResponse response, String uuid, HttpServletRequest request) throws IOException {
        long skipped = WebUtils.checkIsContinue(request);
        boolean isContinue = skipped != -1L;
        response.setContentType("application/octet-stream");
        Task task = queryTask(uuid);
        if (task == null) {
            return;
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + task.getTaskName() + "\"");
        try (InputStream fis = Files.newInputStream(task.getFile().toPath()); OutputStream os = response.getOutputStream()) {
            if (isContinue) {
                long reallySkipped = fis.skip(skipped);
                response.setContentLengthLong(task.getFile().length() - reallySkipped);
                response.setHeader("Content-Range", "bytes " + reallySkipped + "-" + task.getFile().length() + "/" + task.getFile().length());
            } else {
                response.setContentLengthLong(task.getFile().length());
                response.setHeader("Accept-Ranges", "bytes");
                response.setHeader("Content-Length", String.valueOf(task.getFile().length()));
            }
            byte[] b = new byte[1024 * 1024];
            int len;
            while ((len = fis.read(b)) != -1) {
                os.write(b, 0, len);
            }
        }
    }

}
