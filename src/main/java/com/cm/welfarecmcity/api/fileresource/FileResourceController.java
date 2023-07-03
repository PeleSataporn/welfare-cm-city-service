package com.cm.welfarecmcity.api.fileresource;

import com.cm.welfarecmcity.dto.FileResourceDto;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
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
  public ResponseEntity<byte[]> displayImage(@PathVariable Long id) throws IOException, SQLException {
    FileResourceDto image = service.viewById(id);
    byte[] imageBytes = null;
    imageBytes = image.getImage().getBytes(1, (int) image.getImage().length());
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
  }

  @PostMapping("/add")
  public void addImagePost(@ModelAttribute AddImageReq image) throws IOException, SQLException {
    byte[] bytes = image.getImage().getBytes();
    Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

    FileResourceDto fileResource = new FileResourceDto();
    fileResource.setImage(blob);

    service.create(fileResource, image.getEmpId());
  }
}
