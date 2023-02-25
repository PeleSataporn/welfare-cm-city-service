package com.cm.welfarecmcity.utils.listener;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateListener {

  @Autowired
  private GenerateListenerRepository generateListenerRepository;

  //  private static final String CUSTOMER_PREFIX = "CUS";
  private static final int NUMBER_MAX_DIGIT = 5;

  public String generateCustomerCode() {
    String defaultCode = "00001";
    val listCode = generateListenerRepository.getRunningNumber();
    if (listCode.size() == 0) {
      return defaultCode;
    }
    return generateCustomerNumber(listCode.get(listCode.size() - 1).getEmployeeCode());
  }

  private static String padNumber(String str, int maxDigit) {
    return str.length() < maxDigit ? padNumber("0" + str, maxDigit) : str;
  }

  public static String generateCustomerNumber(String lastNumber) {
    val number = Long.parseLong(String.valueOf(lastNumber)) + 1;
    return padNumber(Long.toString(number), NUMBER_MAX_DIGIT);
  }
}
