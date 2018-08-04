package com.hongbog.service;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.hongbog.dao.SensorDao;
import com.hongbog.dto.SensorDto;

@Service("sensorService")
public class SensorServiceImpl implements SensorService{
    Logger log = Logger.getLogger(this.getClass());
     
    @Resource(name="sensorDao")
    private SensorDao sensorDao;
    
    @Override
	public void insertSensor(SensorDto sensorDto){
		sensorDao.insertSensor(sensorDto);
	}

	@Override
	public List<SensorDto> selectSensorList() {
		return sensorDao.selectSensorList();
	}

	@Override
	public void deleteAllFromSensor() {
		sensorDao.deleteAllFromSensor();
	}

	@Override
	public void deleteFromSensorWhereId(String id) {
		sensorDao.deleteFromSensorWhereId(id);
	}

	@Override
	public SensorDto selectSensorFromId(String id) {
		return sensorDao.selectSensorFromId(id);
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
	
    /*@Resource(name="regionDAO")
    private RegionDAO regionDAO;
    
    @Resource(name="trainDAO")
    private TrainDAO trainDAO;
     
	@Override
	public void insertTrainee(TraineeDTO map) throws Exception {
		trainDAO.insertTrainee(map);
	}

	@Override
	public TraineeDTO selectTrainee(Map<String, Object> map) throws Exception {
		TraineeDTO resultMap = trainDAO.selectTrainee(map);
		return resultMap;
	}

	@Override
	public ArrayList<TraineeDTO> selectTrainees(Map<String, Object> map) throws Exception {
		ArrayList<TraineeDTO> resultMap = trainDAO.selectTrainees(map);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> selectPrivacyInfo() {
		List<Map<String, Object>> resultMap = trainDAO.selectPrivacyInfo();
		return resultMap;
	}

	@Override
	public ArrayList<Map<String, Object>> selectSurveyResponseByZipcode(Map map) throws Exception {
		ArrayList<Map<String, Object>> resultMap = trainDAO.selectSurveyResponseByZipcode(map);
		return resultMap;
	}

	@Override
	public void deleteTraineeByZipcodeNo(Map<String, Object> map) {
		trainDAO.deleteSurveyResponseByZipcodeNo(map);
		trainDAO.deleteTraineeByZipcodeNo(map);
	}

	@Override
	public void deleteTraineeByNo(Map<String, Object> map) {
		trainDAO.deleteSurveyResponseByTraineeNo(map);
		trainDAO.deleteTraineeByNo(map);
	}*/
 
}