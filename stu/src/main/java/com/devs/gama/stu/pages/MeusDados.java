package com.devs.gama.stu.pages;

import java.io.Serializable;
import java.sql.SQLException;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.ProfessorDAO;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stMeusDados")
@ViewScoped
public class MeusDados implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Application application;
	
	@Inject
	private ProfessorDAO professorDAO;
	
	private Professor professor;

	@PostConstruct
	public void init() {
		load();
	}

	private void load() {
		try {
			String id = SessionUtils.getLoggedProfessorId();
			professor = professorDAO.findById(Integer.valueOf(id));
		} catch (NumberFormatException | SQLException | EntityNotFoundException e) {
			application.getLogger().error(e.getMessage(), e);
		}
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

}
