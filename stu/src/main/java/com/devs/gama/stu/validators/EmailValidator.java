package com.devs.gama.stu.validators;

import java.util.regex.Pattern;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.FacesValidator;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;

@FacesValidator(value = "emailValidator")
public class EmailValidator implements Validator<String> {
	
	private static final Pattern ptr = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

	/**
	 * This method accepts empty strings as valid, leaving these for the "required" type of validation
	 * in the JSF components to handle.
	 */
	@Override
	public void validate(FacesContext context, UIComponent component, String value) throws ValidatorException {
		if (value.isEmpty())
			return;
		if (!ptr.matcher(value).matches()) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-mail inválido!", "O valor \"" + value + "\" não é válido como endereço de e-mail");
			throw new ValidatorException(message);
		}
		
	}

}
