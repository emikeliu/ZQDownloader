package zq.downloader.utils;

import java.io.File;

public class PartitionUtils {
    public static String getPartInfo() {
        File file = new File("/");
        long available = file.getFreeSpace();
        long total = file.getTotalSpace();
        String totalStr = total * 1.0 / 1024 / 1024 / 1024 + "";
        long used = total - available;
        String usedStr = used * 1.0 / 1024 / 1024 / 1024 + "";
        return usedStr.substring(0, 4) + " " + totalStr.substring(0, 4);
    }
}
