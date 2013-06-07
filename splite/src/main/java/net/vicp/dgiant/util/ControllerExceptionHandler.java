package net.vicp.dgiant.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler({ Exception.class })
	public void handlePersonNotFound(Exception e, HttpServletResponse response) throws IOException {
		logger.error(e.getMessage());
		e.printStackTrace();
		response.sendError(508, e.getMessage());
		// if we return ResponseEntity<String> here, then it will not go to error page defined in web.xml. 
		// Instead, the message will be shown in a blank page without error code. I don't know why.
	}
}
