package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.dto.FileResourceDto;
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

  @Transactional
  public void create(FileResourceDto image, Long empId) {
    val fileResource = repository.save(image);

    val emp = employeeRepository.findById(empId).get();

    val file = emp.getProfileImg();
    if (file != null) {
      repository.delete(file);
    }

    emp.setProfileImg(fileResource);
    employeeRepository.save(emp);
  }

  public List<FileResourceDto> viewAll() {
    return repository.findAll();
  }

  public FileResourceDto viewById(long id) {
    return repository.findById(id).get();
  }
}
