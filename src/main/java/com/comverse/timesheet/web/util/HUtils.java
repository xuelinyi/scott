/**
 * 
 */
package com.comverse.timesheet.web.util;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;

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
	
	 public static void copyFile(Configuration conf ,  String local, String remote) throws IOException {
		 FileSystem fs = FileSystem.get(URI.create("hdfs://192.168.80.133:9000/"), conf);  
		 fs.copyFromLocalFile(new Path(local), new Path(remote));  
	        System.out.println("copy from: " + local + " to " + remote);  
	    }  
	  /** 
	     * 创建文件夹 
	     * @param conf 
	     * @param uri 
	     * @param remoteFile 
	     * @throws IOException 
	     */  
	    public static void markDir(Configuration conf , String uri , String remoteFile ) throws IOException{  
	        FileSystem fs = FileSystem.get(URI.create(uri), conf);  
	        Path path = new Path(remoteFile);  
	          
	        fs.mkdirs(path);  
	        System.out.println("创建文件夹"+remoteFile);  
	           
	    } 
	    /** 
	     * 查看文件 
	     * @param conf 
	     * @param uri 
	     * @param remoteFile 
	     * @throws IOException 
	     */  
	     public static void cat(Configuration conf , String uri ,String remoteFile) throws IOException {  
	            Path path = new Path(remoteFile);  
	            FileSystem fs = FileSystem.get(URI.create(uri), conf);  
	            FSDataInputStream fsdis = null;  
	            System.out.println("cat: " + remoteFile);  
	            try {  
	                fsdis = fs.open(path);  
	                IOUtils.copyBytes(fsdis, System.out, 4096, false);  
	            } finally {  
	                IOUtils.closeStream(fsdis);  
	                fs.close();  
	            }  
	    }  
	     /** 
	      * 下载 hdfs上的文件 
	      * @param conf 
	      * @param uri 
	      * @param remote 
	      * @param local 
	      * @throws IOException 
	      */  
	     public static void download(Configuration conf , String uri ,String remote, String local) throws IOException {  
	            Path path = new Path(remote);  
	            FileSystem fs = FileSystem.get(URI.create(uri), conf);  
	            fs.copyToLocalFile(false,path, new Path(local),true); 
	            System.out.println("download: from" + remote + " to " + local);  
	    }  
	     /** 
	      * 删除文件或者文件夹 
	      * @param conf 
	      * @param uri 
	      * @param filePath 
	      * @throws IOException 
	      */  
	     public static void delete(Configuration conf , String uri,String filePath) throws IOException {  
	            Path path = new Path(filePath);  
	            FileSystem fs = FileSystem.get(URI.create(uri), conf);  
	            fs.deleteOnExit(path);  
	            System.out.println("Delete: " + filePath);  
	    } 
	public static void main(String[] args) throws IOException {
		delete(getConf(), "hdfs://192.168.80.133:9000/", "hdfs://192.168.80.133:9000/tmp/tml_base.sql");
		download(getConf(),  "hdfs://192.168.80.133:9000/", "hdfs://192.168.80.133:9000/tmp/tml_base.sql", "D:/dsp/dsp code/RetailerInfoSepe/scott.sql");
		cat(getConf(), "hdfs://192.168.80.133:9000/", "hdfs://192.168.80.133:9000/tmp/tml_base.sql");
		markDir(getConf(), "hdfs://192.168.80.133:9000/", "hdfs://192.168.80.133:9000/tmp10/xuelinyi");
		copyFile(getConf(), "D:/dsp/dsp code/RetailerInfoSepe/tml_base.sql", "hdfs://192.168.80.133:9000/tmp");
		System.out.println("hdfs上的文件有："+getHdfsFiles("/", true));
	}
}
