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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
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

import com.hongbog.dto.SensorDto;
import com.hongbog.service.SensorService;
import com.hongbog.util.CommandMap;


/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private interface Constant{
		
		String SENSORS = "sensors";
		
		String LIST_DATA_PAGE = "listData";
		
		String INFORM_PAGE = "inform";
		
	}
	
	@Resource(name="sensorService")
	private SensorService sensorService;
		
	@RequestMapping(value = "/uploadData.do", method = RequestMethod.POST)
	public String uploadData(@RequestParam("photo") MultipartFile file, 
				SensorDto sensor) {
		
		sensorService.insertSensor(file, sensor);
		
	    return null;	 
	}
	
	/*@RequestMapping(value = "/showData.do", method = RequestMethod.GET)
	public ModelAndView showData() {
		
		List<SensorDto> sensors = sensorService.getSensorViewData();
		
		int totalCount = sensorService.getTotalCount();
		
		ModelAndView mv = new ModelAndView(Constant.LIST_DATA_PAGE);
		
		mv.addObject(Constant.SENSORS, sensors);
		
		mv.addObject("totalCount", totalCount);
		
		return mv;
	}*/
	
	@RequestMapping(value = "/showData.do")
	public ModelAndView paging(CommandMap pageMap) {
		
		if(pageMap.getMap().size() == 0) {
			
			Map map = new HashMap<>();
			
			map.put("selectedPage", 1);
			
			map.put("dataPerPage", 7);
			
			map.put("startData", 0);
			
			pageMap.putAll(map);
		
		}
		
		List<SensorDto> sensors = sensorService.getSensorViewFromPage(pageMap.getMap());
		
		int totalCount = sensorService.getTotalCount();
		
		ModelAndView mv = new ModelAndView(Constant.LIST_DATA_PAGE);
		
		mv.addObject(Constant.SENSORS, sensors);
		
		mv.addObject("currentPage", pageMap.getMap().get("selectedPage"));
		
		mv.addObject("totalCount", totalCount);
		
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
		    
		    String sensorsJson = sensorService.getJsonArray();
		    
		    PrintWriter out = response.getWriter();

		    out.write(sensorsJson);
		    
		    out.close();
		    
		} catch(Exception e) {
			
		    e.printStackTrace();
		
		}
	}
	
	@RequestMapping(value = "/getJson.do", method = RequestMethod.GET)
	public void getJson(HttpServletResponse response
							, HttpServletRequest request 
							, SensorDto sensor) {
		
		String sensorJsonArray = sensorService.getJsonArrayByLabel(sensor);
		    
		PrintWriter out;
		
		try {
		
			out = response.getWriter();
		
		    out.write(sensorJsonArray);
		    
		    out.close();
		    
		} catch (IOException e) {

			e.printStackTrace();
		
		}
	}
	
	@RequestMapping(value = "/searchData.do", method = RequestMethod.POST)
	public ModelAndView searchData(HttpServletResponse response
							, HttpServletRequest request, SensorDto sensor) {
		
		List<SensorDto> sensors = sensorService.searchSensorByAttr(sensor);
		    
		ModelAndView mv = new ModelAndView(Constant.LIST_DATA_PAGE);
		
		mv.addObject(Constant.SENSORS, sensors);
	
		return mv;
	}
	
	@RequestMapping(value = "/goToInform.do", method = RequestMethod.POST)
	public ModelAndView goToInform(HttpServletResponse response
							, HttpServletRequest request) {
			
		List<SensorDto> sensors = sensorService.selectUniqueSensorDataTypeAndLabel();
		
		ModelAndView mv = new ModelAndView(Constant.INFORM_PAGE);
		
		mv.addObject(Constant.SENSORS, sensors);
		
		return mv;
	}
	
	@RequestMapping(value = "/fn_goToListData.do", method = RequestMethod.POST)
	public ModelAndView fn_goToListData(HttpServletResponse response
							, HttpServletRequest request) {
		
		ModelAndView mv = new ModelAndView("redirect:/showData.do");
		
		return mv;
	}
	
	/*@RequestMapping(value="/downloadImageZipFile.do", method = RequestMethod.POST)
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
		    	
		    	String entryName = URLEncoder.encode(sensor.getIdx() + "_" + sensor.getLabel() + ".json", "UTF-8");
		    	
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
	}*/
}