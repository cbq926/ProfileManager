package com.mycreat.profilemanager.data;

public class Profile {
	private int id;
	private String name;
	private int pic;
	private int audioRing;
	private int audioCall;
	private int audioAlarm;
	private int audioMusic;
	private int vibrator;
	private int airplane;
	private int wifi;
	private int bluetooth;
	private long createTime;
	private long updateTime;
	private boolean isActive;
	private int used;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPic() {
		return pic;
	}
	public void setPic(int pic) {
		this.pic = pic;
	}
	public int getAudioRing() {
		return audioRing;
	}
	public void setAudioRing(int audioRing) {
		this.audioRing = audioRing;
	}
	public int getAudioCall() {
		return audioCall;
	}
	public void setAudioCall(int audioCall) {
		this.audioCall = audioCall;
	}
	public int getAudioAlarm() {
		return audioAlarm;
	}
	public void setAudioAlarm(int audioAlarm) {
		this.audioAlarm = audioAlarm;
	}
	public int getAudioMusic() {
		return audioMusic;
	}
	public void setAudioMusic(int audioMusic) {
		this.audioMusic = audioMusic;
	}
	public int getVibrator() {
		return vibrator;
	}
	public void setVibrator(int vibrator) {
		this.vibrator = vibrator;
	}
	public int getAirplane() {
		return airplane;
	}
	public void setAirplane(int airplane) {
		this.airplane = airplane;
	}
	public int getWifi() {
		return wifi;
	}
	public void setWifi(int wifi) {
		this.wifi = wifi;
	}
	public int getBluetooth() {
		return bluetooth;
	}
	public void setBluetooth(int bluetooth) {
		this.bluetooth = bluetooth;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}
}
