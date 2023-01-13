package zq.downloader.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zq.downloader.bean.Callback;
import zq.downloader.bean.SharedTask;
import zq.downloader.bean.Task;
import zq.downloader.manager.DownloadManager;
import zq.downloader.manager.ShareManager;
import zq.downloader.utils.DownloadUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class DownloadService {

    private DownloadManager downloadManager;

    private ShareManager shareManager;

    private Map<String, Object> keeper;

    private OkHttpClient client;

    @Value("${zq.filepath}")
    private String filePath;

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Autowired
    public void setDownloadManager(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    @Autowired
    public void setShareManager(ShareManager shareManager) {
        this.shareManager = shareManager;
    }

    @Autowired
    public void setKeeper(HashMap<String, Object> keeper) {
        this.keeper = keeper;
    }

    public Callback share(String uuid, String minutes) {
        Callback callback = new Callback();
        Task task = downloadManager.queryTask(uuid);
        if (task == null) {
            callback.setSuccess("1");
            return callback;
        }
        try {
            SharedTask sharedTask = new SharedTask(task, Integer.parseInt(minutes));
            shareManager.addShare(sharedTask);
        } catch (RuntimeException e) {
            callback.setSuccess("1");
            return callback;
        }
        callback.setSuccess("0");
        return callback;
    }

    public double getStatistic() {
        long s = (Long) keeper.getOrDefault("alreadyDown", 0L);
        if (s == 0L) return s;
        return s * 1.0 / 1024 / 1024;
    }

    public void directConnect(String url, HttpServletResponse response) {
        try (Response responseOk = client.newCall(new Request.Builder().url(url).build()).execute()) {
            assert responseOk.body() != null;
            response.setContentLengthLong(responseOk.body().contentLength());
            response.setHeader("Content-Disposition", "attachment; filename=" + DownloadUtils.guessName(url, responseOk.body()));
            int len;
            byte[] b = new byte[1024 * 1024];
            InputStream is = responseOk.body().byteStream();
            OutputStream os = response.getOutputStream();
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }

        } catch (IOException | RuntimeException ignored) {
            try {
                response.getWriter().println("Error!");
            } catch (IOException e) {
                throw new RuntimeException("Cannot invoke getWriter()!", e);
            }
        }
    }

    public void fetch(HttpServletResponse response, String uuid, HttpServletRequest request) {
        try {
            downloadManager.serveToResponse(response, uuid, request);
        } catch (IOException e) {
            throw new RuntimeException("Cannot serve file which uuid is " + uuid, e);
        }
    }

    public Callback cancel(String uuid) {
        Callback callback = new Callback();
        Task task = downloadManager.queryTask(uuid);
        if (task == null) {
            callback.setSuccess("1");
            return callback;
        }
        task.cancel();
        downloadManager.removeTask(task);
        callback.setSuccess("0");
        return callback;
    }

    public Callback newRequest(String url) {
        Callback callback = new Callback();
        Task task = new Task();
        task.setUrl(url);
        boolean init = task.init(filePath);
        if (init) {
            downloadManager.submit(task);
            callback.setSuccess("0");
            return callback;
        }
        callback.setSuccess("1");
        return callback;
    }

    public Callback[] queryAllTasks() {
        return downloadManager.queryAllAsApiCallbacks();
    }
}
