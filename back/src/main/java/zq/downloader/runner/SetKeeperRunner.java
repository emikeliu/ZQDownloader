package zq.downloader.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import zq.downloader.utils.DownloadUtils;

import java.util.Map;

@Component
public class SetKeeperRunner implements ApplicationRunner {

    private Map<String, Object> keeper;

    @Autowired
    public void setKeeper(Map<String, Object> keeper) {
        this.keeper = keeper;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        DownloadUtils.setKeeper(keeper);
    }
}
