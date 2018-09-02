package com.jt.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manage.service.FileService;

@Controller
public class FileController {
	@Autowired
	private FileService fileService;
    /*
     * 如果上传文件完成后，再次定向到文件上传页面
     * 注意事项：
     * 1.文件名必须和页面保持一致
     * 2.重定向写法
     *   1.使用response对象
     *   2.利用redirect重定向
     * 
     * 
     * 文件上传步骤
     * 1.获取文件名称
     * 2.定义文件上传的路径
     * 3.调用mvc的工具类输出文件
     */
	@RequestMapping("/file")
	public String file(MultipartFile fileName) throws Exception{
		//1.获取文件名称
		String name=fileName.getOriginalFilename();
		//2.定义上传路径
		String path="E:/jt-upload";
		//3.判断文件是否存在 如果不存在则新建文件夹
		File file=new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		fileName.transferTo(new File(path+"/"+name));
			
		return "redirect:/file.jsp";
	}
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public PicUploadResult uploadFile(MultipartFile uploadFile){
		return fileService.uploadFile(uploadFile);
		
	}
}
