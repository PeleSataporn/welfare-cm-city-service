package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.api.fileresource.model.AddImageReq;
import com.cm.welfarecmcity.constant.SizeImageEnum;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.image.ImageUtils;

import java.io.IOException;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/file-resource")
public class FileResourceController {

  @Autowired
  private FileResourceService service;

  // display image
  @GetMapping("/display/{id}")
  public ResponseEntity<byte[]> displayImage(@PathVariable Long id) throws SQLException {
    val image = service.viewImageById(id, "PROFILE");
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
  }

  @GetMapping("/display/address/{id}")
  public ResponseEntity<byte[]> displayImageAddress(@PathVariable Long id) throws SQLException {
    val image = service.viewImageById(id, "ADDRESS");
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
  }

  @GetMapping("/display/id-card/{id}")
  public ResponseEntity<byte[]> displayImageIdCard(@PathVariable Long id) throws SQLException {
    val image = service.viewImageById(id, "ID-CARD");
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
  }

  @GetMapping("/display/news/{id}")
  public ResponseEntity<byte[]> displayImageNews(@PathVariable Long id) throws SQLException {
    val image = service.viewImageById(id, "NEWS");
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
  }

  // add
  @PostMapping("/add")
  public void addImagePost(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.M.getWidth());
    val blob = new SerialBlob(resizedImage);
    service.create(blob, req.getEmpId());
  }

  @PostMapping("/add-address")
  public String addImageAddress(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.L.getWidth());
    val blob = new SerialBlob(resizedImage);
    return service.createAddress(blob, req.getEmpId());
  }

  @PostMapping("/add-id-card")
  public String addImageIdCard(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.L.getWidth());
    val blob = new SerialBlob(resizedImage);
    return service.createIdCard(blob, req.getEmpId());
  }

  @PostMapping("/add-news")
  public ResponseModel<ResponseId> addImageNews(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.XL.getWidth());
    val blob = new SerialBlob(resizedImage);
    return service.addImageNews(blob, req.getEmpId());
  }

  @PostMapping("/add-news-detail")
  public void addImageNewsDetail(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.XL.getWidth());
    val blob = new SerialBlob(resizedImage);
    service.addImageNewsDetail(blob, req.getEmpId());
  }

  @PostMapping("/update-news")
  public void updateImageNews(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.XL.getWidth());
    val blob = new SerialBlob(resizedImage);
    service.updateImageNews(blob, req.getEmpId());
  }

  @DeleteMapping("/{id}")
  public void deleted(@PathVariable Long id) throws IOException, SQLException {
    service.deleted(id);
  }

  @DeleteMapping("/detail/{id}")
  public void deletedNewsFile(@PathVariable Long id) {
    service.deletedNewsFile(id);
  }
}
