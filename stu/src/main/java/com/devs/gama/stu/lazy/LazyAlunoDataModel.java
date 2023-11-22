package com.devs.gama.stu.lazy;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.AlunoDAO;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.Professor;
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

@Model
public class LazyAlunoDataModel extends LazyDataModel<Aluno> {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Application application;
	
	@Inject
	private AlunoDAO alunoDAO;
	
	private Professor professor;
	
	@PostConstruct
	public void init() {
		professor = SessionUtils.getLoggedProfessor();
	}

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		
		try {
//			 TODO fazer filtragem de alunos
			return alunoDAO.findCount(professor);
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			return 0;
		}
		
	}

	@Override
	public List<Aluno> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
		
		try {
			// TODO fazer filtragem de alunos
			return alunoDAO.pagination(professor, first, pageSize);
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			return null;
		}
	}

	public Professor getProfessor() {
		return professor;
	}

	public void setProfessor(Professor professor) {
		this.professor = professor;
	}

}
