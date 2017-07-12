package com.jblab.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * Created by damir on 11.07.17.
 */
@Controller
public class RegistrationController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @RequestMapping("/registration")
    public String getUniqueId() {
        logger.info("-------------------------------------");
        logger.info("Registration: new UID generating...");
        String uid = UUID.randomUUID().toString();
        logger.info("Registration: new UID:" + uid);
        return uid;
    }
}