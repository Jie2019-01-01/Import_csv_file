package com.liming;

import com.csvreader.CsvReader;
import com.liming.csv.ImportRunnable;
import com.liming.log.Printer;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.*;

public class Main {

    private static Printer p = new Printer();

    public static void main(String[] args) {

        Connection conn = null;
        Statement statement = null;
        CsvReader reader = null;

        String path = "D:/liming/patent/2018/2018-01";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int fi=0;fi<files.length;fi++){
            String filePath = files[fi].getPath();

            String tmp = "";
            try{
                conn = DriverManager.getConnection("jdbc:mySql://localhost:3306/liming?serverTimezone=GMT%2B8", "root", "root");
                statement = conn.createStatement();
                reader = new CsvReader(filePath,',',Charset.forName("utf-8"));
                reader.readHeaders();
                //len表示的是有几个列
                int len = reader.getHeaders().length;
                //整个while就是为了组装成为 插入语句的形式
                System.out.println(files[fi].getName()+"---文件开始导入...........");
                while(reader.readRecord()){
                    tmp = "insert into pat_lens_2 values(";
                    for(int i=1;i<len-1;i++){
                        tmp += "'"+reader.get(i).replaceAll("'","\\\\'")+"',";
                    }
                    tmp += "'"+reader.get(len-1).replaceAll("'","\\\\'")+"');";
                    //tmp就是组装好的插入语句，即insert into talble(属性）values（内容）；
                    statement.execute(tmp); //执行插入
                }
                System.out.println(files[fi].getName()+"---文件导入完毕...........");
            }catch (Exception e){
                if (e instanceof SQLIntegrityConstraintViolationException){
                    System.out.println("异常文件:" + filePath + "\r\n" + "SQL语句:" + tmp + "\r\n" + "异常信息:" + e.getMessage() + "\r\n");
                }else {
                    p.print("异常文件:" + filePath + "\r\n" + "SQL语句:" + tmp + "\r\n" + "异常信息:" + e.getMessage() + "\r\n");
                }
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
                    eee.printStackTrace();
                }
            }
        }
    }
}
