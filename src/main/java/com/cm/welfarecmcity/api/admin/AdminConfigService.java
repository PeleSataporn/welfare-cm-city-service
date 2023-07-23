package com.cm.welfarecmcity.api.admin;

import com.cm.welfarecmcity.api.admin.model.AdminConfigReq;
import com.cm.welfarecmcity.api.admin.model.AdminConfigRes;
import com.cm.welfarecmcity.dto.AdminConfigDto;
import com.cm.welfarecmcity.dto.FileResourceDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.exception.entity.EmployeeException;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.List;

@Service
public class AdminConfigService {

    @Autowired
    private AdminConfigRepositoryLogic adminConfigRepositoryLogic;

    @Autowired
    private AdminConfigRepository adminConfigRepository;

    @Autowired
    private ResponseDataUtils responseDataUtils;

    @Transactional
    public List<AdminConfigRes> getConfigByList() {
        try {
            return adminConfigRepositoryLogic.documentInfoConfigList();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public ResponseModel<ResponseId> editConfig(AdminConfigReq req) {
        try {
            val findConfig = adminConfigRepository.findById(req.getConfigId());
            if (findConfig.isEmpty()) {
                throw new EmployeeException("Config Admin not found");
            }
            val config = findConfig.get();
            config.setValue(req.getValue());
            return responseDataUtils.insertDataSuccess(req.getConfigId());
        } catch (Exception e) {
            return null;
        }
    }

    public AdminConfigDto configById(long id) {
        return adminConfigRepository.findById(id).get();
    }

    @Transactional
    public void addConfigImg(AdminConfigDto image, Long id) {
        val config = adminConfigRepository.findById(id).get();
        config.setImage(image.getImage());
        adminConfigRepository.save(config);
    }

}
