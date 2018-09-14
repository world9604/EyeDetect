package com.hongbog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.hongbog.dto.SensorDto;

public interface SensorService {
	
	public void deleteAllFromSensor();
	
	public void deleteFromSensorWhereId(String id);

	public String getJsonArrayByLabel(SensorDto sensor);

	public List<SensorDto> searchSensorByAttr(SensorDto sensor);

	public List<SensorDto> getSensorViewData();
	
	public List<SensorDto> getSensorViewFromPage(Map pageMap);

	public List<SensorDto> selectUniqueSensorDataTypeAndLabel();

	public void insertSensor(MultipartFile file, SensorDto sensor);

	public String getJsonArray();

	public int getTotalCount();

}
