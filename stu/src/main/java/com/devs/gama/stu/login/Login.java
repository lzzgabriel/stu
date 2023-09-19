package com.devs.gama.stu.login;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.devs.gama.stu.daos.ProfessorDAO;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.pages.Pages;
import com.devs.gama.stu.utils.MessageUtils;
import com.devs.gama.stu.utils.NavigationUtils;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@Named("stLogin")
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger logger = LogManager.getLogger();

	private String email;
	private String senha;
	
	public void login() {
		try {
			Professor professor = ProfessorDAO.validateLogin(email, senha);
			HttpSession session = SessionUtils.getSession();
			
			session.setAttribute("stuprofessorid", professor.getId());
			session.setAttribute("stuprofessoremail", professor.getEmail());
			session.setAttribute("stuprofessornome", professor.getNome());
			
			NavigationUtils.redirect("index.xhtml");
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro no banco de dados", null, e.getMessage());
		} catch (EntityNotFoundException e) {
			logger.error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Email ou senha incorretos!");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Falha ao redirecionar");
		}
	}
	
	public void testeCadastro() {
		try {
			Professor professor = new Professor();
			professor.setNome("PROF_TESTE");
			professor.setEmail(email);
			professor.setSenha(senha);
			ProfessorDAO.save(professor);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Falha no cadastro");
		}
	}
	
	public void logout() {
		try {
			HttpSession session = SessionUtils.getSession();
			session.invalidate();
			NavigationUtils.redirect(Pages.LOGIN.url);
		} catch (IOException e) {
			logger.error(e.getMessage());
			MessageUtils.addErrorMessage("Erro ao redirecionar a página", null, e.getMessage());
		}
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
