package com.javaproject.dianping.controller;

import com.javaproject.dianping.common.*;
import com.javaproject.dianping.dal.UserModelMapper;
import com.javaproject.dianping.model.UserModel;
import com.javaproject.dianping.request.LoginReq;
import com.javaproject.dianping.request.RegisterReq;
import com.javaproject.dianping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@Controller("/user")
@RequestMapping("/user")
public class UserController {

    public static final String CURRENT_USER_SESSION = "currentUserSession";

    /**
     * 这个 httpServletRequest 是 spring 中的一个 bean，在它内部使用 ThreadLocal 的机制使得每一个用户的请求
     * 拿到的或者指向的 httpServletRequest 其实都是属于 ThreadLocal 内的 httpServletRequest， 所以说需要用
     * Autowired private HttpServletRequest httpServletRequest 这个对象，就可以拿到当前线程处理用户请求的
     * 一个 request 对象。
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        String userName = "ren";
        ModelAndView modelAndView = new ModelAndView("/index.html");
        modelAndView.addObject("name", userName);
        return modelAndView;
    }

    @RequestMapping("/get")
    @ResponseBody
    // 根据用户id返回用户对象
    public CommonRes getUser(@RequestParam(name = "id")Integer id) throws BusinessException {
        UserModel userModel = userService.getUser(id);
        if (userModel == null) {
//            return CommonRes.create(new CommonError(EmBusinessError.NO_OBJECT_FOUND), "fail");
            throw new BusinessException(EmBusinessError.NO_OBJECT_FOUND);
        }else{
            return CommonRes.create(userModel);
        }
    }


    /**
     * Controller中的用户注册方法，返回一个 通用返回类 结果
     * 参数：请求入参@RequestBody 入参校验@Valid
     * 其中加入 入参检验，对前端传过来的值进行 输入检验，对应 RegisterReq 类
     */
    @RequestMapping("/register")
    @ResponseBody
    public CommonRes register(@Valid @RequestBody RegisterReq registerReq,
                              BindingResult bindingResult)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        // 如果校验结果存在，抛出异常
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        // 注册信息 赋值
        UserModel registerUser = new UserModel();
        registerUser.setPassword(registerReq.getPassword());
        registerUser.setTelphone(registerReq.getTelphone());
        registerUser.setNickName(registerReq.getNickName());
        registerUser.setGender(registerReq.getGender());

        UserModel resUserModel = userService.register(registerUser);

        return CommonRes.create(resUserModel);

    }


    @RequestMapping("/login")
    @ResponseBody
    public CommonRes login(@RequestBody @Valid LoginReq loginReq, BindingResult bindingResult)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        UserModel userModel = userService.login(loginReq.getTelphone(), loginReq.getPassword());
        /**
         * 当用户的登入生效的时候，需要通过http的方法session将用户的成功注册放到Session类
         */
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION, userModel);

        return CommonRes.create(userModel);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public CommonRes logout() throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 用户注销，使用invalidate()无效掉所有session
        httpServletRequest.getSession().invalidate();
        return CommonRes.create(null);
    }

    // 获取当前用户信息，通过 CURRENT_USER_SESSION
    @RequestMapping("/getcurrentuser")
    @ResponseBody
    public CommonRes getCurrentUser(){
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return CommonRes.create(userModel);
    }


}
