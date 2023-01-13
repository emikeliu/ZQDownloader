package zq.downloader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import zq.downloader.bean.SharedTask;
import zq.downloader.manager.DownloadManager;
import zq.downloader.manager.ShareManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class ShareController {

    private final ShareManager shareManager;

    private final DownloadManager downloadManager;

    @Autowired
    public ShareController(ShareManager shareManager, DownloadManager downloadManager) {
        this.shareManager = shareManager;
        this.downloadManager = downloadManager;
    }

    @GetMapping("${zq.public-path}/share")
    public void share(String uuid, HttpServletResponse response, HttpServletRequest request) throws IOException {
        SharedTask task = shareManager.findTaskByUUID(uuid);
        if (task == null) {
            response.setStatus(404);
            return;
        }
        downloadManager.serveToResponse(response, task.getUUID(), request);
    }


}
