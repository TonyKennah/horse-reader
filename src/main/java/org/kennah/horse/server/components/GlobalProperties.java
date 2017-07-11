package org.kennah.horse.server.components;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties 
public class GlobalProperties {

    private String targeturl;
    private String targetpath;
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
    

}