package com.nowcoder.wenda.controller;

import com.nowcoder.wenda.aspect.LogAspect;
import com.nowcoder.wenda.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

//@Controller
public class IndexController {
    private static  final Logger logger= LoggerFactory.getLogger(LogAspect.class);

    @RequestMapping(path={"/","/index"},method = {RequestMethod.GET})   //如果返回“/”这样的地址，那就返回下面方法中的内容
    @ResponseBody  //说明返回的是字符串  而不是模板
    public String index(HttpSession httpSession){
        logger.info("Visit Home");

        return  "Hello nowcoder"+ httpSession.getAttribute("msg");
    }

    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value = "key",defaultValue ="nowcoder") String key){
        return String.format("Profile Page of %s / %d, type:%d key:%s",groupId,userId,type,key );
    }

    @RequestMapping(path={"/vm","/index"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value","price");
        List<String> colors = Arrays.asList(new String[] {"red","green","blue"});
        model.addAttribute("colors",colors);

        Map<String,String> map = new HashMap<>();
        for(int i=0;i<=4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("map",map);

//        model.addAttribute("user",new User("Lee"));
        return  "home";
    }

    @RequestMapping(path={"/request"},method = {RequestMethod.GET})
    @ResponseBody
    public String template(Model model,
                           HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession httpSession,
                           @CookieValue("JSESSIONID") String sessionId){  //通过注解的方式得到cookie里面的值
        StringBuilder sb =new StringBuilder();

        sb.append("COOKIEVALUE:" + sessionId);  //??????

        Enumeration<String> headerNames = request.getHeaderNames();//枚举
        while(headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            sb.append(name+":" +request.getHeader(name)+"<br>");
        }
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                sb.append("Cookie:" + cookie.getName() + " value:" + cookie.getValue());
            }
        }


        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI());

        response.addHeader("nowcoderId","hello");
        response.addCookie(new Cookie("username","nowcoder"));
//        response.getOutputStream().write();

        return sb.toString();
    }

//    跳转
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");

        RedirectView red = new RedirectView("/", true);
        if (code == 301) {
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return  red;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key)) {
            return "hello admin";
        }
        throw  new IllegalArgumentException("参数不对");  //抛出异常
    }

//    异常的捕获
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {

        return "error:" + e.getMessage();
    }
}
