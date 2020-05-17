package cn.cestc.kafkaoracleconnectlog4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import cn.cestc.kafkaoracleconnectlog4j.service.*;

@RestController
public class LogParseController {
    @Autowired
    private LogParseService logParseService;


    @RequestMapping("/hello")
    public String hello() {
        return "Hello Spring Boot!";
    }

    @RequestMapping("/parse")
    public List getParseResult(HttpServletRequest request){
        String file = "E:\\kafkaconnectoraclelog4j\\src\\main\\resources\\a.txt";
        String filter = "oracle-logminer-connector2";
        int beginMinutes = 5;
        Map mp = request.getParameterMap();
        List res = null;
        if(mp.containsKey("file")){
            file = request.getParameter("file");
        }
        if(mp.containsKey("filter")){
            filter = request.getParameter("filter");
        }
        if(mp.containsKey("begin")){
            beginMinutes = Integer.parseInt(request.getParameter("begin"));
        }

        try {
            res = logParseService.getRecord(file,filter,beginMinutes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

}
