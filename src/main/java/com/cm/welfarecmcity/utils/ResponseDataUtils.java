package com.cm.welfarecmcity.utils;

import com.cm.welfarecmcity.constant.MessageResponseConstant;
import com.cm.welfarecmcity.dto.base.ResponseData;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ResponseDataUtils {

  @Transactional
  public ResponseModel<ResponseId> insertDataSuccess(Long id) {
    ResponseModel<ResponseId> result = new ResponseModel<>();
    ResponseId resId = new ResponseId();

    resId.setId(id);
    result.setData(resId);
    result.setMessage(MessageResponseConstant.INSERT_DATA_SUCCESS);

    return result;
  }

  @Transactional
  public ResponseModel<ResponseId> updateDataSuccess(Long id) {
    ResponseModel<ResponseId> result = new ResponseModel<>();
    ResponseId resId = new ResponseId();

    resId.setId(id);
    result.setData(resId);
    result.setMessage(MessageResponseConstant.UPDATE_DATA_SUCCESS);

    return result;
  }

  @Transactional
  public ResponseModel<String> updateDataSuccessString(String data) {
    ResponseModel<String> result = new ResponseModel<>();
    result.setData(data);
    result.setMessage(MessageResponseConstant.UPDATE_DATA_SUCCESS);

    return result;
  }

  @Transactional
  public ResponseModel<ResponseId> deleteDataSuccess(Long id) {
    ResponseModel<ResponseId> result = new ResponseModel<>();
    ResponseId resId = new ResponseId();

    resId.setId(id);
    result.setData(resId);
    result.setMessage(MessageResponseConstant.DELETE_DATA_SUCCESS);

    return result;
  }

  @Transactional
  public ResponseModel<ResponseData> DataResourceJson(String statusEmp, Long id) {
    ResponseModel<ResponseData> result = new ResponseModel<>();
    ResponseData data = new ResponseData();

    data.setStatusEmployee(statusEmp);
    data.setId(id);
    result.setData(data);
    result.setMessage(MessageResponseConstant.RESULT_SUCCESS);

    return result;
  }

  @Transactional
  public ResponseModel<ResponseData> test1(Long id) {
    ResponseModel<ResponseData> result = new ResponseModel<>();
    ResponseData data = new ResponseData();

    data.setId(id);
    result.setData(data);
    result.setMessage(MessageResponseConstant.RESULT_SUCCESS);

    return result;
  }
}
