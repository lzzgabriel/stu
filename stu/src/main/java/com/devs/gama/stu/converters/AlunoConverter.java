package com.devs.gama.stu.converters;

import com.devs.gama.stu.entities.Aluno;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("alunoConverter")
public class AlunoConverter implements Converter<Aluno> {

	@Override
	public Aluno getAsObject(FacesContext context, UIComponent component, String value) {
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Aluno value) {
		return null;
	}

}
