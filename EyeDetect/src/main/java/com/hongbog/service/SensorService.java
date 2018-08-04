package com.hongbog.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hongbog.dto.SensorDto;

public interface SensorService {
	
	public void insertSensor(SensorDto sensorDto);

	public List<SensorDto> selectSensorList();

	public void deleteAllFromSensor();
	
	public void deleteFromSensorWhereId(String id);

	public SensorDto selectSensorFromId(String id);

	/*void insertTrainee(TraineeDTO map) throws Exception;

	TraineeDTO selectTrainee(Map<String, Object> map) throws Exception;

	ArrayList<TraineeDTO> selectTrainees(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> selectPrivacyInfo() throws Exception;

	ArrayList<Map<String, Object>> selectSurveyResponseByZipcode(Map map) throws Exception;

	void deleteTraineeByZipcodeNo(Map<String, Object> map);

	void deleteTraineeByNo(Map<String, Object> map);*/

}
