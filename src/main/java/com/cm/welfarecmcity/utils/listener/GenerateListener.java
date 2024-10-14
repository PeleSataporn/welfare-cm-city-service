package com.cm.welfarecmcity.utils.listener;

import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateListener {

  @Autowired private GenerateListenerRepository generateListenerRepository;

  private static final int NUMBER_MAX_DIGIT = 5;

  @Transactional
  public String generateCustomerCode() {
    String defaultCode = "00001";
    val listCode = generateListenerRepository.getRunningNumber();
    if (listCode.size() == 0) {
      return defaultCode;
    }
    return generateCustomerNumber(listCode.get(listCode.size() - 1).getEmployeeCode());
  }

  @Transactional
  private static String padNumber(String str, int maxDigit) {
    return str.length() < maxDigit ? padNumber("0" + str, maxDigit) : str;
  }

  @Transactional
  public static String generateCustomerNumber(String lastNumber) {
    val number = Long.parseLong(String.valueOf(lastNumber)) + 1;
    return padNumber(Long.toString(number), NUMBER_MAX_DIGIT);
  }
}
