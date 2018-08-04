package com.hongbog.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.hongbog.dto.SensorDto;

 
@Repository("sensorDao")
public class SensorDao extends AbstractDAO{

	public void insertSensor(SensorDto sensorDto){
		insert("sensor.insertSensor", sensorDto);
	}

	public List<SensorDto> selectSensorList() {
		return selectList("sensor.selectSensorList");
	}

	public void deleteAllFromSensor() {
		delete("sensor.deleteAllFromSensor");
	}
	
	public void deleteFromSensorWhereId(String id) {
		delete("sensor.deleteFromSensorWhereId", id);
	}

	public SensorDto selectSensorFromId(String id) {
		return (SensorDto)selectOne("sensor.selectSensorFromId", id);
	}
	
	/*@SuppressWarnings("unchecked")
	public List<Map<String, Object>> selectRegionList(Map<String, Object> map) {
		return (List<Map<String, Object>>)selectList("region.selectRegionList", map);
	}

	public void insertTrainee(TraineeDTO map) {
		insert("train.insertTrainee", map);
	}
	
	public void insertTraineeHasSurvey(Map<String, Object> map) {
		insert("train.insertTraineeHasSurvey", map);
	}

	public TraineeDTO selectTrainee(Map<String, Object> map) {
		return (TraineeDTO)selectOne("train.selectTrainee", map);
	}

	public Map<String, Object> selectRegionListTotalCount() {
		return (Map<String, Object>) selectOne("region.selectRegionListTotalCount");
	}

	public ArrayList<TraineeDTO> selectTrainees(Map<String, Object> map) {
		return (ArrayList<TraineeDTO>) selectList("train.selectTraineesByZipcode", map);
	}

	public List<Map<String, Object>> selectPrivacyInfo() {
		return (List<Map<String, Object>>) selectList("train.selectPrivacyInfo");
	}

	public ArrayList<Map<String, Object>> selectSurveyResponseByZipcode(Map map) {
		return (ArrayList<Map<String, Object>>) selectList("train.selectSurveyResponseByZipcode", map);
	}

	public void deleteTraineeByZipcodeNo(Map<String, Object> map) {
		delete("train.deleteTraineeByZipcodeNo", map);
	}

	public void deleteTraineeByNo(Map<String, Object> map) {
		delete("train.deleteTraineeByNo", map);
	}

	public void deleteSurveyResponseByTraineeNo(Map<String, Object> map) {
		delete("train.deleteSurveyResponseByTraineeNo", map);
	}

	public void deleteSurveyResponseByZipcodeNo(Map<String, Object> map) {
		delete("train.deleteSurveyResponseByZipcodeNo", map);
	}*/
 
}