package com.cm.welfarecmcity.api.document;

import com.cm.welfarecmcity.api.document.model.DocumentRes;
import com.cm.welfarecmcity.dto.DocumentDto;
import com.cm.welfarecmcity.exception.entity.DocumentException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public DocumentDto getDocument(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentException("Document not found"));
    }

    public List<DocumentRes> search() {
        return documentRepository.findAll().stream()
                .map(
                        doc ->
                                DocumentRes.builder()
                                        .id(doc.getId())
                                        .name(doc.getName())
                                        .createDate(doc.getCreateDate())
                                        .build())
                .collect(Collectors.toList());
    }
}

