package com.hongbog.dto;

import java.util.Arrays;

public class SensorDto {
	
	private String id;
	private byte[] imageByteArray;
	private String encodedImage;
	private String label;
	private String roll;
	private String pitch;
	private String yaw;
	private String br;
	private String registerDate;
	
	private SensorDto() {}
	
	public SensorDto(byte[] imageByteArray, String label, String roll, String pitch, String yaw, String br) {
		super();
		this.imageByteArray = imageByteArray;
		this.label = label;
		this.roll = roll;
		this.pitch = pitch;
		this.yaw = yaw;
		this.br = br;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	public byte[] getImageByteArray() {
		return imageByteArray;
	}
	public void setImageByteArray(byte[] imageByteArray) {
		this.imageByteArray = imageByteArray;
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
	
	
}
