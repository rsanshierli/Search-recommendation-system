package com.javaproject.dianping.controller.admin;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaproject.dianping.common.*;
import com.javaproject.dianping.model.SellerModel;
import com.javaproject.dianping.request.PageQuery;
import com.javaproject.dianping.request.SellerCreateReq;
import com.javaproject.dianping.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller("/admin/seller")
@RequestMapping("/admin/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    // 商户列表
    @RequestMapping("/index")
    @AdminPermission  // 权限管理，只有登入后的admin才有权限
    public ModelAndView index(PageQuery pageQuery) { // 由于pagequery类中设有初始值，无需进行输入校验

        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize()); // 向当前线程的ThreadLocal内设置一下需要从第几页开始，查多少数据
                                                                        // 当mybatis向下执行sql语句时，就会自动将 limit 约束设置到sql上
                                                                        // 实现不改动sql语句的情况下，实现分页查询

        List<SellerModel> sellerModelList = sellerService.selectAll();

        PageInfo<SellerModel> sellerModelPageInfo = new PageInfo<>(sellerModelList);

        ModelAndView modelAndView = new ModelAndView("/admin/seller/index.html");
        modelAndView.addObject("data",sellerModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME", "seller"); // 视图高亮
        modelAndView.addObject("ACTION_NAME", "index");
        return modelAndView;
    }

    // 只返回 商户创建 的页面
    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage() {

        ModelAndView modelAndView = new ModelAndView("/admin/seller/create.html");
        modelAndView.addObject("CONTROLLER_NAME", "seller");
        modelAndView.addObject("ACTION_NAME", "create");
        return modelAndView;
    }

    // 真正的 商户创建 操作
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid SellerCreateReq sellerCreateReq, BindingResult bindingResult) throws BusinessException {
        // 如果校验结果存在，抛出异常
        if (bindingResult.hasErrors()) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }

        SellerModel sellerModel = new SellerModel();
        sellerModel.setName(sellerCreateReq.getName());
        sellerService.create(sellerModel);

        return "redirect:/admin/seller/index";
    }

    // 商户 启用 禁用
    // 通过 jquary ajax方法，并不是通过界面跳转，这样对于admin用户在使用的时候没有必要来回切换form
    @RequestMapping(value = "down", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes down(@RequestParam(value = "id") Integer id) throws BusinessException {
        SellerModel sellerModel = sellerService.changeStatus(id, 1);

        return CommonRes.create(sellerModel);

    }

    @RequestMapping(value = "up", method = RequestMethod.POST)
    @AdminPermission
    @ResponseBody
    public CommonRes up(@RequestParam(value = "id") Integer id) throws BusinessException {
        SellerModel sellerModel = sellerService.changeStatus(id, 0);

        return CommonRes.create(sellerModel);

    }

}
