package com.liming.csv;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version 1.0
 * @Author: L.W.J
 * @Date: 2019/7/30
 * @Description 向数据库导入csv文件
 */
public class Import {

    public static void main(String[] args) {

        ExecutorService e = Executors.newFixedThreadPool(30);

//        String path = "D:/liming/patent/2016/2016-12";
        String path = "/opt/liming/patent-2014/patent-2014-12";
        File file = new File(path);
        File[] files = file.listFiles();
        for (int i=0;i<files.length;i++){
            ImportRunnable ir = new ImportRunnable(path+"/"+files[i].getName());
            e.submit(ir);
        }

        e.shutdown();
    }
}
