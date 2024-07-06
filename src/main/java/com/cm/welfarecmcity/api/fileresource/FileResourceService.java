package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.news.NewsRepository;
import com.cm.welfarecmcity.dto.FileResourceDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.ResponseDataUtils;
import java.sql.Blob;
import java.sql.SQLException;
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

  @Autowired
  private NewsRepository newRepository;

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
  public String createAddress(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();

    if (emp.getProfileImg() == null) {
      return "ProfileNull";
    }

    emp.getProfileImg().setImageAddress(blob);
    employeeRepository.save(emp);

    return null;
  }

  @Transactional
  public String createIdCard(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();

    if (emp.getProfileImg() == null) {
      return "ProfileNull";
    }

    emp.getProfileImg().setImageIdCard(blob);

    employeeRepository.save(emp);

    return null;
  }

  @Transactional
  public ResponseModel<ResponseId> addImageNews(Blob blob) {
    FileResourceDto resource = new FileResourceDto();
    resource.setImage(blob);

    return responseDataUtils.updateDataSuccess(repository.save(resource).getId());
  }

  @Transactional
  public void updateImageNews(Blob blob, Long newsId) {
    val news = newRepository.findById(newsId).get();
    news.getCoverImg().setImage(blob);
    newRepository.save(news);
  }

  public List<FileResourceDto> viewAll() {
    return repository.findAll();
  }

  public byte[] viewImageById(Long id, String type) throws SQLException {
    val findFile = repository.findById(id);

    if (findFile.isEmpty()) {
      return null;
    }

    val image = findFile.get();

    return switch (type) {
      case "PROFILE", "NEWS" -> getImageBytes(image.getImage());
      case "ADDRESS" -> getImageBytes(image.getImageAddress());
      case "ID-CARD" -> getImageBytes(image.getImageIdCard());
      default -> null;
    };
  }

  private byte[] getImageBytes(Blob imageBlob) throws SQLException {
    if (imageBlob == null) {
      return null;
    }
    return imageBlob.getBytes(1, (int) imageBlob.length());
  }

}
