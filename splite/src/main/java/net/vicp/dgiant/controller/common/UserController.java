package net.vicp.dgiant.controller.common;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.exception.DataExpiredException;
import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.pagination.Pagination;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.util.Constants;
import net.vicp.dgiant.util.CompatibleDateEditor;
import net.vicp.dgiant.util.QueryForm;
import net.vicp.dgiant.validator.common.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes({"form", "page", "capacity"})
@Controller
public class UserController {

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
		binder.registerCustomEditor(Date.class, new CompatibleDateEditor(
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
	
	@RequestMapping(value = "/listUser")
	public String listUsers(@ModelAttribute("page") int page, @ModelAttribute("capacity") int capacity, @ModelAttribute("form") QueryForm form,
			ModelMap map) throws PaginationException, SQLException {
		
		Pagination<User> pagination = null;
		if (form == null || form.getCondition() == null || "".equals(form.getCondition())) {
			pagination = service.queryPaginatedUsers(page, capacity);
		} else if ("name".equals(form.getType())) {
			pagination = service.queryUsersByName(form.getCondition(), page, capacity);
		} else {
			pagination = service.queryUsersByEmail(form.getCondition(), page, capacity);
		}
		// save the query condition
		map.addAttribute("form", form);
		map.addAttribute("pagination", pagination);
		
		return "user_index";
	}
	
	@RequestMapping(value = "/prepare")
	public String processRequest(Integer page, Integer capacity, QueryForm form, ModelMap map) {
		
		if (page == null) {
			page = 1;
		}
		
		if (capacity == null) {
			if (map.get("capacity") == null) {
				capacity = 10;
			} else {
				capacity = (Integer) map.get("capacity");
			}
		}
		
		if (form == null) {
			form = new QueryForm();
		}
		
		map.put("page", page);
		map.put("capacity", capacity);
		map.put("form", form);
		
		return "redirect:listUser.jspa";
	}
}
