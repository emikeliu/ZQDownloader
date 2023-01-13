package zq.downloader.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import zq.downloader.enumuration.TaskStatus;
import zq.downloader.utils.DownloadUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static zq.downloader.enumuration.TaskStatus.*;
import static zq.downloader.utils.DownloadUtils.encodeHexString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private final String UUID = java.util.UUID.randomUUID().toString();
    private Response response;
    private String taskName;
    private TaskStatus status;
    private double percent;
    private String url;
    private File file;
    private String sha256 = "NOT AVAILABLE";
    private boolean shared = false;


    @Override
    public int hashCode() {
        return taskName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName);
    }

    public boolean getSharedStatus() {
        return shared;
    }

    public boolean init(String path) {
        if (!initInner(path)) {
            taskName = "ERROR";
            status = ERROR;
            return false;
        }
        return true;
    }

    private boolean initInner(String path) {
        if (response == null) {
            if (!queryResponse()) return false;
        }
        ResponseBody body = response.body();
        if (body == null) return false;
        taskName = DownloadUtils.guessName(url, body);
        file = new File(path + taskName);
        if (file.exists()) return false;
        status = WAITING;
        return true;
    }

    private boolean queryResponse() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            response = client.newCall(request).execute();
            url = response.request().url().toString();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean cancel() {
        status = ABORTED;
        return file.delete();
    }

    public void initSha256() {
        try {
            MessageDigest digest = MessageDigest.getInstance("sha256");
            InputStream is = Files.newInputStream(file.toPath());
            byte[] b = new byte[1024 * 1024];
            int len;
            while ((len = is.read(b)) != -1) {
                digest.update(b, 0, len);
            }
            sha256 = encodeHexString(digest.digest());
            is.close();
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
