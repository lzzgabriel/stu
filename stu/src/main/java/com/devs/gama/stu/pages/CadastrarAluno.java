package com.devs.gama.stu.pages;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.AlunoDAO;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.utils.MessageUtils;
import com.devs.gama.stu.utils.NavigationUtils;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("stCadastrarAluno")
@ViewScoped
public class CadastrarAluno implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Application application;
	
	@Inject
	private AlunoDAO alunoDAO;
	
	private Aluno aluno;
	
	@PostConstruct
	public void init() {
		aluno = new Aluno();
	}
	
	public void save() {
		try {
			Professor professor = new Professor();
			professor.setId(Integer.valueOf(SessionUtils.getLoggedProfessorId()));
			alunoDAO.save(professor, aluno);
			
			MessageUtils.addExternalMessage(FacesMessage.SEVERITY_INFO, "Aluno cadastrado com sucesso", null);

			NavigationUtils.redirect(Pages.meusAlunos.url);
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro ao cadastrar aluno", "messages", e.getMessage());
			
		} catch (IOException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro ao redirecionar", "messages", e.getMessage());
		}
	}
	
	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

}
