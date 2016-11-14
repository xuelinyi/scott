package com.comverse.timesheet.web.activiti.activiti.form;

import java.util.Arrays;

import org.activiti.engine.form.AbstractFormType;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class UsersFormType  extends AbstractFormType {

  public String getName() {
    return "users";
  }

  @Override
  public Object convertFormValueToModelValue(String propertyValue) {
    String[] split = StringUtils.split(propertyValue, ",");
    return Arrays.asList(split);
  }

  @Override
  public String convertModelValueToFormValue(Object modelValue) {
    return ObjectUtils.toString(modelValue);
  }


}
