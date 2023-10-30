package com.devs.gama.stu.converters;

import com.devs.gama.stu.entities.FormaPagamento;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "formaPagamentoConverter")
public class FormaPagamentoConverter implements Converter<FormaPagamento> {

	@Override
	public FormaPagamento getAsObject(FacesContext context, UIComponent component, String value) {
		String[] arr = value.split("-");
		
		var formaPagamento = new FormaPagamento();
		formaPagamento.setId(Integer.valueOf(arr[0]));
		formaPagamento.setDescricao(arr[1]);
		
		return formaPagamento;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, FormaPagamento value) {
		return value.getId() + "-" + value.getDescricao();
	}

}
