package com.hawen;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

public class DeleteProgram {
    private static Logger log = LogManager.getLogger(DeleteProgram.class);

    public static void run(){
        Runtime runtime = Runtime.getRuntime();
        try{
            log.info("start search 80* port's pid");
            BufferedReader bf = new BufferedReader(new InputStreamReader(runtime.exec("netstat -ano").getInputStream(), Charset.forName("GBK")));
            String line = null;
            StringBuffer readLineBuffer = new StringBuffer();
            List<String> resultList = new ArrayList<String>();
            log.info("start read in......");
            while((line = bf.readLine())!=null){
                readLineBuffer.append(line+"\n");
                resultList.add(line);
            }
            log.info("stop read in");
            Set<String> closePid = new HashSet<String>();
            List<String> afterSplitValueList = new ArrayList<String>();
            for(String result : resultList){
                if(!result.equals("") && result.contains("TCP")){
                    String[] s = result.trim().split(" ");
                    String stringTemp = "";
                    for(String temp : s){
                        if(!temp.equals("") && temp.length()!=0){
                            stringTemp = stringTemp + temp +",";
                        }
                    }
                    afterSplitValueList.add(stringTemp.substring(0,stringTemp.length()-1));
                }
            }
            StringBuffer sb = new StringBuffer();
            log.info("start validation......");
            for (String temp : afterSplitValueList){
                String split1 = temp.split(",")[1].split(":")[1];
                String s = temp.split(",")[4];
                if(split1.matches("^80[0-9]{2}$")){
                    closePid.add(s);
                    sb.append(split1).append(",");
                }
            }
            log.info("stop validation");
            if(closePid.size()>0) {
                Process process = null;
                String message = "已关闭端口: ";
                for (String pid : closePid) {
                    process = Runtime.getRuntime().exec("taskkill /pid " + pid + " /f");
                    process.waitFor();
                    log.info("closed port pid: " + pid);
                }
                message = message + sb.toString().substring(0, sb.length() - 1);
                String title = "<br />";
                log.info("closed port: " + message);
                TipWindow.showMessageInDesktop(title, message);
            } else {
                log.info("****** don't have should port pid ******");
            }
        }catch (Exception ex){
            log.error(ex.getMessage(),ex);
        }
    }

}
