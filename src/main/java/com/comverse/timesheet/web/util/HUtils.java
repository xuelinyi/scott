/**
 * 
 */
package com.comverse.timesheet.web.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

/**
 * Hadoop 工具类
 * @author fansy
 * @date 2015-5-28
 */
public class HUtils {

	private static Configuration conf = null;
	private static FileSystem fs = null;
	
	public static boolean flag = false; // get configuration from db or file  ,true : db,false:file
	
	public static Configuration getConf(){
		
		if(conf ==null){
			conf = new Configuration ();
			if(flag){// get configuration from db
				conf.setBoolean("mapreduce.app-submission.cross-platform", true);// 配置使用跨平台提交任务  
				conf.set("fs.defaultFS", "hdfs://node101:8020");//指定namenode    
				conf.set("mapreduce.framework.name", "yarn");  // 指定使用yarn框架  
				conf.set("yarn.resourcemanager.address", "node101:8032"); // 指定resourcemanager  
				conf.set("yarn.resourcemanager.scheduler.address", "node101:8030");// 指定资源分配器
			}else{// get configuration from file
				//System.out.println(Utils.getKey("mapreduce.app-submission.cross-platform"));
				//System.out.println(Boolean.getBoolean(Utils.getKey("mapreduce.app-submission.cross-platform")));
				conf.setBoolean("mapreduce.app-submission.cross-platform", 
						"true".equals(Util.getKey("mapreduce.app-submission.cross-platform")));// 配置使用跨平台提交任务  
				conf.set("fs.defaultFS", Util.getKey("fs.defaultFS"));//指定namenode    
				conf.set("mapreduce.framework.name", Util.getKey("mapreduce.framework.name"));  // 指定使用yarn框架  
				conf.set("yarn.resourcemanager.address", Util.getKey("yarn.resourcemanager.address")); // 指定resourcemanager  
				conf.set("yarn.resourcemanager.scheduler.address", 
						Util.getKey("yarn.resourcemanager.scheduler.address"));// 指定资源分配器
			}
		}
		
		return conf;
	}
	
	public static FileSystem getFs(){
		if(fs==null){
			try {
				fs=FileSystem.get(getConf());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fs;
	}
	/**
	 * 获取hdfs文件目录及其子文件夹信息
	 * @param input
	 * @param recursive
	 * @return
	 * @throws IOException
	 */
	public static  String getHdfsFiles(String input,boolean recursive) throws IOException{
		RemoteIterator<LocatedFileStatus> files=getFs().listFiles(new Path(input), recursive);
		StringBuffer buff = new StringBuffer();
		while(files.hasNext()){
			buff.append(files.next().getPath().toString()).append("<br>");
		}
		
		return buff.toString();
	}
	public static void main(String[] args) throws IOException {
		System.out.println(getHdfsFiles("/", true));
	}
}
