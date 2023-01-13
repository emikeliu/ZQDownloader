package zq.downloader.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("${zq.public-path}")
public class WebController {
    @Value("${zq.public-path}")
    String path;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        modelAndView.addObject("login", path + "/login-process");
        return modelAndView;
    }

    @GetMapping({"/tasks", "/about", "/status", "/tasks", "/", ""})
    public String home() {
        return "forward:/html/index.html";
    }

}
