package com.javaproject.dianping.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class CommonUtil {
    /**
     * 校验，传入bindingResult，如果为空未返回空，如果不为空，则取每一个校验结果进行拼接，然后输出字符串
     * @param bindingResult
     * @return 返回空或者不满足校验的所有结果
     */
    public static String processErrorString(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError:bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage() + ",");
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
