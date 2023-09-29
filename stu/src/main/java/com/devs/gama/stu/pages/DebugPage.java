package com.devs.gama.stu.pages;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.devs.gama.stu.utils.MessageUtils;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named("stDebugPage")
@ViewScoped
public class DebugPage implements Serializable {

	private static final long serialVersionUID = -2736251001700973914L;

	private LocalDateTime localDateTime;
	
	private String timeZone;

	public void displaytime() {
		MessageUtils.addInfoMessage(localDateTime.atZone(ZoneId.of("UTC")).toString());
		MessageUtils.addInfoMessage(timeZone);
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
