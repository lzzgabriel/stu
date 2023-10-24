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
import com.devs.gama.stu.utils.SessionUtils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.inject.Inject;

@Model
public class LazyMensalidadeDataModel extends LazyDataModel<Mensalidade> {

	private static final long serialVersionUID = 1L;

	@Inject
	private Application application;

	@Inject
	private MensalidadeDAO mensalidadeDAO;

	private Aluno aluno;

	private boolean logMode = false;

	@Override
	public int count(Map<String, FilterMeta> filterBy) {
		try {
			if (!logMode) {
				return mensalidadeDAO.findCountMensalidadeAberta(SessionUtils.getLoggedProfessor());
			} else {
				return mensalidadeDAO.findCountMensalidadeCobrada(SessionUtils.getLoggedProfessor());
			}
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(),e);
			return 0;
		}
	}

	@Override
	public List<Mensalidade> load(int first, int pageSize, Map<String, SortMeta> sortBy,
			Map<String, FilterMeta> filterBy) {

		try {
			if (!logMode) {
				return mensalidadeDAO.paginationMensalidadeAberta(SessionUtils.getLoggedProfessor(), first, pageSize);
			} else {
				return mensalidadeDAO.paginationMensalidadeCobrada(SessionUtils.getLoggedProfessor(), first, pageSize);
			}
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

	public boolean isLogMode() {
		return logMode;
	}

	public void setLogMode(boolean logMode) {
		this.logMode = logMode;
	}

}
