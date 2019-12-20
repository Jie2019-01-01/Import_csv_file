package com.liming.csv;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
//        String path = "/opt/liming/patent/patent-2014/";
        String path = "D:/liming/patent/2016/";
        File file = new File(path);
        find_childNode(file);
    }

    public static void find_childNode(File file){
        List<File> fileList = new ArrayList<File>();
        // 当前目录下的子项目
        File[] child_projects = file.listFiles();
        for (int i=0; i<child_projects.length; i++){
            // 判断目录还是文件
            if(child_projects[i].isDirectory()){
                // 继续找
                find_childNode(child_projects[i]);
            }else if(child_projects[i].isFile()){
                fileList.add(child_projects[i]);
            }
        }
        // 单个目录下的文件下载
        //
        if (fileList.size()>0){
            start_run(fileList);
        }
    }

    public static void start_run(List<File> files){
        ExecutorService e = Executors.newFixedThreadPool(1);
        for (File file: files){
            ImportRunnable ir = new ImportRunnable(file.getPath());
            e.submit(ir);
        }
        e.shutdown();
    }
}
