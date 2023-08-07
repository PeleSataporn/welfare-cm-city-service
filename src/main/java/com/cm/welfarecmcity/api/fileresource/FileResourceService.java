package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.dto.FileResourceDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import java.sql.Blob;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FileResourceService {

  @Autowired
  private FileResourceRepository repository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ResponseDataUtils responseDataUtils;

  @Transactional
  public void create(Blob blob, Long empId) {
    //

    val emp = employeeRepository.findById(empId).get();

    val file = emp.getProfileImg();
    if (file != null) {
      val img = repository.findById(file.getId()).get();
      img.setImage(blob);
    } else {
      FileResourceDto resource = new FileResourceDto();
      resource.setImage(blob);

      val fileResource = repository.save(resource);
      emp.setProfileImg(fileResource);
    }

    employeeRepository.save(emp);
  }

  @Transactional
  public void createAddress(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();
    emp.getProfileImg().setImageAddress(blob);

    employeeRepository.save(emp);
  }

  @Transactional
  public void createIdCard(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();
    emp.getProfileImg().setImageIdCard(blob);

    employeeRepository.save(emp);
  }

  @Transactional
  public ResponseModel<ResponseId> addImageNews(Blob blob) {
    FileResourceDto resource = new FileResourceDto();
    resource.setImage(blob);

    return responseDataUtils.updateDataSuccess(repository.save(resource).getId());
  }

  public List<FileResourceDto> viewAll() {
    return repository.findAll();
  }

  public FileResourceDto viewById(long id) {
    return repository.findById(id).get();
  }
}
