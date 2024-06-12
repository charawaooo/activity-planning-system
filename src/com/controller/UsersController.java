package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;//new
import org.springframework.mail.SimpleMailMessage;//new
import org.springframework.mail.javamail.JavaMailSender;//new
import org.springframework.web.bind.annotation.PostMapping;//new
import org.springframework.web.bind.annotation.RequestParam;//new
import org.springframework.web.servlet.ModelAndView;//new

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.entity.Users;
import com.service.UsersService;
import com.util.PageHelper;
import com.util.VeDate;

import static com.util.MD5.md5;

//定义为控制器
@Controller
// 设置路径
@RequestMapping(value = "/users", produces = "text/plain;charset=utf-8")
public class UsersController extends BaseController {
	// 注入Service 由于标签的存在 所以不需要getter setter
	@Autowired
	private UsersService usersService;

	//new
	@Autowired
	private JavaMailSender mailSender;

	//new
	@PostMapping("/sendPasswordResetEmail.action")
	public ModelAndView sendPasswordResetEmail(@RequestParam("username") String username, @RequestParam("contact") String contact) {
		ModelAndView modelAndView = new ModelAndView();
		Users user = usersService.getUsersByUsernameAndContact(username, contact);
		if (user != null) {
			String temporaryPassword = generateTemporaryPassword(); // 生成临时密码
			String encryptedTemporaryPassword = md5(temporaryPassword); // 对临时密码进行MD5加密
			user.setPassword(encryptedTemporaryPassword); // 更新用户密码为临时密码
			usersService.updateUsers(user);
			// 发送包含临时密码的邮件
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(contact);
			message.setSubject("密码重置");
			message.setFrom("3065026296@qq.com");
			message.setText("您的临时密码是：" + temporaryPassword);
			mailSender.send(message);

			modelAndView.addObject("message", "密码重置邮件已发送，请检查您的邮箱。");
		} else {
			modelAndView.addObject("message", "用户账号或邮箱地址不正确，请重新输入。");
		}
		modelAndView.setViewName("users/recoverPassword");
		return modelAndView;
	}

	// 生成临时密码的方法 new
	private String generateTemporaryPassword() {
		// 这里简单地生成一个随机的临时密码，可以根据实际需求使用更加复杂的方式生成
		return UUID.randomUUID().toString().substring(0, 8); // 生成一个8位的随机字符串
	}

	// 准备添加数据
	@RequestMapping("createUsers.action")
	public String createUsers() {
		return "admin/addusers";
	}

	// 添加数据
	@RequestMapping("addUsers.action")
	public String addUsers(Users users) {
		users.setStatus("");
		users.setRegdate(VeDate.getStringDateShort());
		this.usersService.insertUsers(users);
		return "redirect:/users/createUsers.action";
	}

	// 通过主键删除数据
	@RequestMapping("deleteUsers.action")
	public String deleteUsers(String id) {
		this.usersService.deleteUsers(id);
		return "redirect:/users/getAllUsers.action";
	}

	// 批量删除数据
	@RequestMapping("deleteUsersByIds.action")
	public String deleteUsersByIds() {
		String[] ids = this.getRequest().getParameterValues("usersid");
		for (String usersid : ids) {
			this.usersService.deleteUsers(usersid);
		}
		return "redirect:/users/getAllUsers.action";
	}

	// 更新数据
	@RequestMapping("updateUsers.action")
	public String updateUsers(Users users) {
		this.usersService.updateUsers(users);
		return "redirect:/users/getAllUsers.action";
	}

	// 更新状态
	@RequestMapping("status.action")
	public String status(String id) {
		String status = "正常";
		Users users = this.usersService.getUsersById(id);
		if (status.equals(users.getStatus())) {
			status = "锁定";
			users.setStatus(status);
			this.usersService.updateUsers(users);
		} else {
			users.setStatus(status);
			this.usersService.updateUsers(users);
		}
		return "redirect:/users/getAllUsers.action";
	}

	// 显示全部数据
	@RequestMapping("getAllUsers.action")
	public String getAllUsers(String number) {
		List<Users> usersList = this.usersService.getAllUsers();
		PageHelper.getPage(usersList, "users", null, null, 10, number, this.getRequest(), null);
		return "admin/listusers";
	}

	// 按条件查询数据 (模糊查询)
	@RequestMapping("queryUsersByCond.action")
	public String queryUsersByCond(String cond, String name, String number) {
		Users users = new Users();
		if (cond != null) {
			if ("username".equals(cond)) {
				users.setUsername(name);
			}
			if ("password".equals(cond)) {
				users.setPassword(name);
			}
			if ("realname".equals(cond)) {
				users.setRealname(name);
			}
			if ("sex".equals(cond)) {
				users.setSex(name);
			}
			if ("birthday".equals(cond)) {
				users.setBirthday(name);
			}
			if ("contact".equals(cond)) {
				users.setContact(name);
			}
			if ("image".equals(cond)) {
				users.setImage(name);
			}
			if ("status".equals(cond)) {
				users.setStatus(name);
			}
			if ("regdate".equals(cond)) {
				users.setRegdate(name);
			}
		}

		List<String> nameList = new ArrayList<String>();
		List<String> valueList = new ArrayList<String>();
		nameList.add(cond);
		valueList.add(name);
		PageHelper.getPage(this.usersService.getUsersByLike(users), "users", nameList, valueList, 10, number, this.getRequest(), "query");
		name = null;
		cond = null;
		return "admin/queryusers";
	}

	// 按主键查询数据
	@RequestMapping("getUsersById.action")
	public String getUsersById(String id) {
		Users users = this.usersService.getUsersById(id);
		this.getRequest().setAttribute("users", users);
		return "admin/editusers";
	}

	public UsersService getUsersService() {
		return usersService;
	}

	public void setUsersService(UsersService usersService) {
		this.usersService = usersService;
	}

}
// 
