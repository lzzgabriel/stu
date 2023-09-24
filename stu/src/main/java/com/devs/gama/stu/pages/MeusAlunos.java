package com.devs.gama.stu.pages;

import java.io.Serializable;

import org.primefaces.model.LazyDataModel;

import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.utils.MessageUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stMeusAlunos")
@ViewScoped
public class MeusAlunos implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private LazyDataModel<Aluno> lazyDataModel;
	
	@PostConstruct
	public void init() {
		Object o = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("message");
		if (o == null)
			return;
		MessageUtils.addMessage((FacesMessage) o, "messages");
	}
	
	public LazyDataModel<Aluno> getLazyDataModel() {
		return lazyDataModel;
	}
	
}
