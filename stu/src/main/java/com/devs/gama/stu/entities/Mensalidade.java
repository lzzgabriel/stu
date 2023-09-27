package com.devs.gama.stu.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.devs.gama.stu.exceptions.CallNotFoundException;

public class Mensalidade {

	private Aluno aluno;
	private LocalDate mensalidade;
	private LocalDate proximoVencimento;
	private Status status;
	private BigDecimal valor;
	private FormaPagamento formaPagamento;
	private LocalDateTime momentoPagamento;

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

	public LocalDate getProximoVencimento() {
		return proximoVencimento;
	}

	public void setProximoVencimento(LocalDate proximoVencimento) {
		this.proximoVencimento = proximoVencimento;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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

	public LocalDateTime getMomentoPagamento() {
		return momentoPagamento;
	}

	public void setMomentoPagamento(LocalDateTime momentoPagamento) {
		this.momentoPagamento = momentoPagamento;
	}

	public static enum Status {
		EM_ABERTO, ATRASADA;

		public static Status parse(String s) {
			return Arrays.asList(Status.values()).stream().filter(status -> s.equalsIgnoreCase(status.name()))
					.findFirst()
					.orElseThrow(() -> new CallNotFoundException("Status da mensalidade não encontrado: " + s));
		}
	}
}
