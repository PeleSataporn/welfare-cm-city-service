package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.logic.document.model.DocumentV1Res;
import com.cm.welfarecmcity.logic.document.model.DocumentV2Res;
import com.cm.welfarecmcity.logic.document.model.GrandTotalRes;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  public List<DocumentV1Res> searchDocumentV1(Long stockId) {
    return documentRepository.documentInfoV1(stockId);
  }

  public List<DocumentV2Res> searchDocumentV2(Long stockId) {
    return documentRepository.documentInfoV2(stockId);
  }

  public GrandTotalRes grandTotal() {
    val res = documentRepository.grandTotal();

    val sumTotal = (res.getSumStockValue() + res.getSumLoanInterest() + res.getSumLoanOrdinary());
    res.setSumTotal(sumTotal);

    return res;
  }
}