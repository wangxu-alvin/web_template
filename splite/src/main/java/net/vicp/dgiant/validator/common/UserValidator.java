package net.vicp.dgiant.validator.common;

import net.vicp.dgiant.entry.common.User;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "name", "name.empty");
		ValidationUtils.rejectIfEmpty(errors, "password", "password.empty");
		ValidationUtils.rejectIfEmpty(errors, "email", "email.empty");
		User user = (User) target;
		if(user.getEmail().indexOf("@") == -1) {
			errors.rejectValue("email", "email.wrong.format");
		}
	}

}
