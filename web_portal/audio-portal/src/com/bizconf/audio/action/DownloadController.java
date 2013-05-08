package com.bizconf.audio.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.utils.LiberContainer;

/**
 * 下载中心
 * @author wangyong
 *
 */
@ReqPath("downCenter")
public class DownloadController {
	
	private static final String dirPath = LiberContainer.getContainer().getServletContext().getRealPath("download")+File.separator;

	/**
	 * 下载中心下载会议客户端
	 * wangyong
	 * 2013-4-26
	 */
	@AsController(path = "downConfClient")
	public void downConfClient(HttpServletRequest request,HttpServletResponse response){
//		String dirPath = LiberContainer.getContainer().getServletContext().getRealPath("download")+File.separator;
		downLoad("mcsetup.exe", dirPath, request, response);
	}
	
	/**
	 * 下载中心下载视频转换软件
	 * wangyong
	 * 2013-4-26
	 */
	@AsController(path = "downVideoTrans")
	public void downVideoTrans(HttpServletRequest request,HttpServletResponse response){
//		String dirPath = LiberContainer.getContainer().getServletContext().getRealPath("download")+File.separator;
		downLoad("VideoTranslate.rar", dirPath, request, response);
	}
	
	private void downLoad(String fileName, String dirPath, HttpServletRequest request,HttpServletResponse response){
		String tempPath = dirPath + fileName;
		File file = new File(tempPath);
		response.setContentType("octets/stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        BufferedInputStream in = null;
        BufferedOutputStream out = null; 
        try {
        	in = new BufferedInputStream(new FileInputStream(file));
        	out = new BufferedOutputStream(response.getOutputStream());
        	byte[] bts = new byte[1024*20];
        	int temp = 0;
        	while((temp = in.read(bts))!=-1){
        		out.write(bts,0,temp);
        	}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.flush();
				out.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
				in.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
