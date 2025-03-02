package com.cm.welfarecmcity.api.document;

import com.cm.welfarecmcity.api.document.model.DocumentRes;
import com.cm.welfarecmcity.dto.bean.FileConfigBean;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.cm.welfarecmcity.logic.document.model.CalculateInstallments;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentsController {

  @Autowired private DocumentsService documentService;

  @PostMapping("/upload-file")
  public ResponseEntity<Long> uploadDocument(
      @RequestParam("name") String name, @RequestParam("file") MultipartFile file)
      throws IOException {
    val res = documentService.saveDocument(name, file);
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

  @GetMapping("/{id}")
  public ResponseEntity<byte[]> getDocument(@PathVariable Long id) {
    val document = documentService.getDocument(id);
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + document.getId() + ".pdf\"")
        .contentType(MediaType.APPLICATION_PDF)
        .body(document.getPdfFile());
  }

  @PostMapping("/search")
  public List<DocumentRes> search() {
    return documentService.search();
  }

  @DeleteMapping("/{id}")
  public void deleted(@PathVariable Long id) throws IOException {
    documentService.deleted(id);
  }

  @PostMapping("/get-file")
  public ResponseEntity<byte[]> getFile(@RequestBody FileConfigBean req) throws IOException {
    val res = documentService.getFile(req);
    String filename = res.get(0).getFileName();
    // Encode the filename to handle non-ASCII characters
    String encodedFilename =
        URLEncoder.encode(filename, StandardCharsets.UTF_8).replaceAll("\\+", "%20");

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
        .contentType(MediaType.APPLICATION_PDF)
        .body(res.get(0).getFileData());
  }

  @PostMapping("/add-file")
  public ResponseEntity<Map<String, String>> addFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam("month") String month,
      @RequestParam("year") String year)
      throws IOException {
    val res = documentService.addFile2(file, month, year);
    return ResponseEntity.status(HttpStatus.OK).body(res);
  }

  @PostMapping("/readFileExcelForLoan")
  public List<List<String>> readFileExcelForLoan(){
    return documentService.readFileExcelForLoan();
  }
}
