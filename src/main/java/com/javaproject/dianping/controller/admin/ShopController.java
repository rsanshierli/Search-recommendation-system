package com.javaproject.dianping.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.javaproject.dianping.common.AdminPermission;
import com.javaproject.dianping.common.BusinessException;
import com.javaproject.dianping.common.CommonUtil;
import com.javaproject.dianping.common.EmBusinessError;
import com.javaproject.dianping.model.CategoryModel;
import com.javaproject.dianping.model.ShopModel;
import com.javaproject.dianping.request.CategoryCreateReq;
import com.javaproject.dianping.request.PageQuery;
import com.javaproject.dianping.request.ShopCreateReq;
import com.javaproject.dianping.service.CategoryService;
import com.javaproject.dianping.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller("/admin/shop")
@RequestMapping("/admin/shop")
public class ShopController {


    @Autowired
    private ShopService shopService;

    // 门店列表
    @RequestMapping("/index")
    @AdminPermission  // 权限管理，只有登入后的admin才有权限
    public ModelAndView index(PageQuery pageQuery) { // 由于pagequery类中设有初始值，无需进行输入校验

        PageHelper.startPage(pageQuery.getPage(), pageQuery.getSize()); // 向当前线程的ThreadLocal内设置一下需要从第几页开始，查多少数据
        // 当mybatis向下执行sql语句时，就会自动将 limit 约束设置到sql上
        // 实现不改动sql语句的情况下，实现分页查询

        List<ShopModel> shopModelList = shopService.selectAll();

        PageInfo<ShopModel> shopModelPageInfo = new PageInfo<>(shopModelList);

        ModelAndView modelAndView = new ModelAndView("/admin/shop/index.html");
        modelAndView.addObject("data",shopModelPageInfo);
        modelAndView.addObject("CONTROLLER_NAME", "shop");
        modelAndView.addObject("ACTION_NAME", "index");

        return modelAndView;
    }

    // 只返回 品类创建 的页面
    @RequestMapping("/createpage")
    @AdminPermission
    public ModelAndView createPage() {

        ModelAndView modelAndView = new ModelAndView("/admin/shop/create.html");
        modelAndView.addObject("CONTROLLER_NAME", "shop");
        modelAndView.addObject("ACTION_NAME", "create");
        return modelAndView;
    }

//    // 真正的 品类创建 操作
//    @RequestMapping(value = "/create", method = RequestMethod.POST)
//    @AdminPermission
//    public String create(@Valid ShopCreateReq shopCreateReq, BindingResult bindingResult) throws BusinessException {
//        // 如果校验结果存在，抛出异常
//        if (bindingResult.hasErrors()) {
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
//        }
//
//        ShopModel shopModel = new ShopModel();
//        shopModel.setCategoryId(shopCreateReq.getCategoryId());
//        shopModel.setStartTime(shopCreateReq.getStartTime());
//        shopModel.setEndTime(shopCreateReq.getEndTime());
//        shopModel.setAddress(shopCreateReq.getAddress());
//        shopModel.setLatitude(shopCreateReq.getLatitude());
//        shopModel.setLongitude(shopCreateReq.getLongitude());
//        shopModel.setIconUrl(shopCreateReq.getIconUrl());
//        shopModel.setName(shopCreateReq.getName());
//        shopModel.setPricePerMan(shopCreateReq.getPricePerMan());
//        shopModel.setSellerId(shopCreateReq.getSellerId());
//
//        shopService.create(shopModel);
//
//        return "redirect:/admin/shop/index";
//    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @AdminPermission
    public String create(@Valid ShopCreateReq shopCreateReq, BindingResult bindingResult) throws BusinessException {
        if(bindingResult.hasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, CommonUtil.processErrorString(bindingResult));
        }
        ShopModel shopModel = new ShopModel();
        shopModel.setIconUrl(shopCreateReq.getIconUrl());
        shopModel.setAddress(shopCreateReq.getAddress());
        shopModel.setCategoryId(shopCreateReq.getCategoryId());
        shopModel.setEndTime(shopCreateReq.getEndTime());
        shopModel.setStartTime(shopCreateReq.getStartTime());
        shopModel.setLongitude(shopCreateReq.getLongitude());
        shopModel.setLatitude(shopCreateReq.getLatitude());
        shopModel.setName(shopCreateReq.getName());
        shopModel.setPricePerMan(shopCreateReq.getPricePerMan());
        shopModel.setSellerId(shopCreateReq.getSellerId());

        shopService.create(shopModel);

        return "redirect:/admin/shop/index";
    }

}
