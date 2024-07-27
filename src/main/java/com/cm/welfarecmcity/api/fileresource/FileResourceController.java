package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.constant.SizeImageEnum;
import com.cm.welfarecmcity.dto.FileResourceDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import com.cm.welfarecmcity.utils.image.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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
    return service.addImageNews(blob);
  }

  @PostMapping("/add-news-detail")
  public ResponseModel<ResponseId> addImageNewsDetail(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.XL.getWidth());
    val blob = new SerialBlob(resizedImage);
    return service.addImageNewsDetail(blob);
  }

  @PostMapping("/update-news")
  public void updateImageNews(@ModelAttribute AddImageReq req) throws IOException, SQLException {
    val resizedImage = ImageUtils.resizeImage(req.getImage(), SizeImageEnum.XL.getWidth());
    val blob = new SerialBlob(resizedImage);
    service.updateImageNews(blob, req.getEmpId());
  }
}
