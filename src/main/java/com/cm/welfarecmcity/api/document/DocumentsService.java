package com.cm.welfarecmcity.api.document;

import com.cm.welfarecmcity.dto.DocumentDto;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentsService {

    @Autowired
    private DocumentsRepository documentRepository;

    public long saveDocument(String name, MultipartFile pdfFile) throws IOException {
        val document = new DocumentDto();
        document.setName(name);
        document.setPdfFile(pdfFile.getBytes());

        return documentRepository.save(document).getId();
    }
}

