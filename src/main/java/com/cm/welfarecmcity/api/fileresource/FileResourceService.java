package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.dto.FileResourceDto;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileResourceService {

  @Autowired
  private FileResourceRepository repository;

  @Autowired
  private EmployeeRepository employeeRepository;

  public void create(FileResourceDto image, Long empId) {
    val fileResource = repository.save(image);

    val emp = employeeRepository.findById(empId).get();

    if (emp.getProfileImg() != null) {
      val file = emp.getProfileImg();

      emp.setProfileImg(fileResource);
      employeeRepository.save(emp);

      repository.delete(file);
    }
  }

  public List<FileResourceDto> viewAll() {
    return repository.findAll();
  }

  public FileResourceDto viewById(long id) {
    return repository.findById(id).get();
  }
}
