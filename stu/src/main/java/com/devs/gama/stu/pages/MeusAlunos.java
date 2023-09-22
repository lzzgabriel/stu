package com.devs.gama.stu.pages;

import java.io.Serializable;

import org.primefaces.model.LazyDataModel;

import com.devs.gama.stu.entities.Aluno;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stMeusAlunos")
@ViewScoped
public class MeusAlunos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LazyDataModel<Aluno> lazyDataModel;
	
	public LazyDataModel<Aluno> getLazyDataModel() {
		return lazyDataModel;
	}
	
}
