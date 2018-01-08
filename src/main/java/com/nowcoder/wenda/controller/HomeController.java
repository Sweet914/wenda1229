package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.aspect.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @RequestMapping(path = {"/vm1", "/index1"}, method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value", "price");
        List<String> colors = Arrays.asList(new String[]{"red", "green", "blue"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i <= 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);


        return "jj";
    }
}