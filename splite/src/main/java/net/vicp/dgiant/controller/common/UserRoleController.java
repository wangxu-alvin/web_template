package net.vicp.dgiant.controller.common;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import net.vicp.dgiant.entry.common.Role;
import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.exception.DataExpiredException;
import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.util.Constants;
import net.vicp.dgiant.util.Pagination;
import net.vicp.dgiant.util.QueryForm;
import net.vicp.dgiant.validator.common.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserRoleController {

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private UserRoleService service;
	
	@InitBinder({"user"})
	public void setValidator(WebDataBinder binder) {
		binder.setValidator(userValidator);

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}
	
	@RequestMapping(value = "/prepareOperation")
	public String prepare(Integer id, ModelMap model) throws SQLException {
		User user = null;
		if (id == null) {
			user = new User();
		} else {
			user = service.queryUserById(id);
		}
		model.addAttribute("user", user);

		return "user_operation";
	}
	

	@RequestMapping(value = "/addUser")
	public String add(@ModelAttribute("user") @Valid User user,
			BindingResult result, ModelMap model) throws SQLException {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "user_operation";
		}
		
		service.createUser(user);

		return "redirect:listUser.jspa";
	}
	
	@RequestMapping(value = "/updateUser")
	public String update(@ModelAttribute("user") @Valid User user,
			BindingResult result, ModelMap model) throws SQLException, DataExpiredException {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "user_operation";
		}
		
		service.updateUser(user);

		return "redirect:listUser.jspa";
	}
	
	@RequestMapping(value = "/deleteUser")
	public String delete(Integer id) throws SQLException, DataExpiredException {

		if (id == null) {
			throw new IllegalArgumentException("Wrong aregument when doing delete");
		}
		
		service.deleteUser(id);

		return "redirect:listUser.jspa";
	}

	@RequestMapping(value = "/list")
	public @ResponseBody List<Role> listRoles(String type, String condition) throws SQLException {
		List<Role> roles = new ArrayList<Role>();
		roles = service.queryRoles();
		return roles;
	}

	@RequestMapping(value = "/listUser")
	public String listUsers(Integer page, @ModelAttribute("form") QueryForm form,
			ModelMap map) throws PaginationException, SQLException {
		
		//TODO if we don't check page here, just put null page to the other methods, 
		//null pointer exception will be raised no matter if you use it or not.
		if (page == null) {
			page = 1;
		}
		
		Pagination<User> pagination = null;
		if (form == null || form.getCondition() == null) {
			pagination = service.queryPaginatedUsers(page, 10,
					"listUser.jspa");
		} else if ("name".equals(form.getType())) {
			pagination = service.queryUsersByName(form.getCondition(), page, 10,
					"listUser.jspa");
		} else {
			pagination = service.queryUsersByEmail(form.getCondition(), page, 10,
					"listUser.jspa");
		}
		// save the query condition
		map.addAttribute("form", form);
		map.addAttribute("pageUsers", pagination.getData());
		map.addAttribute("footer", pagination.getFooter());
		
		return "user_index";
	}
}
