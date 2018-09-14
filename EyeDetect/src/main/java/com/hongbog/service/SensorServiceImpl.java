package com.hongbog.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hongbog.dao.SensorDao;
import com.hongbog.dto.SensorDto;

@Service("sensorService")
public class SensorServiceImpl implements SensorService{
	
    private Logger log = Logger.getLogger(this.getClass());
     
    private interface Constant{
		
		String FOLDER_NAME = "hongbog";
		
		String EXT_NAME = "PNG";
		
	}
    
    @Resource(name="sensorDao")
    private SensorDao sensorDao;

	@Override
	public void deleteAllFromSensor() {
		
		sensorDao.deleteAllFromSensor();
		
	}

	@Override
	public void deleteFromSensorWhereId(String id) {
		
		sensorDao.deleteFromSensorWhereId(id);
		
	}

	private byte[] fileToByteArray(File file){
		
		BufferedImage bufferedImage;
		
		byte[] imageBytes = null;
		
		try {
			
			bufferedImage = ImageIO.read(file);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ImageIO.write(bufferedImage, "png", baos);
			
			imageBytes = baos.toByteArray();
		
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
		
		return imageBytes;
	}
	
	@Override
	public String getJsonArrayByLabel(SensorDto sensor) {

		Gson gson = new Gson();
	    		
	    List<SensorDto> sensors = sensorDao.selectSensorsByDataTypeAndLabel(sensor);
	    
	    sensors = convertForUse(sensors);
	    
	    String sensorsJson = gson.toJson(sensors);
		
		return sensorsJson;
	}
	
	private List<SensorDto> convertForUse(List<SensorDto> sensors){
		
		sensors = convertImageByteArrayFromSensors(sensors);
	    
	    sensors = convertExtraDataToJson(sensors);
	    
	    return sensors;
		
	}
		
	private List<SensorDto> convertImageByteArrayFromSensors(List<SensorDto> sensors) {
		
		for(SensorDto sensorTmp : sensors) {
			
			String imageSavePath = sensorTmp.getImageSavePath();

			File file = new File(imageSavePath);
			
			byte[] imageBytes = fileToByteArray(file);
			
			if(imageBytes == null) continue;
			
			sensorTmp.setImageByteArray(imageBytes);
			
			sensorTmp.setImageSavePath(null);
			
		}
		
		return sensors;	
	}
	
	private List<SensorDto> convertExtraDataToJson(List<SensorDto> sensors) {
		
		for(SensorDto sensorTmp : sensors) {
			
			String extraData = sensorTmp.getExtraData();
			
			if(extraData == null) continue;
	
			JsonParser jsonParser = new JsonParser();

			JsonObject jo = (JsonObject)jsonParser.parse(extraData);
			
			sensorTmp.setExtraDataJsonObject(jo);
			
			sensorTmp.setExtraData(null);
			
		}
		
		return sensors;	
	}

	@Override
	public List<SensorDto> searchSensorByAttr(SensorDto sensor) {

		List<SensorDto> sensors = sensorDao.selectSensorsByDataTypeAndLabel(sensor);
		
		List<SensorDto> convertSensors = convertEncodedImage(sensors);
		
		return convertSensors;
	}

	@Override
	public List<SensorDto> getSensorViewData() {

		List<SensorDto> sensors = convertEncodedImage(sensorDao.selectSensorList());
		
		return sensors;
	}
	
	@Override
	public List<SensorDto> getSensorViewFromPage(Map pageMap) {
		
		List<SensorDto> sensors = convertEncodedImage(sensorDao.selectSensorListPaging(pageMap));
		
		return sensors;
	}
	
	private List<SensorDto> convertEncodedImage(List<SensorDto> sensors){
		
		for(SensorDto sensor : sensors) {
			
			String imageSavePath = sensor.getImageSavePath();

			if(imageSavePath == null) continue;
			
			File file = new File(imageSavePath);
			
			byte[] imageBytes = fileToByteArray(file);
			
			if(imageBytes == null) continue;
			
			String encodedImage = encodeToString(imageBytes);
			
			sensor.setEncodedImage(encodedImage);
			
		}
		
		return sensors;
	}
	
	private String encodeToString(byte[] bytes) {
		
		return BaseEncoding.base64().encode(bytes);
	}

	@Override
	public List<SensorDto> selectUniqueSensorDataTypeAndLabel() {

		List<String> dataTypes = sensorDao.selectUniqueSensorDataType();
		
		List<String> labels = sensorDao.selectUniqueSensorLabel();
		
		List<SensorDto> sensors = new ArrayList<>();
		
		Iterator<String> dataTypeItr = dataTypes.iterator();
		
		Iterator<String> labelItr = labels.iterator();

		while(dataTypeItr.hasNext() || labelItr.hasNext()) {
			
			SensorDto sensor = new SensorDto();
			
			if(dataTypeItr.hasNext()) sensor.setDataType(dataTypeItr.next());
			
			if(labelItr.hasNext()) sensor.setLabel(labelItr.next());

			sensors.add(sensor);
			
		}
			
		return sensors;
	}
	
	private String removeExtName(String includedExtName) {
		
		int idx = includedExtName.lastIndexOf(".");

		includedExtName = includedExtName.substring(0, idx);

		return includedExtName;
	}

	private String makePath(String fileName, String dirName, String extName) {
		
		String path = "C:" + File.separator + dirName;
		
		File file = new File(path);
		
		if(file.mkdirs()) log.debug(file.getAbsolutePath());
		
		path += (File.separator + fileName + "." + extName);
		
		return path;
	}
	
	@Override
	public void insertSensor(MultipartFile file, SensorDto sensor) {

		if(file == null) return;
		
		String folderName = Constant.FOLDER_NAME + 
				File.separator + sensor.getDataType() + File.separator + sensor.getLabel();
		
		String fileOriName = removeExtName(file.getOriginalFilename());
		
		String imageSavePath = makePath(fileOriName, folderName, Constant.EXT_NAME);
		
		sensor.setImageSavePath(imageSavePath);
		
		if(sensor != null) sensorDao.insertSensor(sensor);
		
		try {
			
			file.transferTo(new File(imageSavePath));
			
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
			
	}

	@Override
	public String getJsonArray() {
		
		Gson gson = new Gson();
		
	    List<SensorDto> sensors = sensorDao.selectSensorList();
	    
	    sensors = convertForUse(sensors);
	    
	    String sensorsJson = gson.toJson(sensors);
		
		return sensorsJson;
	}

	@Override
	public int getTotalCount() {

		return sensorDao.selectTotalCount();
	}
	
	/*public void byteToZipFile() {
		
		ArrayList<SensorDto> sensors = (ArrayList<SensorDto>)sensorDao.selectSensorList();
		
		
		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream());
		ZipEntry entry = null;
		
		while ((entry = zipStream.getNextEntry()) != null) {

		    String entryName = entry.getName();

		    FileOutputStream out = new FileOutputStream(entryName);

		    byte[] byteBuff = new byte[4096];
		    int bytesRead = 0;
		    while ((bytesRead = zipStream.read(byteBuff)) != -1)
		    {
		        out.write(byteBuff, 0, bytesRead);
		    }

		    out.close();
		    zipStream.closeEntry();
		}
		zipStream.close(); 
	}*/
	
	/*public void byteToZipFile2() {
		
		//String zipName = "C:/Users/taein/Desktop/test.zip"; 
		
		int bufferSize = 1024 * 2;
		String ouputName = "test";
		            
		ZipOutputStream zos = null;
		            
		try {
		                
		    if (request.getHeader("User-Agent").indexOf("MSIE 5.5") > -1) {
		        response.setHeader("Content-Disposition", "filename=" + ouputName + ".zip" + ";");
		    } else {
		        response.setHeader("Content-Disposition", "attachment; filename=" + ouputName + ".zip" + ";");
		    }
		    response.setHeader("Content-Transfer-Encoding", "binary");
		    
		                
		    OutputStream os = response.getOutputStream();
		    zos = new ZipOutputStream(os); // ZipOutputStream
		    zos.setLevel(8); // 압축 레벨 - 최대 압축률은 9, 디폴트 8
		    BufferedInputStream bis = null;
		                
		    
		    String[] filePaths = {"filePath1","filePath2","filePath3"};
		    String[] fileNames = {"fileName1","fileName2","fileName3"};
		    int    i = 0;
		    for(String filePath : filePaths){
		        File sourceFile = new File(filePath);
		                        
		        bis = new BufferedInputStream(new FileInputStream(sourceFile));
		        ZipEntry zentry = new ZipEntry(fileNames[i]);
		        zentry.setTime(sourceFile.lastModified());
		        zos.putNextEntry(zentry);
		        
		        byte[] buffer = new byte[bufferSize];
		        int cnt = 0;
		        while ((cnt = bis.read(buffer, 0, bufferSize)) != -1) {
		            zos.write(buffer, 0, cnt);
		        }
		        zos.closeEntry();
		 
		        i++;
		    }
		               
		    zos.close();
		    bis.close();
		                
		                
		} catch(Exception e){
		    e.printStackTrace();
		}
	}*/
	
 
}