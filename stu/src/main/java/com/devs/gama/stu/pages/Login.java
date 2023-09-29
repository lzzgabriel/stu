package com.devs.gama.stu.pages;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.ProfessorDAO;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.exceptions.EntityNotFoundException;
import com.devs.gama.stu.utils.MessageUtils;
import com.devs.gama.stu.utils.NavigationUtils;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

@Named("stLogin")
@ViewScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Application application;

	@Inject
	private ProfessorDAO professorDAO;
	
	private String email;

	private String senha;
	
	private ZoneId zoneId;
	
	private Locale locale;
	
	public void login() {
		try {
			Professor professor = professorDAO.validateLogin(email, senha);
			HttpSession session = SessionUtils.getSession();

			session.setAttribute("stuprofessorid", professor.getId());
			session.setAttribute("stuprofessoremail", professor.getEmail());
			session.setAttribute("stuprofessornome", professor.getNome());

			NavigationUtils.redirect(Pages.home.url);

		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro no banco de dados", null, e.getMessage());
		} catch (EntityNotFoundException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Email ou senha incorretos!");
		} catch (IOException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Falha ao redirecionar");
		}
	}
	
	public void testeCadastro() {
		try {
			Professor professor = new Professor();
			professor.setNome("PROF_TESTE");
			professor.setEmail(email);
			professor.setSenha(senha);
			professorDAO.save(professor);
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Falha no cadastro");
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

	public ZoneId getZoneId() {
		return zoneId;
	}

	public void setZoneId(ZoneId zoneId) {
		this.zoneId = zoneId;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}
