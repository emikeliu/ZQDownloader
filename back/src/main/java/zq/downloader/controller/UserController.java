package zq.downloader.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zq.downloader.bean.Callback;
import zq.downloader.bean.request.ChangePasswordRequest;
import zq.downloader.bean.request.UserRegisterRequest;
import zq.downloader.service.AccountService;

@RestController
@RequestMapping("${zq.public-path}" + "/api/user")
public class UserController {

    private AccountService service;

    @Autowired
    public void setService(AccountService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public Callback register(@RequestBody UserRegisterRequest user) {
        Callback callback = new Callback();
        if (service.register(user)) {
            callback.setSuccess("0");
        } else {
            callback.setSuccess("1");
        }
        return callback;
    }

    @GetMapping("/username")
    public String username() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    @PostMapping("/change-password")
    public Callback changePassword(@RequestBody ChangePasswordRequest request) {
        Callback callback = new Callback();
        try {
            service.changePassword(request.getOldPassword(), request.getNewPassword(), username());
            callback.setSuccess("0");
        } catch (RuntimeException e) {
            callback.setSuccess("1");
        }
        return callback;
    }

}
