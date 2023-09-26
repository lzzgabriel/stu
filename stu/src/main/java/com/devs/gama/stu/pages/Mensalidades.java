package com.devs.gama.stu.pages;

import java.io.Serializable;

import org.primefaces.model.LazyDataModel;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Mensalidade;

import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stMensalidades")
public class Mensalidades implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Application application;
	
	@Inject
	private LazyDataModel<Mensalidade> lazyDataModel;
	
	private Aluno aluno;
	
	public void load() {
		//TODO
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	
		
}
