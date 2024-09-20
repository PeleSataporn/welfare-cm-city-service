package com.cm.welfarecmcity.api.document;

import com.cm.welfarecmcity.api.document.model.DocumentRes;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentsController {

    @Autowired
    private DocumentsService documentService;

    @PostMapping("/upload-file")
    public ResponseEntity<Long> uploadDocument(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) throws IOException {
            val res = documentService.saveDocument(name, file);
            return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getDocument(@PathVariable Long id) {
        val document = documentService.getDocument(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getId() + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(document.getPdfFile());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<byte[]> getDocumentV2(@PathVariable Long id) {
//        val document = documentService.getDocument(id);
//
//        val headers = new HttpHeaders();
//        headers.add(
//                HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=doc-\"" + document.getId() + ".pdf\"");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(document.getPdfFile());
//    }

    @PostMapping("/search")
    public List<DocumentRes> search() {
        return documentService.search();
    }

    @DeleteMapping("/{id}")
    public void deleted(@PathVariable Long id) throws IOException {
        documentService.deleted(id);
    }
}
