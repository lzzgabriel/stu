package com.devs.gama.stu.lazy;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.MensalidadeDAO;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Mensalidade;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Model
public class LazyMensalidadeDataModel extends LazyDataModel<Mensalidade> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Application application;
	
	@Inject
	private MensalidadeDAO MensalidadeDAO;
	
	private Aluno aluno;
	
	@PostConstruct
	public void init() {
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		return 1;
	}

	@Override
	public List<Mensalidade> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		
		try {
			// TODO fazer filtragem de Mensalidades
			// TODO pagination
			return MensalidadeDAO.findAll();
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			return null;
		}
	}

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	
}