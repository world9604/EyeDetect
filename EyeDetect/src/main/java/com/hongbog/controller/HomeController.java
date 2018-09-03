package com.hongbog.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringInputStream;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hongbog.dto.SensorDto;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.hongbog.service.SensorService;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private static final String FOLDER_NAME = "eyeDetect";
	private static final String EXT_NAME = "PNG";
	
	@Resource(name="sensorService")
	private SensorService sensorService;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	/*@RequestMapping(value = "/uploadImage.do", method = RequestMethod.POST)
	public String uploadImage(@RequestParam("photo") MultipartFile file, 
									 Model model) {
		
		try {
			log.debug("photo byte array : " + Arrays.toString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return "home";
	}*/

	/*@RequestMapping(value = "/uploadImage.do", method = RequestMethod.POST)
	public String uploadImage(@ModelAttribute MultipartFile file, 
									 Model model) {
		
		try {
			log.debug("photo byte array : " + Arrays.toString(file.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return "home";
	}*/
	
	
	@RequestMapping(value = "/uploadImage.do", method = RequestMethod.POST)
	public String uploadImage(@RequestParam("photo") MultipartFile file, 
									@RequestParam("label") String label,
									@RequestParam("roll") String roll,
									@RequestParam("pitch") String pitch,
									@RequestParam("yaw") String yaw,
									@RequestParam("br") String br, Model model) {
		
		byte[] byteArray = null;
		
		try {
			byteArray = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(byteArray != null) {
			log.debug("photo byte array length: " + byteArray.length);
//			log.debug("photo byte array : " + Arrays.toString(byteArray));
			log.debug("label : " + label);
			log.debug("roll : " + roll);
			log.debug("pitch : " + pitch);
			log.debug("yaw : " + yaw);
			log.debug("br : " + br);
			
			SensorDto sensorDto = new SensorDto(byteArray, label, roll, pitch, yaw, br);
			
			if(sensorDto != null) {
				sensorService.insertSensor(sensorDto);
			}
		}
		
	    return "success";
	}
	
	
	@RequestMapping(value = "/uploadData.do", method = RequestMethod.POST)
	public String uploadData(@RequestParam("photo") MultipartFile file, 
			@RequestParam("label") String label,
			@RequestParam("roll") String roll,
			@RequestParam("pitch") String pitch,
			@RequestParam("yaw") String yaw,
			@RequestParam("br") String br, Model model) {
				
		if(file != null) {
			
			log.debug("label : " + label);
			log.debug("roll : " + roll);
			log.debug("pitch : " + pitch);
			log.debug("yaw : " + yaw);
			log.debug("br : " + br);
			
			String imageSavePath = makePath(file.getName(), FOLDER_NAME, EXT_NAME);
			log.debug("imageSavePath : " + imageSavePath);
			
			SensorDto sensorDto = new SensorDto(imageSavePath, label, roll, pitch, yaw, br);
			
			if(sensorDto != null) sensorService.insertSensor(sensorDto);
			
			try {
				file.transferTo(new File(imageSavePath));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	    return "success";	 
	}
	
	
	private String makePath(String fileName, String dirName, String extName) {
		
		String path = "C:" + File.separator + dirName;
		File file = new File(path);
		file.mkdir();
		
		path += (File.separator + fileName + "." + extName);
		return path;
	}
	
	
	/*@RequestMapping(value = "/writeImage.do", method = RequestMethod.GET)
	public ModelAndView writeImage(Model model) {
		
		log.debug("mSensorDto : " + mSensorDto.getImageByteArray());
		
		Base64.Encoder encode = Base64.getEncoder();
		String encodedImage = encode.encodeToString(mSensorDto.getImageByteArray());
		
		Map<String, String> map = new HashMap<>();
		map.put("encodedImage", encodedImage);
		
		ModelAndView mv = new ModelAndView("home", map);
		
	    return mv;
	}*/
	
	
	@RequestMapping(value = "/showData.do", method = RequestMethod.GET)
	public ModelAndView showData(Model model) {
		
		ArrayList<SensorDto> sensors = (ArrayList<SensorDto>)sensorService.selectSensorList();
		
		Base64.Encoder encode = Base64.getEncoder();
		
		ArrayList<SensorDto> outputSensors = new ArrayList<>();
		
		for(SensorDto sensor : sensors) {
			byte[] imageByteArray = sensor.getImageByteArray();
			String encodedImage = encode.encodeToString(imageByteArray);
			sensor.setEncodedImage(encodedImage);
			outputSensors.add(sensor);
		}
		
		ModelAndView mv = new ModelAndView("home");
		mv.addObject("sensors", outputSensors);
		
		return mv;
	}
	
	@RequestMapping(value = "/deleteAllData.do", method = RequestMethod.POST)
	public ModelAndView deleteAllData(Model model) {
		
		sensorService.deleteAllFromSensor();
		
		ModelAndView mv = new ModelAndView("redirect:/showData.do");
		
		return mv;
	}
	
	@RequestMapping(value = "/deleteDataFromId.do", method = RequestMethod.POST)
	public ModelAndView deleteDataFromId(@RequestParam("id") String id, Model model) {
		
		sensorService.deleteFromSensorWhereId(id);
		
		ModelAndView mv = new ModelAndView("redirect:/showData.do");
		
		return mv;
	}
	
	@RequestMapping(value="/downloadImageFile.do", method = RequestMethod.POST)
	public void downloadImageFile(@RequestParam("id") String id, HttpServletResponse response) throws Exception{
	    
		log.debug("id : " + id);
		
		SensorDto sensor = sensorService.selectSensorFromId(id);
		
		if(sensor != null) {
			byte[] imageByteArray = sensor.getImageByteArray();
			
			response.setContentType("image/png");
			response.setContentLength(imageByteArray.length);
		    response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(sensor.getLabel() + ".PNG", "UTF-8") + "\";");
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    response.getOutputStream().write(imageByteArray);
		    
		    response.getOutputStream().flush();
		    response.getOutputStream().close();
		}
	}
	
	@RequestMapping(value="/downloadImageZipFile.do", method = RequestMethod.POST)
	public void downloadImageZipFile(HttpServletResponse response, HttpServletRequest request) {
		
		String ouputName = "eyeImage";
		            
		ZipOutputStream zos = null;
		            
		try {
		    if (request.getHeader("User-Agent").indexOf("MSIE 5.5") > -1) {
		        response.setHeader("Content-Disposition", "filename=" + ouputName + ".zip" + ";");
		    } else {
		        response.setHeader("Content-Disposition", "attachment; filename=" + ouputName + ".zip" + ";");
		    }
		    response.setHeader("Content-Transfer-Encoding", "binary");

		    OutputStream os = response.getOutputStream();
		    
		    zos = new ZipOutputStream(os);
		    //zos.setLevel(8); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
	    	
		    BufferedInputStream bis = null;
		    
		    ArrayList<SensorDto> sensors = (ArrayList<SensorDto>)sensorService.selectSensorList();
		    
		    for(SensorDto sensor : sensors){

		    	byte[] imageByteArray = sensor.getImageByteArray();
		    	String entryName = URLEncoder.encode(sensor.getId() + "_" + sensor.getLabel() + ".PNG", "UTF-8");
		    	
		        bis = new BufferedInputStream(new ByteArrayInputStream(imageByteArray));
		        ZipEntry zentry = new ZipEntry(entryName);
		        zos.putNextEntry(zentry);
		        
		        int bufferSize = imageByteArray.length;
		        
		        byte[] buffer = new byte[bufferSize];
		        
		        int cnt = 0;
		        
		        while ((cnt = bis.read(buffer, 0, bufferSize)) != -1) {
		            zos.write(buffer, 0, cnt);
		        }
		        
		        zos.closeEntry();
		    }
		               
		    zos.close();
		    bis.close();
		                
		} catch(Exception e){
		    e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/downloadAllZipJsonTxt.do", method = RequestMethod.POST)
	public void downloadAllZipJsonTxt(HttpServletResponse response, HttpServletRequest request) {
		
		String ouputName = "sensorValues";
		            
		ZipOutputStream zos = null;
		            
		try {
		                
		    if (request.getHeader("User-Agent").indexOf("MSIE 5.5") > -1) {
		        response.setHeader("Content-Disposition", "filename=" + ouputName + ".zip" + ";");
		    } else {
		        response.setHeader("Content-Disposition", "attachment; filename=" + ouputName + ".zip" + ";");
		    }
		    response.setHeader("Content-Transfer-Encoding", "binary");

		    OutputStream os = response.getOutputStream();
		    
		    zos = new ZipOutputStream(os);
		    //zos.setLevel(8); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
	    	
		    BufferedInputStream bis = null;
		    
		    ArrayList<SensorDto> sensors = (ArrayList<SensorDto>)sensorService.selectSensorList();
		    
		    Gson gson = new Gson();
		    
		    for(SensorDto sensor : sensors){

		    	String sensorJson = gson.toJson(sensor);
		    	byte[] sensorJsonByteArray = sensorJson.getBytes("UTF8");
		    	String entryName = URLEncoder.encode(sensor.getId() + "_" + sensor.getLabel() + ".json", "UTF-8");
		    	
		        bis = new BufferedInputStream(new ByteArrayInputStream(sensorJsonByteArray));
		        ZipEntry zentry = new ZipEntry(entryName);
		        zos.putNextEntry(zentry);
		        
		        int bufferSize = sensorJsonByteArray.length;
		        
		        byte[] buffer = new byte[bufferSize];
		        
		        int cnt = 0;
		        
		        while ((cnt = bis.read(buffer, 0, bufferSize)) != -1) {
		            zos.write(buffer, 0, cnt);
		        }
		        
		        zos.closeEntry();
		    }
		               
		    zos.close();
		    bis.close();
		                
		} catch(Exception e){
		    e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/downloadAllJsonTree.do", method = RequestMethod.POST)
	public void downloadAllJsonTree(HttpServletResponse response, HttpServletRequest request) {
		
		String ouputName = "sensorValues";
		            
		try {
		    if (request.getHeader("User-Agent").indexOf("MSIE 5.5") > -1) {
		        response.setHeader("Content-Disposition", "filename=" + ouputName + ".txt" + ";");
		    } else {
		        response.setHeader("Content-Disposition", "attachment; filename=" + ouputName + ".txt" + ";");
		    }
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    
		    Gson gson = new Gson();
		    
		    ArrayList<SensorDto> sensors = (ArrayList<SensorDto>)sensorService.selectSensorList();
		    
		    String sensorsJson = gson.toJson(sensors);
		    
		    PrintWriter out = response.getWriter();

		    out.write(sensorsJson);
		    
		} catch(Exception e) {
		    e.printStackTrace();
		}
	}
	
	
}