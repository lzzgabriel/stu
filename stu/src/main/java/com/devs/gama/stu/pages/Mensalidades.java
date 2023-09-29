package com.devs.gama.stu.pages;

import java.io.Serializable;

import org.primefaces.model.LazyDataModel;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Mensalidade;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stMensalidades")
@ViewScoped
public class Mensalidades implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Application application;
	
	@Inject
	private LazyDataModel<Mensalidade> lazyDataModel;
	
	private boolean logMode = false;
	
	public void enableLogMode() {
		logMode = true;
	}

	public LazyDataModel<Mensalidade> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel(LazyDataModel<Mensalidade> lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}

	public boolean isLogMode() {
		return logMode;
	}

	public void setLogMode(boolean logMode) {
		this.logMode = logMode;
	}
	
}
