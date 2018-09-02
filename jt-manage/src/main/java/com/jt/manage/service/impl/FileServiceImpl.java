package com.jt.manage.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Service
public class FileServiceImpl implements FileService{

	
	private String localPath="E:/jt-upload/";//表示本地磁盘路径
	private String urlPath="http://image.jt.com/";
	@Override
	public PicUploadResult uploadFile(MultipartFile uploadFile) {
		PicUploadResult result=new PicUploadResult();
		//1.获取图片名称
		String fileName=uploadFile.getOriginalFilename();
		fileName=fileName.toLowerCase();//将字符全部小写
		//2.使用正则表达式判断
		if(!fileName.matches("^.*(jpg|png|gif)$")){
			result.setError(1);//表示这个不是图片
		return result;
		}
		//3.判断是否是恶意程序
		try {
			BufferedImage bufferedImage=ImageIO.read(uploadFile.getInputStream());
			int width=bufferedImage.getWidth();
			int height=bufferedImage.getHeight();
			if(width==0||height==0){
				result.setError(1);
				return result;
			}
			//为了实现文件分文件存储 yyyy/mm/dd
			String dateDir=new SimpleDateFormat("yyyy/MM/dd").format(new Date()).toString();
			//创建文件
			String fileDir=localPath+dateDir;
			File dirFile=new File(fileDir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			/*
			 * 5.防止文件重名
			 * 5.1截取文件的后缀
			 * 5.2使用UUID当文件名+随机数
			 */
			String fileType=fileName.substring(fileName.lastIndexOf("."));
			String UUIDName=UUID.randomUUID().toString().replace("-", "");
			int randomNum=new Random().nextInt(1000);
			String realFileName=UUIDName+randomNum+fileType;
			
			String realLocalPath=fileDir +"/"+realFileName;
			
				uploadFile.transferTo(new File(realLocalPath));
				result.setHeight(height+"");
				result.setWidth(width+"");
				//实现图片回显 定义网络请求的路径
				String realUrlPath=urlPath+dateDir+"/"+realFileName;
				result.setUrl(realUrlPath);
		} catch (IOException e) {
			e.printStackTrace();
			//不是正经图片信息
			result.setError(1);
			return result;
		}
		
			
		
		return result;
	}

}
