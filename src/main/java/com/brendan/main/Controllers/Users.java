package com.brendan.main.Controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.brendan.main.Models.User;
import com.brendan.main.Services.UserService;
import com.brendan.main.Validators.UserValidator;

@Controller
public class Users {
    private final UserService userService;
    private UserValidator userValidator;
    
    public Users(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }
    
    @RequestMapping("/registration")
    public String registerForm(@ModelAttribute("user") User user) {
        return "registrationPage.jsp";
    }

    
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session) {
        // if result has errors, return the registration page (don't worry about validations just now)
        // else, save the user in the database, save the user id in session, and redirect them to the /home route
    	userValidator.validate(user, result);
    	if(result.hasErrors()) {
    		return "registrationPage.jsp";
    	}
    	User u = userService.registerUser(user);
    	session.setAttribute("userid", u.getId());
    	return "redirect:/home";
    	
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session) {
        // if the user is authenticated, save their user id in session
        // else, add error messages and return the login page
    	if(userService.authenticateUser(email, password)) {
    		User u = userService.findByEmail(email);
    		session.setAttribute("userid", u.getId());
    		return "redirect:/home";
    	}
    	session.setAttribute("error", "Invalid!!");
    	return "redirect:/registration";
    }
    
    @RequestMapping("/home")
    public String home(HttpSession session, Model model) {
        // get user from session, save them in the model and return the home page
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}else {
    		return "homePage.jsp";
    	}
    }
    
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        // invalidate session
        // redirect to login page
    	if(session.getAttribute("userid") == null) {
    		return "redirect:/login";
    	}else {
    		session.removeAttribute("userid");
    		return "redirect:/login";
    	}
    }
}
