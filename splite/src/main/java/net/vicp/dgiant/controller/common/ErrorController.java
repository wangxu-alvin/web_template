package net.vicp.dgiant.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
	
	@RequestMapping("/error")
	public String handle(HttpServletRequest request, ModelMap map) {
		map.addAttribute("error_code", request.getAttribute("javax.servlet.error.status_code"));
		map.addAttribute("error_message", request.getAttribute("javax.servlet.error.message"));
		return "error";
	}
}
