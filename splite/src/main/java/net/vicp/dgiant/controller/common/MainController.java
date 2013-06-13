package net.vicp.dgiant.controller.common;

import java.sql.SQLException;

import net.vicp.dgiant.service.common.UserRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

@Controller
public class MainController {
	
	@Autowired
	private UserRoleService service;
	
	@Autowired
	private JdbcConnectionSource connectionSource;
	
	@RequestMapping("/main")
	public String handle() throws SQLException {
		return "main";
	}
	
	@RequestMapping("/asyn")
	public String asyn() {
		return "user_asyn";
	}
}
