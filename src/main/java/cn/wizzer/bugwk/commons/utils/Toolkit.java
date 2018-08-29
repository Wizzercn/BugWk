package cn.wizzer.bugwk.commons.utils;

import java.io.*;
import java.util.Date;

/**
 * Created by wizzer on 2018.08
 */
public class Toolkit {
    public static String updateAt(long t) {
        long now = System.currentTimeMillis() / 1000;
        long diff = now - t;
        if (diff < 5) {
            return "刚刚";
        }
        if (diff < 60) {
            return diff + "秒前";
        }
        if (diff < 60 * 60) {
            return (diff / 60) + "分钟前";
        }
        if (diff < 24 * 60 * 60) {
            return (diff / 60 / 60) + "小时前";
        }
        return (diff / 24 / 60 / 60) + "天前";
    }

    public static String filteContent(String cnt) {
        if (cnt != null)
            try {
                Reader r = new StringReader(cnt);
                BufferedReader br = new BufferedReader(r);
                StringWriter sw = new StringWriter();
                boolean isCode = false;
                String prevLine = "";
                while (br.ready()) {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    if ("``".equals(line.trim()) && !isCode) {
                        line = "```";
                    }
                    if ("```".equals(line.trim())) {
                        if (isCode) {
                            isCode = false;
                        } else {
                            isCode = true;
                            if (prevLine != null && !prevLine.trim().isEmpty()) {
                                sw.append("\r\n");
                            }
                        }
                    }
                    sw.append(line);
                    sw.append("\r\n");
                    prevLine = line;
                }
                return sw.toString();
            } catch (IOException e) {
                e.printStackTrace();// 不可能吧
            }
        return cnt;
    }
}
