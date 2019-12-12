package com.liming.csv;

import com.csvreader.CsvReader;
import com.liming.log.Printer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.*;

/**
 * @version 1.0
 * @Author: L.W.J
 * @Date: 2019/7/30
 * @Description 导入csv文件的线程类
 */
public class ImportRunnable implements Runnable {

    private static Printer p = new Printer();

    private String path;
    public ImportRunnable(String path) {
        this.path = path;
    }

    @Override
    public void run() {

        Connection conn = null;
        Statement statement = null;
        CsvReader reader = null;

        String tmp = "";
        try{
            conn = DriverManager.getConnection("jdbc:mySql://localhost:13306/liming?serverTimezone=GMT%2B8", "root", "root");
            statement = conn.createStatement();
            reader = new CsvReader(path,',',Charset.forName("utf-8"));
            reader.readHeaders();
            //len表示的是有几个列
            int len = reader.getHeaders().length;
            //整个while就是为了组装成为 插入语句的形式
            System.out.println(path.substring(path.lastIndexOf("/"))+"---文件开始导入...........");
            while(reader.readRecord()){
                try{
                    tmp = "insert into pat_lens_1 values(";
                    for(int i=1;i<len-1;i++){
                        tmp += "'"+reader.get(i).replaceAll("'","\\\\'")+"',";
                    }
                    tmp += "'"+reader.get(len-1).replaceAll("'","\\\\'")+"');";
                    //tmp就是组装好的插入语句，即insert into talble(属性）values（内容）；
                    statement.execute(tmp); //执行插入
                }catch (Exception e){
                    if (e instanceof SQLIntegrityConstraintViolationException == false){
                        p.print("异常文件:" + path + "\r\n" + "SQL语句:" + tmp + "\r\n" + "异常信息:" + getTrace(e) + "\r\n");
                    }
                }
            }
            System.out.println(path.substring(path.lastIndexOf("/"))+"---文件导入完毕...........");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (reader!=null){
                    reader.close();
                }
                if (statement!=null){
                    statement.close();
                }
                if (conn!=null){
                    conn.close();
                }
            }catch (SQLException eee){
                p.print(getTrace(eee));
                eee.printStackTrace();
            }
        }
    }

    /**
     * 异常信息转换成字符串
     */
    public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
}
