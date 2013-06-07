package net.vicp.dgiant.controller.common;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.exception.DataExpiredException;
import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.util.Constants;
import net.vicp.dgiant.util.CompatibleDateEditor;
import net.vicp.dgiant.util.Pagination;
import net.vicp.dgiant.util.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserAsynController {

	@Autowired
	private UserRoleService service;
	
	@InitBinder({"user"})
	public void setValidator(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CompatibleDateEditor(
				dateFormat, true));
	}
	
	@RequestMapping(value = "/addUserAsyn")
	public @ResponseBody Result add(User user) throws SQLException {
		
		service.createUser(user);

		return new Result(true);
	}
	
	@RequestMapping(value = "/updateUserAsyn")
	public @ResponseBody Result update(User user) {
		Result result = null;
		try {
			service.updateUser(user);
			result = new Result(true);
		} catch (SQLException e) {
			result = new Result(false, e.getMessage());
		} catch (DataExpiredException e) {
			result = new Result(false, e.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/deleteUserAsyn")
	public @ResponseBody Result deleteAsyn(Integer id) throws SQLException, DataExpiredException {

		if (id == null) {
			throw new IllegalArgumentException("Wrong aregument when doing delete");
		}
		
		service.deleteUser(id);
		return new Result(true);
	}

	@RequestMapping(value = "/userAsyn", method = RequestMethod.POST)
    public
    @ResponseBody
    Pagination<User> listUsers(Integer page, Integer rows, String type, String condition) throws PaginationException, SQLException {
		
		if (page == null) {
			page = 1;
		}
		
		if (rows == null) {
			rows = 10;
		}
		
		Pagination<User> pagination = null;
		if (condition == null) {
			pagination = service.queryPaginatedUsers(page, rows);
		} else if ("name".equals(type)) {
			pagination = service.queryUsersByName(condition, page, rows, null);
		} else {
			pagination = service.queryUsersByEmail(condition, page, rows, null);
		}
		
        return pagination;
    }
}
