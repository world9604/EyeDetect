package com.hongbog.dto;

import java.util.Arrays;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

public class SensorDto {
	
	private String idx;
	private byte[] imageByteArray;
	private String encodedImage;
	private String label;
	private String roll;
	private String pitch;
	private String yaw;
	private String br;
	private String registerDate;
	private String imageSavePath;
	private String dataType;
	private String extraData;
	private JsonObject extraDataJsonObject;
	
	public SensorDto() {}
	
	public SensorDto(String imageSavePath, String label, String roll, 
			String pitch, String yaw, String br, String dataType) {
		super();
		this.imageSavePath = imageSavePath;
		this.label = label;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
		this.br = br;
		this.dataType = dataType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		
		System.out.println("dataType.hashCode() : " + dataType.hashCode());
		System.out.println("label.hashCode() : " + label.hashCode());
		System.out.println("result : " + result);
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
		
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		SensorDto other = (SensorDto) obj;
		
		if (dataType == null) {
			
			if (other.dataType != null) return false;
		
		} else if (!dataType.equals(other.dataType))
			
			return false;
		
		if (label == null) {
			
			if (other.label != null) return false;
			
		} else if (!label.equals(other.label)) return false;
		
		return true;

	}

	public byte[] getImageByteArray() {
		return imageByteArray;
	}

	public void setImageByteArray(byte[] imageByteArray) {
		this.imageByteArray = imageByteArray;
	}
	
	public String getImageSavePath() {
		return imageSavePath;
	}

	public void setImageSavePath(String imageSavePath) {
		this.imageSavePath = imageSavePath;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getIdx() {
		return idx;
	}

	public void setId(String idx) {
		this.idx = idx;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getEncodedImage() {
		return encodedImage;
	}
	
	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getRoll() {
		return roll;
	}
	
	public void setRoll(String roll) {
		this.roll = roll;
	}
	
	public String getPitch() {
		return pitch;
	}
	
	public void setPitch(String pitch) {
		this.pitch = pitch;
	}
	
	public String getYaw() {
		return yaw;
	}
	
	public void setYaw(String yaw) {
		this.yaw = yaw;
	}
	
	public String getBr() {
		return br;
	}
	
	public void setBr(String br) {
		this.br = br;
	}
	
	public String getExtraData() {
		return extraData;
	}
	
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}
	
	public JsonObject getExtraDataJsonObject() {
		return extraDataJsonObject;
	}

	public void setExtraDataJsonObject(JsonObject extraDataJsonObject) {
		this.extraDataJsonObject = extraDataJsonObject;
	}

}
