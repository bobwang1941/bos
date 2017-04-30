package cn.itcast.bos.attachment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import sun.misc.BASE64Encoder;

public class MyFileUtils {
	/**
	 * 下载文件时，针对不同浏览器，进行附件名的编码
	 * 
	 * @param filename
	 *            下载文件名
	 * @param agent
	 *            客户端浏览器
	 * @return 编码后的下载附件名
	 * @throws IOException
	 */
	@SuppressWarnings("all")
	public static String encodeDownloadFilename(String filename, String agent) {
		try {
			if (agent.contains("Firefox")) { // 火狐浏览器
				filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
				filename = filename.replace("_", ":");
			} else { // IE及其他浏览器
				filename = URLEncoder.encode(filename, "utf-8");
				filename = filename.replace("%3A", "-").replace("+", " ");
			}
			return filename;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
