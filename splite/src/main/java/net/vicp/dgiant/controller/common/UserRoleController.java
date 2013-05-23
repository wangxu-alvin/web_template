package net.vicp.dgiant.controller.common;

import javax.validation.Valid;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.validator.common.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	
	@Autowired
	private UserValidator userValidator;
	
	@InitBinder
	public void setValidator(WebDataBinder binder)
	{
		binder.setValidator(userValidator);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addCustomer(@Valid User user) {
		
		return "index";
	}
}
