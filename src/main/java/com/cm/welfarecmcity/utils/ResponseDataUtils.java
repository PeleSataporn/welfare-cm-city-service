package com.cm.welfarecmcity.utils;

import com.cm.welfarecmcity.constant.MessageResponseConstant;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public class ResponseDataUtils {

    public ResponseModel<ResponseId> insertDataSuccess(Long id){
        ResponseModel<ResponseId> result = new ResponseModel<>();
        ResponseId resId = new ResponseId();

        resId.setId(id);
        result.setData(resId);
        result.setMessage(MessageResponseConstant.INSERT_DATA_SUCCESS);

        return result;
    }

    public ResponseModel<ResponseId> updateDataSuccess(Long id){
        ResponseModel<ResponseId> result = new ResponseModel<>();
        ResponseId resId = new ResponseId();

        resId.setId(id);
        result.setData(resId);
        result.setMessage(MessageResponseConstant.UPDATE_DATA_SUCCESS);

        return  result;
    }

    public ResponseModel<ResponseId> deleteDataSuccess(Long id){
        ResponseModel<ResponseId> result = new ResponseModel<>();
        ResponseId resId = new ResponseId();

        resId.setId(id);
        result.setData(resId);
        result.setMessage(MessageResponseConstant.DELETE_DATA_SUCCESS);

        return  result;
    }
}
