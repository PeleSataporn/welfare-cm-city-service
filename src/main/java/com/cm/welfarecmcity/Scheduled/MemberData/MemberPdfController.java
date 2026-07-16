package com.cm.welfarecmcity.Scheduled.MemberData;

import com.cm.welfarecmcity.logic.document.DocumentService;
import com.cm.welfarecmcity.logic.document.model.DocumentInfoAllRes;
import com.cm.welfarecmcity.logic.document.model.DocumentReq;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/document/pdf")
@RequiredArgsConstructor
public class MemberPdfController {

    @Autowired
    private MemberPdfService memberPdfService;
    @Autowired
    private DocumentService documentService;

    @GetMapping("/generatePdfMemberAll")
    public ResponseEntity<byte[]> generatePdfMemberAll() throws Exception {

        DocumentReq req = new DocumentReq();
        req.setMonthCurrent("กรกฎาคม");
        req.setYearCurrent("2569");

        List<DocumentInfoAllRes> list =  documentService.documentInfoAll(req);
        byte[] pdf =  memberPdfService.generate(list);

        return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=memberAll.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

}
