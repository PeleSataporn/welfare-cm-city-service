package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.dto.FileResourceDto;
import com.cm.welfarecmcity.dto.base.ResponseId;
import com.cm.welfarecmcity.dto.base.ResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
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
  public void addImagePost(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    service.create(blob, image.getEmpId());
  }

  @PostMapping("/add-address")
  public String addImageAddress(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    return service.createAddress(blob, image.getEmpId());
  }

  @PostMapping("/add-id-card")
  public String addImageIdCard(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    return service.createIdCard(blob, image.getEmpId());
  }

  @PostMapping("/add-news")
  public ResponseModel<ResponseId> addImageNews(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    return service.addImageNews(blob);
  }

  @PostMapping("/update-news")
  public void updateImageNews(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
    service.updateImageNews(blob, image.getEmpId());
  }
}
