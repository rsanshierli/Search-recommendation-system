package com.javaproject.dianping.controller.admin;


import com.javaproject.dianping.common.AdminPermission;
import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.common.CommonRes;
import com.javaproject.dianping.common.EmBusinessError;
import com.javaproject.dianping.service.CategoryService;
import com.javaproject.dianping.service.SellerService;
import com.javaproject.dianping.service.ShopService;
import com.javaproject.dianping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


// 两层 admin， 第一层表示这是admin相关的url，第二层表示admin相关的操作
@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {

    /**
     * 由于是 admin 的后台管理，不再是遵从返回Response Json的方式，而是直接返回 Thymeleaf 动态渲染的 View
     */

    // 配置文件中的admin账户信息
    @Value("${admin.email}")
    private String email;

    @Value("${admin.encryptPassword}")
    private String encryptPassword;

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private SellerService sellerService;

    // 用 session 管理 admin 登入的状态
    @Autowired
    private HttpServletRequest httpServletRequest;

    public static final String CURRENT_ADMIN_SESSION = "currentAdminSession";

    @RequestMapping("/index")
    @AdminPermission
//    @AdminPermission(produceType = "application/json")
//    @ResponseBody
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/admin/admin/index");
        modelAndView.addObject("userCount",userService.countAllUser()); // 运营后台首页的 注册用户统计
        modelAndView.addObject("shopCount",shopService.countAllShop()); //
        modelAndView.addObject("categoryCount",categoryService.countAllCategory()); //
        modelAndView.addObject("sellerCount",sellerService.countAllSeller()); //
        modelAndView.addObject("CONTROLLER_NAME","admin");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
//        return CommonRes.create(null);
    }

    // 登入页面，无需任何操作，只需要返回登入页面
    @RequestMapping("/loginpage")
    public ModelAndView loginpage() {
        ModelAndView modelAndView = new ModelAndView("/admin/admin/login");
        return modelAndView;
    }

    // 登入接口，真正的登入方法, request为post操作
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(name = "email")String email,
                              @RequestParam(name = "password")String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名和密码不能为空");
        }

        if (email.equals(this.email) && encodeByMd5(password).equals(this.encryptPassword)) {
            // 登入成功，保存session状态，重定向到首页
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION, email);
            return "redirect:/admin/admin/index";
        }else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户名或密码错误");
        }
    }



    // 确认MD5计算方法，用于将铭文密码进行MD5加密转换
    private String encodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确认计算方法MD5
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
    }

}
