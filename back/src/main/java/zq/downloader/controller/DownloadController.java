package zq.downloader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zq.downloader.bean.Callback;
import zq.downloader.bean.request.DownRequest;
import zq.downloader.service.DownloadService;
import zq.downloader.utils.PartitionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.Charset;

@RestController
@RequestMapping("${zq.public-path}/api")
public class DownloadController {

    private DownloadService service;

    @Autowired
    public void setService(DownloadService service) {
        this.service = service;
    }

    @GetMapping("/share")
    public Callback share(String uuid, String minutes) {
        return service.share(uuid, minutes);
    }

    @GetMapping("/statistic")
    public double downStatistic() {
        return service.getStatistic();
    }

    @GetMapping("/direct-connect")
    public void directConnect(HttpServletResponse response, String url) {
        url = URLDecoder.decode(url, Charset.defaultCharset());
        service.directConnect(url, response);
    }

    @GetMapping("/fetch")
    public void fetch(HttpServletResponse response, @Valid String uuid, HttpServletRequest request) {
        service.fetch(response, uuid, request);
    }


    @RequestMapping("/request-cancel")
    public Callback requestCancel(String uuid) {
        return service.cancel(uuid);
    }

    @PostMapping("/request-down")
    public Callback requestDown(@RequestBody DownRequest request) {
        return service.newRequest(request.getUrl());
    }

    @GetMapping("/status")
    public Callback[] status() {
        return service.queryAllTasks();
    }

    @GetMapping("/partition")
    public String partition() {
        return PartitionUtils.getPartInfo();
    }

}
