package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.api.employee.EmployeeRepository;
import com.cm.welfarecmcity.api.news.NewsRepository;
import com.cm.welfarecmcity.api.newsfiledetail.NewsFileDetailRepository;
import com.cm.welfarecmcity.dto.FileResourceDto;
import com.cm.welfarecmcity.dto.NewsFileDetailDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.dto.embeddable.NewsFileDetailKey;
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

  @Autowired
  private NewsFileDetailRepository newsFileDetailRepository;

  @Transactional
  public void create(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();
    val file = emp.getProfileImg();

    if (file != null) {
      val img = repository.findById(file.getId()).get();
      img.setImage(blob);
    } else {
      val resource = new FileResourceDto();
      resource.setImage(blob);

      val fileResource = repository.save(resource);
      emp.setProfileImg(fileResource);
    }

    employeeRepository.save(emp);
  }

  @Transactional
  public String createAddress(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();
    val file = emp.getProfileImg();

    if (file == null) {
      return "ProfileNull";
    }

    val img = repository.findById(file.getId()).get();
    img.setImageAddress(blob);

    repository.save(img);

//    if (img.getImageAddress() != null) {
//      img.setImageAddress(blob);
//    } else {
////      val resource = new FileResourceDto();
////      resource.setImage(blob);
//
////      val fileResource = repository.save(resource);
//      emp.setProfileImg(fileResource);
//    }
//    emp.getProfileImg().setImageAddress(blob);
//    employeeRepository.save(emp);

    return null;
  }

  @Transactional
  public String createIdCard(Blob blob, Long empId) {
    val emp = employeeRepository.findById(empId).get();
    val file = emp.getProfileImg();

    if (file == null) {
      return "ProfileNull";
    }

    val img = repository.findById(file.getId()).get();
    img.setImageIdCard(blob);

    repository.save(img);

    return null;
  }

  @Transactional
  public ResponseModel<ResponseId> addImageNews(Blob blob, Long newsId) {
    val resource = new FileResourceDto();
    resource.setImage(blob);
    val file = repository.save(resource);

    val news = newRepository.findById(newsId).get();
    news.setCoverImg(file);
    newRepository.save(news);

    return responseDataUtils.updateDataSuccess(file.getId());
  }

//  @Transactional
//  public void addImageNewsDetail(Blob blob, Long newsId) {
//    val resource = new FileResourceDto();
//    resource.setImage(blob);
//    val file = repository.save(resource);
//
//    val news = newRepository.findById(newsId).get();
//
//    val fileDetail = new NewsFileDetailDto();
//    fileDetail.setFileResource(file);
//    fileDetail.setNews(news);
//
//    newsFileDetailRepository.save(fileDetail);
//  }

  @Transactional
  public void addImageNewsDetail(Blob blob, Long newsId) {
    val resource = new FileResourceDto();
    resource.setImage(blob);
    val file = repository.save(resource);

    val news = newRepository.findById(newsId).get();

    val fileDetail = new NewsFileDetailDto();
    fileDetail.setNews(news);
    fileDetail.setFileResource(file);

    val id = new NewsFileDetailKey();
    id.setNews_id(newsId);
    id.setFile_resource_id(file.getId());
    fileDetail.setId(id);

    newsFileDetailRepository.save(fileDetail);
  }

  @Transactional
  public void deleted(Long id) {
    val file = repository.findById(id).get();
    repository.delete(file);
  }

  @Transactional
  public void updateImageNews(Blob blob, Long newsId) {
    val news = newRepository.findById(newsId).get();
    if (news.getCoverImg() != null) {
      news.getCoverImg().setImage(blob);
    } else {
      val file = new FileResourceDto();
      file.setImage(blob);
      val fileResource = repository.save(file);

      news.setCoverImg(fileResource);
    }

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

  public byte[] getImageBytes(Blob imageBlob) throws SQLException {
    if (imageBlob == null) {
      return null;
    }
    return imageBlob.getBytes(1, (int) imageBlob.length());
  }
}
