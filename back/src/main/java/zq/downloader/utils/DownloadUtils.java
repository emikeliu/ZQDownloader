package zq.downloader.utils;

import okhttp3.Response;
import okhttp3.ResponseBody;
import zq.downloader.bean.Task;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;

import static zq.downloader.enumuration.TaskStatus.*;

public class DownloadUtils {

    private static final Map<String, String> map;
    private static Map<String, Object> keeper;

    static {
        try {
            //Load Map from disk.
            map = (Map<String, String>) new ObjectInputStream(DownloadUtils.class.getResourceAsStream("/mime_to_ext.ser")).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setKeeper(Map<String, Object> keeper) {
        DownloadUtils.keeper = keeper;
    }

    public static boolean download(Task task) {
        Response response = task.getResponse();
        File file = task.getFile();
        try (response; OutputStream os = new FileOutputStream(file); ResponseBody body = response.body()) {
            if (body == null) return false;
            InputStream is = body.byteStream();
            long expectedLength = body.contentLength();
            int len;
            long total = 0;
            byte[] b = new byte[1024 * 1024];
            while ((len = is.read(b)) != -1 && task.getStatus() == DOWNLOADING) {
                os.write(b, 0, len);
                keeper.put("alreadyDown", (Long) keeper.getOrDefault("alreadyDown", 0L) + len);
                if (expectedLength != -1) task.setPercent(total * 1.0 / expectedLength);
                total += len;
            }
            if (task.getStatus() == DOWNLOADING) {
                if (expectedLength == -1L) {
                    task.setPercent(1);
                    task.setStatus(FINISHED);
                } else {
                    task.setStatus(total == expectedLength ? FINISHED : ERROR);
                    task.setPercent(1);
                }

            }
            task.initSha256();
            return task.getStatus() == FINISHED;
        } catch (IOException e) {
            task.setStatus(ERROR);
            return false;
        }
    }

    public static String guessName(String url, ResponseBody body) {
        try {

            //Assert if valid.
            if (!url.startsWith("https://") && !url.startsWith("http://")) throw new RuntimeException("Invalid url!");
            //Whether the Url contains parameters from http get.
            boolean contains = url.contains("?");
            String former = url.substring(0, contains ? url.indexOf("?") : url.length());
            //If contains then try to get name from the parameters.
            if (contains) {
                String latter = url.substring(url.indexOf("?") + 1, url.length() - 1);
                String[] ps = latter.split("&");
                for (String s : ps) {
                    if (s.contains("=")) {
                        if (s.split("=")[0].equals("response-content-disposition")) {
                            //Try to decode the Url.
                            for (String v : URLDecoder.decode(s.split("=")[1], Charset.defaultCharset()).split(";")) {
                                if (v.contains("=") && v.split("=")[0].trim().equals("filename")) {
                                    return v.split("=")[1].trim();
                                }
                            }
                        }
                    }
                }
            }
            //Do not have such parameters. Try to find from Url.
            String endName;
            String[] split = former.split("/");
            endName = split[split.length - 1];
            //Try to add its extended name.
            String ext;
            try {
                ext = map.getOrDefault(Objects.requireNonNull(body.contentType()).type()
                        + "/"
                        + Objects.requireNonNull(body.contentType()).subtype(), "");
            } catch (NullPointerException e) {
                ext = "";
            }
            if (!endName.endsWith(ext)) return endName + ext;
            else return URLDecoder.decode(endName, Charset.defaultCharset());
        } catch (Exception e) {
            return "NO_NAME_ERROR";
        }
    }

    public static String encodeHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
