package com.devs.gama.stu.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import com.devs.gama.stu.utils.StringUtils;

public class Aluno {

	private Integer id;
	private String nome;
	private String email;
	private String celular;
	private Boolean ativo;

	private LocalDateTime momentoCadastro;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular() {
		return celular;
	}

	public String getCelularUnmasked() {
		return celular == null ? null : celular.replace("(", "").replace(")", "").replace("-", "");
	}

	public String getCelularMasked() {
		return StringUtils.formattedTelephone(celular);
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public LocalDateTime getMomentoCadastro() {
		return momentoCadastro;
	}

	public void setMomentoCadastro(LocalDateTime momentoCadastro) {
		this.momentoCadastro = momentoCadastro;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(celular);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		return Objects.equals(celular, other.celular);
	}

}
