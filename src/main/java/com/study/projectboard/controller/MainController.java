package com.study.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.font.ShapeGraphicAttribute;


@Controller
public class MainController {

    @GetMapping("/")
    public String root(){
        return "forward:/articles";
    }
}
