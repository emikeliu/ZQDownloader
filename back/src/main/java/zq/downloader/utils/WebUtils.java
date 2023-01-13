package zq.downloader.utils;

import okhttp3.OkHttpClient;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
    private static final OkHttpClient client = new OkHttpClient();

    public static long checkIsContinue(HttpServletRequest request) {
        String range = request.getHeader("RANGE");
        long skipped = -1L;
        if (range != null) {
            range = range.replace(" ", "");
            try {
                skipped = Long.parseLong(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
            } catch (Exception e) {
                return -1L;
            }
        }
        return skipped;
    }

}
