package com.devs.gama.stu.pages;

import java.io.Serializable;
import java.sql.SQLException;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.ProfessorDAO;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.MessageUtils;
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
	
	private boolean editMode = false;

	@PostConstruct
	public void init() {
		load();
	}

	private void load() {
		try {
			String id = SessionUtils.getLoggedProfessorId();
			professor = professorDAO.findById(Integer.valueOf(id));
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro no banco!", "messages", "O banco de dados retornou erro, favor contatar o suporte");
		} catch (EntityNotFoundException e) {
			// Não deveria dar esse erro, já que o user já logou
			application.getLogger().error(e.getMessage(), e);
		}
	}
	
	public void edit() {
		
		try {
			professorDAO.edit(professor);
			editMode = false;
			
			load();
			
			MessageUtils.addInfoMessage("Dados atualizados com sucesso", "messages");
			
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro no banco de dados", "messages", e.getMessage());
		}
	}
	
	public void enterEditMode() {
		editMode = true;
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}
