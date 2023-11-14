package com.devs.gama.stu.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Referente às tabelas {@code mensalidade_aberta} e {@code mensalidade_cobrada}
 * 
 * @author Gabriel Lezo
 */
public class Mensalidade {

	private Aluno          aluno;
	private LocalDate      mensalidade;
	private LocalDate      vencimento;
	private Boolean        status;
	private BigDecimal     valor;
	private FormaPagamento formaPagamento;
	private ZonedDateTime  momentoPagamento;
	
	private int            diaVencimento;

	public Aluno getAluno() {
		return aluno;
	}

	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}

	public LocalDate getMensalidade() {
		return mensalidade;
	}

	public void setMensalidade(LocalDate mensalidade) {
		this.mensalidade = mensalidade;
	}
	
	public Date getVencimentoAsDate() {
		return Date.from(vencimento.atStartOfDay(ZoneId.of("UTC")).toInstant());
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate proximoVencimento) {
		this.vencimento = proximoVencimento;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	public Date getMomentoPagamentoAsDate() {
		return Date.from(momentoPagamento.toInstant());
	}

	public ZonedDateTime getMomentoPagamento() {
		return momentoPagamento;
	}

	public void setMomentoPagamento(ZonedDateTime momentoPagamento) {
		this.momentoPagamento = momentoPagamento;
	}
	
	public int getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(int diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	public static Boolean parse(String s) {
		if (s == null)
			return null;
		return (s.equalsIgnoreCase("em aberto"));
	}

}
