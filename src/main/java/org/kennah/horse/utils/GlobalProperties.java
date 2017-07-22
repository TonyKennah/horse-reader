package org.kennah.horse.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class GlobalProperties {

	private String targeturl;
	private String targetpath;
	private String appid;
	private String bfun;
	private String bfpw;
	private String ctpw;
	private String horseserver;
	private String faceurl;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getBfun() {
		return bfun;
	}

	public void setBfun(String bfun) {
		this.bfun = bfun;
	}

	public String getBfpw() {
		return bfpw;
	}

	public void setBfpw(String bfpw) {
		this.bfpw = bfpw;
	}

	public String getCtpw() {
		return ctpw;
	}

	public void setCtpw(String ctpw) {
		this.ctpw = ctpw;
	}

	public String getTargeturl() {
		return targeturl;
	}

	public void setTargeturl(String targeturl) {
		this.targeturl = targeturl;
	}

	public String getTargetpath() {
		return targetpath;
	}

	public void setTargetpath(String targetpath) {
		this.targetpath = targetpath;
	}

	public String getHorseserver() {
		return horseserver;
	}

	public void setHorseserver(String horseserver) {
		this.horseserver = horseserver;
	}

	public String getFaceurl() {
		return faceurl;
	}

	public void setFaceurl(String faceurl) {
		this.faceurl = faceurl;
	}
	
}