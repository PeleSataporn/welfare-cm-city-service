package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.logic.document.model.*;

import java.util.List;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

  @Autowired
  private DocumentRepository documentRepository;

  @Transactional
  public List<DocumentV1Res> searchDocumentV1(Long stockId) {
    return documentRepository.documentInfoV1(stockId);
  }

  @Transactional
  public List<DocumentV2Res> searchDocumentV2(Long stockId) {
    return documentRepository.documentInfoV2(stockId);
  }

  @Transactional
  public GrandTotalRes grandTotal() {
    val res = documentRepository.grandTotal();

    val sumTotal = (res.getSumStockValue() + res.getSumLoanInterest() + res.getSumLoanOrdinary());
    res.setSumTotal(sumTotal);

    return res;
  }

  @Transactional
  public List<DocumentInfoAllRes> documentInfoAll() {
    return documentRepository.documentInfoAll();
  }


  // loan
  @Transactional
  public List<DocumentV1ResLoan> searchDocumentV1Loan(Long loanId) {
    return documentRepository.documentInfoV1Loan(loanId);
  }

  @Transactional
  public List<DocumentV2ResLoan> searchDocumentV2Loan(Long loanId) {
    return documentRepository.documentInfoV2Loan(loanId);
  }

}
