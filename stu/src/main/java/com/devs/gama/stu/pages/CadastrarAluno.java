package com.devs.gama.stu.pages;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.AlunoDAO;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.MessageUtils;
import com.devs.gama.stu.utils.NavigationUtils;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
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
		load();
	}

	private void load() {
		try {
			String id = SessionUtils.getLoggedProfessorId();
			aluno = alunoDAO.findById(Integer.valueOf(id));
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
			Professor professor = new Professor();
			professor.setId(Integer.valueOf(SessionUtils.getLoggedProfessorId()));
			alunoDAO.edit(professor, aluno);

			NavigationUtils.redirect(Pages.meusAlunos.url);
			
			MessageUtils.addInfoMessage("Dados atualizados com sucesso", "messages");
			
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
