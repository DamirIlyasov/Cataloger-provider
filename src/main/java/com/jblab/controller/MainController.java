package com.jblab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by damir on 13.07.17.
 */
@Controller
public class MainController {
    @RequestMapping("/")
    public String getIndexPage() {
        return "index";
    }
}
