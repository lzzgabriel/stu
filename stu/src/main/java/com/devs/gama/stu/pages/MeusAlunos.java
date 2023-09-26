package com.devs.gama.stu.pages;

import java.io.Serializable;
import java.sql.SQLException;

import org.primefaces.model.LazyDataModel;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.AlunoDAO;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.utils.MessageUtils;
import com.devs.gama.stu.utils.SessionUtils;

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
	private Application application;
	
	@Inject
	private AlunoDAO alunoDAO;
	
	@Inject
	private LazyDataModel<Aluno> lazyDataModel;
	
	private Aluno aluno;
	private Professor professor;
	
	private boolean insertMode = false;
	private boolean editMode = false;
	
	@PostConstruct
	public void init() {
		
		professor = SessionUtils.getLoggedProfessor();
		
		Object o = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("message");
		if (o == null)
			return;
		MessageUtils.addMessage((FacesMessage) o, "messages");
	}
	
	public void save() {
		try {
			professor.setId(Integer.valueOf(SessionUtils.getLoggedProfessorId()));
			alunoDAO.save(professor, aluno);
			
			MessageUtils.addInfoMessage("Aluno cadastrado");

			normalMode();
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro ao cadastrar aluno", "messages", e.getMessage());
		}
	}
	
	public void edit() {
		try {
			professor.setId(Integer.valueOf(SessionUtils.getLoggedProfessorId()));
			alunoDAO.edit(professor, aluno);
			
			MessageUtils.addInfoMessage("Dados alterados");

			normalMode();
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro ao cadastrar aluno", "messages", e.getMessage());
		}
	}
	
	public void normalMode() {
		editMode = false;
		insertMode = false;
	}
	
	public void enableInsertMode() {
		aluno = new Aluno();
		editMode = false;
		insertMode = true;
	}
	
	public void enableEditMode(Aluno aluno) {
		this.aluno = aluno;
		insertMode = false;
		editMode = true;
	}
	
	public LazyDataModel<Aluno> getLazyDataModel() {
		return lazyDataModel;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public boolean isInsertMode() {
		return insertMode;
	}

	public boolean isEditMode() {
		return editMode;
	}

}
