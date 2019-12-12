package com.liming.log;

import com.liming.csv.Import;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * 打印日志类
 * @author liuweijie
 *
 */
public class Printer {

	private static final Logger log = LogManager.getLogger(Import.class);

	public void print(String message) {
		log.info(message);
	}
}
