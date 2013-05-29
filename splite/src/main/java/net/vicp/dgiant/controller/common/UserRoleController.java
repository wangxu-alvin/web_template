package net.vicp.dgiant.controller.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.util.Pagination;
import net.vicp.dgiant.validator.common.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
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

	@InitBinder
	public void setValidator(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@RequestMapping(value = "/add")
	public String add(@ModelAttribute("user") @Valid User user,
			BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "add_user";
		}

		return "index";
	}

	@RequestMapping(value = "/list")
	public @ResponseBody
	List<User> list(String type, String condition) {

		List<User> users = new ArrayList<User>();
		try {
			users = queryUsers(type, condition);
		} catch (SQLException e) {
			// TODO @ExceptionHandler
		}

		return users;
	}

	@RequestMapping(value = "/listPage")
	public String listPage(Integer page, String type, String condition,
			ModelMap map) {

		if (page == null) {
			page = 1;
		}

		try {
			Pagination<User> pagination = null;
			if (condition == null) {
				pagination = service.queryPaginatedUsers(page, 10,
						"listPage.jspa");
			} else if ("name".equals(type)) {
				pagination = service.queryUsersByName(condition, page, 10,
						"listPage.jspa");
			} else {
				pagination = service.queryPaginatedUsers(page, 10,
						"listPage.jspa");
			}
			map.addAttribute("pageUsers", pagination.getData());
			map.addAttribute("footer", pagination.getFooter());
		} catch (SQLException e) {
			// TODO @ExceptionHandler
		}
		return "index";
	}

	private List<User> queryUsers(String type, String condition)
			throws SQLException {
		if ("email".equals(type)) {
			return service.queryUsersByEmail(condition);
		}
		return service.queryUsersByName(condition);
	}

	@RequestMapping(value = "/init")
	public String init(ModelMap model) {
		return "index";
	}
}
