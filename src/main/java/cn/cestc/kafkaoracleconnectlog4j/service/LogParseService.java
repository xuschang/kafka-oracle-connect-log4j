package cn.cestc.kafkaoracleconnectlog4j.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

@Service
public class LogParseService {
    //读文件
    public BufferedReader readFile(String fileName)throws Exception{
        File file = new File(fileName);
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(file));
        return br;
    }

    //文件行处理，得到满足条件的行

    /**
     *
     * @param fileName 文件名
     * @param connectName 过滤名
     * @param time  时间，间隔时间
     * @return
     * @throws Exception
     */
    public List<String> getRecord(String fileName, String connectName,int time)throws Exception{
        BufferedReader br = readFile(fileName);
        String regex = "\\[\\d{4}\\-\\d{2}\\-\\d{2} \\S{12}\\] .*";
        String timeBegin = getBeforeTime(time);
        List<String> result = new ArrayList();
        String strLine = null;
        String tem = null;
        while(null != (strLine = br.readLine())){
            if( strLine.matches(regex)){
                if(tem!=null) {
                    if (tem.contains(connectName) || tem.contains("main")) {
                        if (timeBegin.compareTo(tem) < 0) {
                            result.add(tem);
                        }
                    }
                }
                tem = strLine+"\n";
                continue;
            }
            if(null != tem) {
                tem += strLine;
                tem += "\n";
            }
        }
        if(tem.contains(connectName) || tem.contains("main")) {
            if(timeBegin.compareTo(tem)<0) {
                result.add(tem);
            }
        }
        return result;
    }

    public String getBeforeTime(int minutes){
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.MINUTE,-minutes);
        Date beforeDate = beforeTime.getTime();
        String before = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss,SSS]").format(beforeDate);
        return before;
    }
}
