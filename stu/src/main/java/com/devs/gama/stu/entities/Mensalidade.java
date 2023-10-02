package com.devs.gama.stu.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Mensalidade {

	private Aluno aluno;
	private LocalDate mensalidade;
	private LocalDate proximoVencimento;
	private Boolean status;
	private BigDecimal valor;
	private FormaPagamento formaPagamento;
	private ZonedDateTime momentoPagamento;

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
	
	public Date getProximoVencimentoAsDate() {
		return Date.from(proximoVencimento.atStartOfDay(ZoneId.of("UTC")).toInstant());
	}

	public LocalDate getProximoVencimento() {
		return proximoVencimento;
	}

	public void setProximoVencimento(LocalDate proximoVencimento) {
		this.proximoVencimento = proximoVencimento;
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

	public ZonedDateTime getMomentoPagamento() {
		return momentoPagamento;
	}

	public void setMomentoPagamento(ZonedDateTime momentoPagamento) {
		this.momentoPagamento = momentoPagamento;
	}
	
	public static Boolean parse(String s) {
		if (s == null)
			return null;
		return (s.equalsIgnoreCase("em aberto") || s.equalsIgnoreCase("atrasada"));
	}

}
