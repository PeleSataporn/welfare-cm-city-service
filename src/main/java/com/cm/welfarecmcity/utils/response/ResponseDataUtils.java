package com.cm.welfarecmcity.utils.response;

import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseIds;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.response.model.MessageResponseEnum;
import java.util.List;

public class ResponseDataUtils {

  private ResponseDataUtils() {}

  public static ResponseModel<ResponseId> insertDataSuccess(Long id) {
    ResponseModel<ResponseId> result = new ResponseModel<>();
    ResponseId resId = new ResponseId();
    resId.setId(id);
    result.setData(resId);
    result.setMessage(MessageResponseEnum.INSERT_DATA_SUCCESS.getText());
    return result;
  }

  public static <T> ResponseModel<T> insertDataSuccess(T data) {
    ResponseModel<T> result = new ResponseModel<>();
    result.setData(data);
    result.setMessage(MessageResponseEnum.INSERT_DATA_SUCCESS.getText());
    return result;
  }

  public static ResponseModel<Void> insertDataSuccess() {
    ResponseModel<Void> result = new ResponseModel<>();
    result.setMessage(MessageResponseEnum.INSERT_DATA_SUCCESS.getText());
    return result;
  }

  public static ResponseModel<Void> operatorDataSuccess() {
    ResponseModel<Void> result = new ResponseModel<>();
    result.setMessage(MessageResponseEnum.OPERATOR_DATA_SUCCESS.getText());
    return result;
  }

  public static <T> ResponseModel<T> operatorDataSuccess(T data) {
    ResponseModel<T> result = new ResponseModel<>();
    result.setMessage(MessageResponseEnum.OPERATOR_DATA_SUCCESS.getText());
    result.setData(data);

    return result;
  }

  public static ResponseModel<ResponseIds> insertMultiDataSuccess(List<Long> ids) {
    ResponseModel<ResponseIds> result = new ResponseModel<>();
    ResponseIds resIds = new ResponseIds(ids);

    result.setData(resIds);
    result.setMessage(MessageResponseEnum.INSERT_DATA_SUCCESS.getText());
    return result;
  }

  public static ResponseModel<Void> updateDataSuccess() {
    ResponseModel<Void> result = new ResponseModel<>();

    result.setMessage(MessageResponseEnum.UPDATE_DATA_SUCCESS.getText());

    return result;
  }

  public static ResponseModel<Void> deleteDataSuccess() {
    ResponseModel<Void> result = new ResponseModel<>();

    result.setMessage(MessageResponseEnum.DELETE_DATA_SUCCESS.getText());

    return result;
  }

  public static ResponseModel<ResponseIds> responseMultiData(List<Long> ids, String message) {
    ResponseModel<ResponseIds> result = new ResponseModel<>();
    ResponseIds resIds = new ResponseIds(ids);

    result.setData(resIds);
    result.setMessage(message);

    return result;
  }

  public static <T> ResponseModel<T> fetchDataSuccess(T data) {
    ResponseModel<T> result = new ResponseModel<>();

    result.setData(data);
    result.setMessage(MessageResponseEnum.FETCH_DATA_SUCCESS.getText());

    return result;
  }

  public static ResponseModel<Void> requestSuccess() {
    ResponseModel<Void> result = new ResponseModel<>();

    result.setMessage(MessageResponseEnum.RESULT_SUCCESS.getText());

    return result;
  }

  public static ResponseModel<Void> requestFailure() {
    ResponseModel<Void> result = new ResponseModel<>();

    result.setResult("FAIL");
    result.setMessage(MessageResponseEnum.RESULT_FAILURE.getText());

    return result;
  }

  public static <T> ResponseModel<T> updateAndFetchDataSuccess(T data) {
    ResponseModel<T> result = new ResponseModel<>();

    result.setData(data);
    result.setMessage(MessageResponseEnum.UPDATE_AND_FETCH_DATA_SUCCESS.getText());

    return result;
  }
}
