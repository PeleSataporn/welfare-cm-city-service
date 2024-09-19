package com.cm.welfarecmcity.api.document;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}
