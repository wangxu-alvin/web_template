package net.vicp.dgiant.controller.common;

import javax.validation.Valid;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.validator.common.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserRoleController {
	
	@Autowired
	private UserValidator userValidator;
	
	@InitBinder
	public void setValidator(WebDataBinder binder)
	{
		binder.setValidator(userValidator);
	}
	
	@RequestMapping(value = "/add")
	public String addCustomer(@ModelAttribute("user") @Valid User user, BindingResult result) {
		
		if (result.hasErrors())
		{
			System.out.println("------------------------");
		}
		
		return "AddUser";
	}
	
	@RequestMapping(value = "/init")
	public String init(ModelMap model) {
		model.addAttribute(new User("wangxu", "111", "sacat.wx@gmail.com"));
		return "AddUser";
	}
}
