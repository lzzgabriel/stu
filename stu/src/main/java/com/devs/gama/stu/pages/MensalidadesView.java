package com.devs.gama.stu.pages;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.primefaces.model.LazyDataModel;

import com.devs.gama.stu.app.Application;
import com.devs.gama.stu.daos.FormaPagamentoDAO;
import com.devs.gama.stu.daos.MensalidadeDAO;
import com.devs.gama.stu.entities.Aluno;
import com.devs.gama.stu.entities.FormaPagamento;
import com.devs.gama.stu.entities.Mensalidade;
import com.devs.gama.stu.lazy.LazyMensalidadeDataModel;
import com.devs.gama.stu.utils.MessageUtils;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class MensalidadesView implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Application application;

	@Inject
	private MensalidadeDAO mensalidadeDAO;

	@Inject
	private FormaPagamentoDAO formaPagamentoDAO;

	@Inject
	private LazyMensalidadeDataModel lazyDataModel;

	private Aluno selectedAluno;
	private FormaPagamento selectedFormaPagamento;
	private Mensalidade selectedMensalidade;

	private boolean logMode = false;

	private List<FormaPagamento> availableFormaPagamentos;

	public void enableLogMode() {
		logMode = true;
		lazyDataModel.setLogMode(true);
	}

	public void disableLogMode() {
		logMode = false;
		lazyDataModel.setLogMode(false);
	}

	public void confirmarPagamento() {
		try {
			mensalidadeDAO.confirmPay(selectedAluno, selectedFormaPagamento);
			MessageUtils.addInfoMessage("Pagamento confirmado", null);
		} catch (Exception e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro ao confirmar pagamento", null);
		}
	}
	
	public void editarMensalidade() {
		try {
			mensalidadeDAO.editMensalidadeAberta(selectedMensalidade);
			MessageUtils.addInfoMessage("Mensalidade alterada", null);
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
			MessageUtils.addErrorMessage("Erro ao alterar a mensalidade", null);
		}
	}

	public void loadFormaPagamento() {
		try {
			if (Objects.isNull(availableFormaPagamentos))
				availableFormaPagamentos = formaPagamentoDAO.findAll();
		} catch (SQLException e) {
			application.getLogger().error(e.getMessage(), e);
		}
	}

	public LazyDataModel<Mensalidade> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel(LazyMensalidadeDataModel lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}

	public Aluno getSelectedAluno() {
		return selectedAluno;
	}

	public void setSelectedAluno(Aluno selectedAluno) {
		this.selectedAluno = selectedAluno;
		lazyDataModel.setAluno(selectedAluno);
	}

	public FormaPagamento getSelectedFormaPagamento() {
		return selectedFormaPagamento;
	}

	public void setSelectedFormaPagamento(FormaPagamento selectedFormaPagamento) {
		this.selectedFormaPagamento = selectedFormaPagamento;
	}

	public Mensalidade getSelectedMensalidade() {
		return selectedMensalidade;
	}

	public void setSelectedMensalidade(Mensalidade selectedMensalidade) {
		this.selectedMensalidade = selectedMensalidade;
	}

	public boolean isLogMode() {
		return logMode;
	}

	public void setLogMode(boolean logMode) {
		this.logMode = logMode;
	}

	public List<FormaPagamento> getAvailableFormaPagamentos() {
		return availableFormaPagamentos;
	}

}
