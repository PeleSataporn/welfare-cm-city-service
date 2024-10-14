package com.cm.welfarecmcity.api.admin;

import com.cm.welfarecmcity.api.admin.model.AdminConfigReq;
import com.cm.welfarecmcity.api.admin.model.AdminConfigRes;
import com.cm.welfarecmcity.api.admin.model.AdminUpdateInfoReq;
import com.cm.welfarecmcity.api.admin.model.employeeListRes;
import com.cm.welfarecmcity.api.fileresource.model.AddImageReq;
import com.cm.welfarecmcity.dto.AdminConfigDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminConfigController {

  @Autowired private AdminConfigService service;

  @PostMapping("logic/document/getConfigByList")
  public List<AdminConfigRes> getConfigByList() {
    return service.getConfigByList();
  }

  @PostMapping("logic/document/getEmployeeByList")
  public List<employeeListRes> getEmployeeByList(@RequestBody AdminConfigReq req) {
    return service.getEmployeeByList(req);
  }

  @PostMapping("logic/document/updateRoleEmp")
  public Boolean updateRoleEmp(@RequestBody AdminConfigReq req) {
    return service.updateRoleEmp(req);
  }

  @PostMapping("logic/document/updateConfig")
  public ResponseModel<ResponseId> editConfig(@RequestBody AdminConfigReq req) {
    return service.editConfig(req);
  }

  @GetMapping("logic/document/getImageConfig/{id}")
  public ResponseEntity<byte[]> displayImage(@PathVariable Long id)
      throws IOException, SQLException {
    AdminConfigDto image = service.configById(id);
    byte[] imageBytes = null;
    imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
  }

  @PostMapping("logic/document/getImageConfig/add")
  public void addImagePost(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    AdminConfigDto fileResource = new AdminConfigDto();
    fileResource.setImage(blob);

    service.addConfigImg(fileResource, image.getEmpId());
  }

  @PutMapping("logic/update-info")
  public void updateInfo(@RequestBody AdminUpdateInfoReq req) {
    service.updateInfo(req);
  }
}
