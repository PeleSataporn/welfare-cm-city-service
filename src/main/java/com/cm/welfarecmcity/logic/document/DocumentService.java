package com.cm.welfarecmcity.logic.document;

import com.cm.welfarecmcity.api.admin.AdminConfigRepository;
import com.cm.welfarecmcity.api.loan.LoanRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailLogicRepository;
import com.cm.welfarecmcity.api.loandetail.LoanDetailRepository;
import com.cm.welfarecmcity.api.loandetail.model.LoanHistoryV2Res;
import com.cm.welfarecmcity.api.loandetail.model.SumLoanHistoryV2Res;
import com.cm.welfarecmcity.api.loandetailhistory.LoanDetailHistoryRepository;
import com.cm.welfarecmcity.api.stock.StockRepository;
import com.cm.welfarecmcity.api.stock.StockService;
import com.cm.welfarecmcity.api.stockdetail.StockDetailLoginRepository;
import com.cm.welfarecmcity.api.stockdetail.StockDetailRepository;
import com.cm.welfarecmcity.dto.LoanDetailDto;
import com.cm.welfarecmcity.dto.LoanHistoryDto;
import com.cm.welfarecmcity.logic.document.model.*;
import com.cm.welfarecmcity.utils.DateUtils;
import com.cm.welfarecmcity.utils.NumberFormatUtils;
import com.cm.welfarecmcity.utils.ThaiNumeralsUtils;
import com.cm.welfarecmcity.utils.ZipUtil;
import jakarta.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.val;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {

  @Autowired private DocumentRepository documentRepository;

  @Autowired private StockDetailRepository stockDetailRepository;

  @Autowired private StockDetailLoginRepository stockDetailLoginRepository;

  @Autowired private LoanDetailRepository loanDetailRepository;

  @Autowired private LoanDetailLogicRepository loanDetailLogicRepository;

  @Autowired private LoanDetailHistoryRepository loanDetailHistoryRepository;
  @Autowired private LoanRepository loanRepository;
  @Autowired private AdminConfigRepository adminConfigRepository;
  @Autowired private StockService stockService;
  @Autowired private StockRepository stockRepository;

  @Transactional
  public List<DocumentV1Res> searchDocumentV1(Long empId, String monthCurrent, String yearCurrent) {
    val res1 = documentRepository.documentInfoV1stock(empId, monthCurrent, yearCurrent);

    for (int i = 0; i < res1.size(); i++) {
      var res2 = new ArrayList<DocumentLoanV1Res>();
      if (monthCurrent != null) {
        res2 =
            (ArrayList<DocumentLoanV1Res>)
                documentRepository.documentInfoV1loan(
                    res1.get(i).getEmpId(), monthCurrent, yearCurrent);

        if (res2.size() > 0) {
          res1.get(i).setLoanOrdinary(res2.get(0).getLoanOrdinary());
          res1.get(i).setLoanInstallment(res2.get(0).getLoanInstallment());
          res1.get(i).setInterest(res2.get(0).getInterest());
          res1.get(i).setLoanTime(res2.get(0).getLoanTime());
        }
      } else {
        res2 =
            (ArrayList<DocumentLoanV1Res>)
                documentRepository.documentInfoV1loan(empId, null, yearCurrent);

        if (res2.size() > 0) {
          res1.get(i).setLoanOrdinary(res2.get(i).getLoanOrdinary());
          res1.get(i).setLoanInstallment(res2.get(i).getLoanInstallment());
          res1.get(i).setInterest(res2.get(i).getInterest());
          res1.get(i).setLoanTime(res2.get(i).getLoanTime());
        }
      }

      // sum
      if (res1.get(i).getLoanOrdinary() != null && res1.get(i).getInterest() != null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val interest = Integer.parseInt(res1.get(i).getInterest());
        int sum = 0;
        if (res1.get(i).getLoanTime().equals(res1.get(i).getLoanInstallment())) {
          sum = stockValue + (loanOrdinary + interest);
        } else {
          sum = stockValue + (loanOrdinary - interest) + interest;
        }
        res1.get(i).setSumMonth(String.valueOf(sum));
      } else if (res1.get(i).getLoanOrdinary() != null && res1.get(i).getInterest() == null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val sum = stockValue + loanOrdinary;

        res1.get(i).setSumMonth(String.valueOf(sum));
      } else if (res1.get(i).getLoanOrdinary() == null && res1.get(i).getInterest() != null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val sum = stockValue + loanOrdinary;

        res1.get(i).setSumMonth(String.valueOf(sum));
      } else {
        res1.get(i).setSumMonth(res1.get(i).getStockValue());
      }
    }

    return res1;
  }

  @Transactional
  public List<DocumentV2Res> searchDocumentV2(Long empId, String monthCurrent, String yearCurrent) {
    val documentV1 = searchDocumentV1(empId, monthCurrent, yearCurrent);

    if (monthCurrent != null) {
      val list = new ArrayList<DocumentV2Res>();

      for (DocumentV1Res documentV1Res : documentV1) {
        var loanDetailInterestTotal = 0;
        var loanDetailOrdinaryTotal = 0;
        var stockValueTotal = 0;
        var stockAccumulateTotal = 0;
        var totalMonth = 0;
        var loanInterest = 0;
        var loanOrdinary = 0;

        val documentV2 = new DocumentV2Res();
        documentV2.setDepartmentName(documentV1Res.getDepartmentName());

        if (documentV1Res.getInterest() != null) {
          loanInterest = Integer.parseInt(documentV1Res.getInterest());
        }

        if (documentV1Res.getLoanOrdinary() != null) {
          loanOrdinary = Integer.parseInt(documentV1Res.getLoanOrdinary());
        }

        loanDetailInterestTotal += loanInterest;
        loanDetailOrdinaryTotal += loanOrdinary;
        stockValueTotal +=
            Integer.parseInt(
                documentV1Res.getStockValue() != null ? documentV1Res.getStockValue() : "0");
        stockAccumulateTotal +=
            Integer.parseInt(
                documentV1Res.getStockAccumulate() != null
                    ? documentV1Res.getStockAccumulate()
                    : "0");
        totalMonth +=
            Integer.parseInt(
                documentV1Res.getSumMonth() != null ? documentV1Res.getSumMonth() : "0");

        documentV2.setLoanDetailInterestTotal(String.valueOf(loanDetailInterestTotal));
        documentV2.setLoanDetailOrdinaryTotal(String.valueOf(loanDetailOrdinaryTotal));
        documentV2.setStockValueTotal(String.valueOf(stockValueTotal));
        documentV2.setStockAccumulateTotal(String.valueOf(stockAccumulateTotal));
        documentV2.setTotalMonth(String.valueOf(totalMonth));

        list.add(documentV2);
      }

      return list;
    } else {
      val list = new ArrayList<DocumentV2Res>();
      var loanDetailInterestTotal = 0;
      var loanDetailOrdinaryTotal = 0;
      var stockValueTotal = 0;
      var stockAccumulateTotal = 0;
      var totalMonth = 0;

      var loanInterest = 0;
      var loanOrdinary = 0;
      val documentV2 = new DocumentV2Res();

      for (DocumentV1Res documentV1Res : documentV1) {
        documentV2.setDepartmentName(documentV1Res.getDepartmentName());

        if (documentV1Res.getInterest() != null) {
          loanInterest = Integer.parseInt(documentV1Res.getInterest());
        }

        if (documentV1Res.getLoanOrdinary() != null) {
          loanOrdinary = Integer.parseInt(documentV1Res.getLoanOrdinary());
        }

        loanDetailInterestTotal += loanInterest;
        loanDetailOrdinaryTotal += loanOrdinary;
        stockValueTotal += Integer.parseInt(documentV1Res.getStockValue());
        stockAccumulateTotal += Integer.parseInt(documentV1Res.getStockAccumulate());
        totalMonth += Integer.parseInt(documentV1Res.getSumMonth());
      }

      documentV2.setLoanDetailInterestTotal(String.valueOf(loanDetailInterestTotal));
      documentV2.setLoanDetailOrdinaryTotal(String.valueOf(loanDetailOrdinaryTotal));
      documentV2.setStockValueTotal(String.valueOf(stockValueTotal));
      documentV2.setStockAccumulateTotal(String.valueOf(stockAccumulateTotal));
      documentV2.setTotalMonth(String.valueOf(totalMonth));

      list.add(documentV2);

      return list;
    }
  }

  @Transactional
  public List<DocumentV1Res> searchDocumentOldV1(
      Long empId, String monthCurrent, String yearCurrent) {
    val res1 = documentRepository.documentInfoV1stockOldHistory(empId, monthCurrent, yearCurrent);

    for (int i = 0; i < res1.size(); i++) {
      var res2 = new ArrayList<DocumentLoanV1Res>();
      if (monthCurrent != null) {
        res2 =
            (ArrayList<DocumentLoanV1Res>)
                documentRepository.documentInfoV1loanHistory(
                    res1.get(i).getEmpId(), monthCurrent, yearCurrent);

        if (res2.size() > 0) {
          res1.get(i).setLoanOrdinary(res2.get(0).getLoanOrdinary());
          res1.get(i).setLoanInstallment(res2.get(0).getLoanInstallment());
          res1.get(i).setInterest(res2.get(0).getInterest());
          res1.get(i).setLoanTime(res2.get(0).getLoanTime());
        }
      } else {
        res2 =
            (ArrayList<DocumentLoanV1Res>)
                documentRepository.documentInfoV1loanHistory(empId, null, yearCurrent);

        if (res2.size() > 0) {
          res1.get(i).setLoanOrdinary(res2.get(i).getLoanOrdinary());
          res1.get(i).setLoanInstallment(res2.get(i).getLoanInstallment());
          res1.get(i).setInterest(res2.get(i).getInterest());
          res1.get(i).setLoanTime(res2.get(i).getLoanTime());
        }
      }

      // sum
      if (res1.get(i).getLoanOrdinary() != null && res1.get(i).getInterest() != null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val interest = Integer.parseInt(res1.get(i).getInterest());
        int sum = 0;
        if (res1.get(i).getLoanTime().equals(res1.get(i).getLoanInstallment())) {
          sum = stockValue + (loanOrdinary + interest);
        } else {
          sum = stockValue + (loanOrdinary - interest) + interest;
        }
        res1.get(i).setSumMonth(String.valueOf(sum));
      } else if (res1.get(i).getLoanOrdinary() != null && res1.get(i).getInterest() == null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val sum = stockValue + loanOrdinary;

        res1.get(i).setSumMonth(String.valueOf(sum));
      } else if (res1.get(i).getLoanOrdinary() == null && res1.get(i).getInterest() != null) {
        val stockValue = Integer.parseInt(res1.get(i).getStockValue());
        val loanOrdinary = Integer.parseInt(res1.get(i).getLoanOrdinary());
        val sum = stockValue + loanOrdinary;

        res1.get(i).setSumMonth(String.valueOf(sum));
      } else {
        res1.get(i).setSumMonth(res1.get(i).getStockValue());
      }
    }

    return res1;
  }

  @Transactional
  public List<DocumentV2Res> searchDocumentOldV2(
      Long empId, String monthCurrent, String yearCurrent) {
    val documentV1 = searchDocumentOldV1(empId, monthCurrent, yearCurrent);

    if (monthCurrent != null) {
      val list = new ArrayList<DocumentV2Res>();

      for (DocumentV1Res documentV1Res : documentV1) {
        var loanDetailInterestTotal = 0;
        var loanDetailOrdinaryTotal = 0;
        var stockValueTotal = 0;
        var stockAccumulateTotal = 0;
        var totalMonth = 0;
        var loanInterest = 0;
        var loanOrdinary = 0;

        val documentV2 = new DocumentV2Res();
        documentV2.setDepartmentName(documentV1Res.getDepartmentName());

        if (documentV1Res.getInterest() != null) {
          loanInterest = Integer.parseInt(documentV1Res.getInterest());
        }

        if (documentV1Res.getLoanOrdinary() != null) {
          loanOrdinary = Integer.parseInt(documentV1Res.getLoanOrdinary());
        }

        loanDetailInterestTotal += loanInterest;
        loanDetailOrdinaryTotal += loanOrdinary;
        stockValueTotal +=
            Integer.parseInt(
                documentV1Res.getStockValue() != null ? documentV1Res.getStockValue() : "0");
        stockAccumulateTotal +=
            Integer.parseInt(
                documentV1Res.getStockAccumulate() != null
                    ? documentV1Res.getStockAccumulate()
                    : "0");
        totalMonth +=
            Integer.parseInt(
                documentV1Res.getSumMonth() != null ? documentV1Res.getSumMonth() : "0");

        documentV2.setLoanDetailInterestTotal(String.valueOf(loanDetailInterestTotal));
        documentV2.setLoanDetailOrdinaryTotal(String.valueOf(loanDetailOrdinaryTotal));
        documentV2.setStockValueTotal(String.valueOf(stockValueTotal));
        documentV2.setStockAccumulateTotal(String.valueOf(stockAccumulateTotal));
        documentV2.setTotalMonth(String.valueOf(totalMonth));

        list.add(documentV2);
      }

      return list;
    } else {
      val list = new ArrayList<DocumentV2Res>();
      var loanDetailInterestTotal = 0;
      var loanDetailOrdinaryTotal = 0;
      var stockValueTotal = 0;
      var stockAccumulateTotal = 0;
      var totalMonth = 0;

      var loanInterest = 0;
      var loanOrdinary = 0;
      val documentV2 = new DocumentV2Res();

      for (DocumentV1Res documentV1Res : documentV1) {
        documentV2.setDepartmentName(documentV1Res.getDepartmentName());

        if (documentV1Res.getInterest() != null) {
          loanInterest = Integer.parseInt(documentV1Res.getInterest());
        }

        if (documentV1Res.getLoanOrdinary() != null) {
          loanOrdinary = Integer.parseInt(documentV1Res.getLoanOrdinary());
        }

        loanDetailInterestTotal += loanInterest;
        loanDetailOrdinaryTotal += loanOrdinary;
        stockValueTotal += Integer.parseInt(documentV1Res.getStockValue());
        stockAccumulateTotal += Integer.parseInt(documentV1Res.getStockAccumulate());
        totalMonth += Integer.parseInt(documentV1Res.getSumMonth());
      }

      documentV2.setLoanDetailInterestTotal(String.valueOf(loanDetailInterestTotal));
      documentV2.setLoanDetailOrdinaryTotal(String.valueOf(loanDetailOrdinaryTotal));
      documentV2.setStockValueTotal(String.valueOf(stockValueTotal));
      documentV2.setStockAccumulateTotal(String.valueOf(stockAccumulateTotal));
      documentV2.setTotalMonth(String.valueOf(totalMonth));

      list.add(documentV2);

      return list;
    }
  }

  @Transactional
  public EmployeeLoanNew searchEmployeeLoanNew(DocumentReq req) {
    try {
      val empFullData = documentRepository.getEmpFullData(req.getEmpCode());
      req.setStockId(empFullData.getStockId());
      req.setLoanId(empFullData.getLoanId());
      boolean flagLoan = false;
      if (req.getLoanId() != null) {
        val loadDetail = documentRepository.searchEmployeeLoanOfNull(req);
        if (loadDetail.size() > 0) {
          req.setLoanId(empFullData.getLoanId());
        } else {
          flagLoan = true;
          // req.setLoanId(null);
        }
      } else {
        flagLoan = true;
        // req.setLoanId(null);
      }

      if (flagLoan) {
        EmployeeLoanNew employeeLoanNew =
            documentRepository.searchEmployeeLoanOldHistoryOfNull(req);
        employeeLoanNew.setHistoryLoanFlag(true);
        return employeeLoanNew;
      } else {
        return documentRepository.searchEmployeeLoanNew(req); // searchEmployeeLoanNewOfNull
      }

    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public EmployeeLoanNew searchEmployeeLoanOld(DocumentReq req) {
    val empFullData = documentRepository.getEmpFullData(req.getEmpCode());
    req.setStockId(empFullData.getStockId());
    req.setLoanId(empFullData.getLoanId());
    req.setEmpId(empFullData.getEmpId());

    if (req.getLoanId() != null) {
      val loadDetail = documentRepository.searchEmployeeLoanOfNullHistory(req);
      if (loadDetail != null) {
        req.setLoanId(empFullData.getLoanId());
      } else {
        req.setLoanId(null);
      }
    } else {
      req.setLoanId(null);
    }
    // return documentRepository.searchEmployeeLoanOldHistory(req);
    EmployeeLoanNew employeeLoanNew = documentRepository.searchEmployeeLoanOldHistoryOfNull(req);
    employeeLoanNew.setHistoryLoanFlag(true);
    return employeeLoanNew;
  }

  // v2
  @Transactional
  public EmployeeLoanNew searchEmployeeLoanNewV2(DocumentReq req) {
    boolean flagLoan = false;
    if (req.getLoanId() != null) {
      val loadDetail = documentRepository.searchEmployeeLoanOfNull(req);
      if (loadDetail.isEmpty()) {
        flagLoan = true;
        req.setLoanId(null);
      }
    } else {
      flagLoan = true;
    }

    if (flagLoan) {
      val employeeLoanNew = documentRepository.searchEmployeeLoanOldHistoryOfNull(req);
      if (employeeLoanNew != null) {
        employeeLoanNew.setHistoryLoanFlag(true);
      }
      return employeeLoanNew;
    } else {
      return documentRepository.searchEmployeeLoanNew(req); // searchEmployeeLoanNewOfNull
    }
  }

  @Transactional
  public EmployeeLoanNew searchEmployeeLoanOldV2(DocumentReq req) {
    val employeeLoanNew = documentRepository.searchEmployeeLoanOldHistoryOfNull(req);
    if (employeeLoanNew != null) {
      employeeLoanNew.setHistoryLoanFlag(true);
    }
    return employeeLoanNew;
  }

  // all bill receipt
  @Transactional
  public InputStreamResource receiptStock(ReportReq req) throws Exception {
    val pdfList = new ArrayList<InputStreamResource>();
    val nameList = new ArrayList<String>();

    val config1 = adminConfigRepository.findById(4L).get();
    byte[] imageBytes1 = config1.getImage().getBytes(1, (int) config1.getImage().length());

    val config2 = adminConfigRepository.findById(5L).get();
    byte[] imageBytes2 = config2.getImage().getBytes(1, (int) config2.getImage().length());

    val find = documentRepository.findByActiveTrueAndEmployee();
    for (val stock : find) {
      req.setStockId(stock.getStockId());
      req.setEmpCode(stock.getEmployeeCode());
      req.setEmpId(stock.getEmpId());
      req.setLoanId(stock.getLoanId());

      pdfList.add(generateReceiptStockReport(req, stock, imageBytes1, imageBytes2));

      nameList.add(stock.getEmployeeCode() + "_" + stock.getFirstName());
    }

    return ZipUtil.createZipFileR5(pdfList, nameList);
  }

  @Transactional
  public InputStreamResource receiptStockEmpCode(ReportReq req) throws Exception {
    val find = documentRepository.findByActiveTrueAndEmployeeCode(req.getEmpCode());
    req.setStockId(find.getStockId());
    req.setEmpCode(find.getEmployeeCode());
    req.setEmpId(find.getEmpId());
    req.setLoanId(find.getLoanId());

    val config1 = adminConfigRepository.findById(4L).get();
    byte[] imageBytes1 = config1.getImage().getBytes(1, (int) config1.getImage().length());

    val config2 = adminConfigRepository.findById(5L).get();
    byte[] imageBytes2 = config2.getImage().getBytes(1, (int) config2.getImage().length());

    return generateReceiptStockReport(req, find, imageBytes1, imageBytes2);
  }

  public InputStreamResource generateReceiptStockReport(
      ReportReq req, StockAndEmployeeCodeRes resEmp, byte[] imageBytes1, byte[] imageBytes2)
      throws Exception {
    val currentDate = LocalDate.now();
    val monthNow = DateUtils.getThaiMonthInt(currentDate.getMonthValue());
    val yearNow = String.valueOf(currentDate.getYear() + 543);

    val stockDetails = stockDetailRepository.findAllByStock_Id(req.getStockId());
    val lastStockDetail = stockDetails.get(stockDetails.size() - 1);

    val documentReq = new DocumentReq();
    documentReq.setEmpCode(req.getEmpCode());
    documentReq.setMonthCurrent(req.getMonthCurrent());
    documentReq.setYearCurrent(req.getYearCurrent());
    documentReq.setEmpId(req.getEmpId());
    documentReq.setStockId(req.getStockId());
    documentReq.setLoanId(req.getLoanId());

    EmployeeLoanNew searchEmp;
    if (Objects.equals(req.getMonthCurrent(), monthNow)
        && Objects.equals(req.getYearCurrent(), yearNow)) {
      searchEmp = searchEmployeeLoanNewV2(documentReq);
    } else if (Objects.equals(req.getMonthCurrent(), lastStockDetail.getStockMonth())
        && Objects.equals(req.getYearCurrent(), lastStockDetail.getStockYear())) {
      searchEmp = searchEmployeeLoanNewV2(documentReq);
    } else {
      searchEmp = searchEmployeeLoanOldV2(documentReq);
    }

    val res = new ReportRes();
    res.setMonth(req.getMonthCurrent() + " " + req.getYearCurrent());
    res.setEmployeeCode(req.getEmpCode());
    res.setDepartmentName(resEmp.getDepartmentName());
    res.setFullName(resEmp.getFullName());
    if (searchEmp != null) {
      res.setStockAccumulate(
          searchEmp.getStockAccumulate() != null
              ? Double.parseDouble(searchEmp.getStockAccumulate())
              : 0.00);
      res.setStockDetailInstallment(
          searchEmp.getStockDetailInstallment() != null
              ? searchEmp.getStockDetailInstallment()
              : 0L);
      res.setStockValue(
          searchEmp.getStockValue() != null ? Double.parseDouble(searchEmp.getStockValue()) : 0.00);
      res.setInstallment(searchEmp.getInstallment() != null ? searchEmp.getInstallment() : 0L);
      res.setInterest(
          searchEmp.getInterestLoanLastMonth() != null
              ? Long.parseLong(searchEmp.getInterestLoanLastMonth())
              : 0L);
      res.setPrincipalBalance(
          searchEmp.getLoanBalance() != null
              ? Double.parseDouble(searchEmp.getLoanBalance())
              : 0.00);
      res.setTotalDeduction(
          (searchEmp.getLoanOrdinary() != null
                  ? Double.parseDouble(searchEmp.getLoanOrdinary())
                  : 0.00)
              - (searchEmp.getInterestLoanLastMonth() != null
                  ? Double.parseDouble(searchEmp.getInterestLoanLastMonth())
                  : 0.00));
    }
    res.setTotalPrice(res.getStockValue() + res.getTotalDeduction() + res.getInterest());
    res.setTotalText("(" + ThaiNumeralsUtils.formatThaiWords(res.getTotalPrice()) + ")");

    res.setSignature1(imageBytes1);
    res.setSignature2(imageBytes2);

    val params = new HashMap<String, Object>();
    params.put(JRParameter.REPORT_LOCALE, new Locale("th"));

    val filePath = "report/icoop_receipt_stock.jrxml";
    val jasperXmlInputStream = new ClassPathResource(filePath).getInputStream();
    val jasperDesign = JRXmlLoader.load(jasperXmlInputStream);
    val jasperReport = JasperCompileManager.compileReport(jasperDesign);

    return processStream(jasperReport, res, params);
  }

  public InputStreamResource processStream(
      JasperReport compiledReport, ReportRes req, HashMap<String, Object> params)
      throws JRException, IOException {
    try (val output = new ByteArrayOutputStream()) {
      val print =
          JasperFillManager.fillReport(
              compiledReport, params, new JRBeanCollectionDataSource(List.of(req)));
      val exporter = new JRPdfExporter();
      exporter.setExporterInput(new SimpleExporterInput(print));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
      exporter.exportReport();
      return new InputStreamResource(new ByteArrayInputStream(output.toByteArray()));
    }
  }

  @Transactional
  public List<GuaranteeRes> searchGuarantorUnique(String empCode) {
    try {
      var result = documentRepository.getEmpCodeOfId(empCode);
      return documentRepository.documentGuarantee(result.getEmpId());
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public DocumentReq searchEmpCodeOfId(String empCode) {
    try {
      return documentRepository.getEmpCodeOfId(empCode);
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public DocumentReq searchIdOfEmpCode(Long empCode) {
    try {
      return documentRepository.getIdOfEmpCode(empCode);
    } catch (Exception e) {
      return null;
    }
  }

  @Transactional
  public GrandTotalRes grandTotal(DocumentReq req) {
    // val res = documentRepository.grandTotal(req.getYearCurrent(), req.getMonthCurrent());
    GrandTotalRes res = new GrandTotalRes();
    val resEmp = documentRepository.documentInfoSumEmp();
    res.setSumEmp(resEmp.getSumEmp());
    val resLoan = documentRepository.documentInfoSumLoanEmp();
    res.setSumLoan(resLoan.getSumLoan());
    //    val resLoanBalance = documentRepository.getSumLoanBalance(req.getYearCurrent(),
    // req.getMonthCurrent());
    //    res.setSumLoanBalance(resLoanBalance.getSumLoanBalance());
    val listLoanBalance =
        documentRepository.getSumLoanBalanceList(req.getYearCurrent(), req.getMonthCurrent());
    if (listLoanBalance.size() > 0) {
      calculateSumLoanBalance(listLoanBalance, res);
    } else {
      res.setSumLoanBalance(0L);
    }
    val resStockAccumulate =
        documentRepository.getSumStockAccumulate(req.getYearCurrent(), req.getMonthCurrent());
    res.setSumStockAccumulate(
        resStockAccumulate.getSumStockAccumulate() != null
            ? resStockAccumulate.getSumStockAccumulate()
            : 0);

    val resStockValue =
        documentRepository.getSumStockValue(req.getYearCurrent(), req.getMonthCurrent());
    res.setSumStockValue(
        resStockValue.getSumStockValue() != null ? resStockValue.getSumStockValue() : 0);
    val resLoanInterest =
        documentRepository.getSumLoanInterest(req.getYearCurrent(), req.getMonthCurrent());
    res.setSumLoanInterest(
        resLoanInterest.getSumLoanInterest() != null ? resLoanInterest.getSumLoanInterest() : 0);
    //    val resLoanOrdinary = documentRepository.getSumLoanOrdinary(req.getYearCurrent(),
    // req.getMonthCurrent());
    //    res.setSumLoanOrdinary(resLoanOrdinary.getSumLoanOrdinary());
    val listLoanOrdinary =
        documentRepository.getSumLoanOrdinaryList(req.getYearCurrent(), req.getMonthCurrent());
    if (listLoanOrdinary.size() > 0) {
      calculateSumLoanOrdinary(listLoanOrdinary, res);
    } else {
      res.setSumLoanOrdinary(0L);
    }
    val sumTotal = (res.getSumStockValue() + res.getSumLoanInterest() + res.getSumLoanOrdinary());
    res.setSumTotal(sumTotal);

    return res;
  }

  public void calculateSumLoanBalance(List<LoanDetailDto> loanBalanceList, GrandTotalRes res) {
    if (loanBalanceList.size() > 0) {
      double sumLoanBalance = 0;
      for (LoanDetailDto list : loanBalanceList) {
        if (list.getInstallment() > 0) {
          if (list.getLoanBalance() > 0) {
            sumLoanBalance =
                sumLoanBalance
                    + (list.getLoanBalance()
                        + Math.round((list.getLoanOrdinary() - list.getInterest())));
          } else {
            sumLoanBalance = sumLoanBalance + list.getLoanOrdinary();
          }
        } else {
          sumLoanBalance = sumLoanBalance + 0;
        }
      }
      res.setSumLoanBalance((long) sumLoanBalance);
    }
  }

  public void calculateSumLoanOrdinary(List<LoanDetailDto> loanBalanceList, GrandTotalRes res) {
    if (loanBalanceList.size() > 0) {
      double sumLoanBalance = 0;
      //      double sumTotalLoanOrdinary = 0;
      for (LoanDetailDto list : loanBalanceList) {
        //        double sumLoanBalanceBox = 0;
        if (list.getInstallment() > 0) {
          if (list.getLoanBalance() > 0) {
            sumLoanBalance =
                sumLoanBalance + Math.round((list.getLoanOrdinary() - list.getInterest()));
          } else {
            sumLoanBalance = sumLoanBalance + list.getLoanOrdinary();
          }
        } else {
          sumLoanBalance = sumLoanBalance + 0;
        }
        //        sumLoanBalanceBox = list.getLoanOrdinary() - list.getInterest();
        //        sumTotalLoanOrdinary += sumLoanBalanceBox;
      }
      res.setSumLoanOrdinary((long) sumLoanBalance);
    }
  }

  @Transactional
  public List<DocumentInfoAllRes> documentInfoAll(DocumentReq req) {
    List<DocumentInfoAllRes> listInfoAll =
        documentRepository.documentInfoAll(req.getMonthCurrent(), req.getYearCurrent());

    // guarantee
    listInfoAll.forEach(
        infoAll -> {
          val guarantee = documentRepository.documentGuarantee(infoAll.getId());

          if (guarantee.size() > 1) {
            guarantee.stream()
                .findFirst()
                .ifPresent(
                    guaranteeOne -> {
                      infoAll.setCodeGuaranteeOne(guaranteeOne.getCodeGuarantee());
                      infoAll.setFullNameGuaranteeOne(guaranteeOne.getFullNameGuarantee());
                    });

            guarantee.stream()
                .reduce((first, second) -> second)
                .ifPresent(
                    guaranteeTwo -> {
                      infoAll.setCodeGuaranteeTwo(guaranteeTwo.getCodeGuarantee());
                      infoAll.setFullNameGuaranteeTwo(guaranteeTwo.getFullNameGuarantee());
                    });
          } else {
            guarantee.stream()
                .findFirst()
                .ifPresent(
                    guaranteeOne -> {
                      infoAll.setCodeGuaranteeOne(guaranteeOne.getCodeGuarantee());
                      infoAll.setFullNameGuaranteeOne(guaranteeOne.getFullNameGuarantee());
                    });
          }

          DocumentReq reqStock = new DocumentReq();
          reqStock.setStockId(infoAll.getStockId());
          reqStock.setMonthCurrent(req.getMonthCurrent());
          reqStock.setYearCurrent(req.getYearCurrent());
          stockDetailLoginRepository.documentInfoV3StockDetail(reqStock).stream()
              .findFirst()
              .ifPresent(stockDetailDto -> infoAll.setInstallment(stockDetailDto.getInstallment()));

          // loan details
          if (infoAll.getLoanId() != null) {
            if (infoAll.getNewLoan() == false || infoAll.getNewLoan() == null) {
              val calculateReq = new CalculateReq();
              calculateReq.setPrincipal(infoAll.getLoanValue());
              calculateReq.setInterestRate(infoAll.getInterestPercent());
              calculateReq.setNumOfPayments(Integer.parseInt(infoAll.getLoanTime()));
              calculateReq.setPaymentStartDate("2023-01-31");

              val loan =
                  loanDetailLogicRepository.loanDetail(
                      infoAll.getLoanId(), req.getMonthCurrent(), req.getYearCurrent());
              loan.stream()
                  .reduce((first, second) -> second)
                  .ifPresent(
                      interest -> {
                        try {
                          val calculate = calculateLoanOld(calculateReq);

                          int sumTotalValueInterest = 0;
                          int setTotalValuePrinciple = 0;
                          int sumTotalValueInterestOfInstallment = 0;
                          int sumTotalValuePrincipleOfInstallment = 0;

                          for (CalculateInstallments calculation : calculate) {
                            sumTotalValueInterest += calculation.getInterest();
                            setTotalValuePrinciple += calculation.getTotalDeduction();
                            if (calculation.getInstallment() == interest.getInstallment()) {
                              infoAll.setInterestMonth(calculation.getInterest());
                              infoAll.setEarlyMonth(calculation.getTotalDeduction());
                              infoAll.setInstallmentLoan(calculation.getInstallment());

                              // Interest
                              sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                              infoAll.setTotalValueInterest(sumTotalValueInterestOfInstallment);
                              // Principle
                              infoAll.setTotalValuePrinciple(setTotalValuePrinciple);
                              infoAll.setOutStandPrinciple(calculation.getPrincipalBalance());
                            }

                            if (calculation.getInstallment()
                                == Integer.parseInt(infoAll.getLoanTime())) {
                              infoAll.setInterestMonthLast(
                                  Integer.parseInt(
                                      infoAll.getInterestLastMonth() != null
                                          ? infoAll.getInterestLastMonth()
                                          : null));
                              infoAll.setEarlyMonthLast(calculation.getTotalDeduction());
                            }
                          }
                          infoAll.setOutStandInterest(
                              sumTotalValueInterest - sumTotalValueInterestOfInstallment);
                        } catch (ParseException e) {
                          throw new RuntimeException(e);
                        }
                      });
            } else {
              val calculateReq = new CalculateReq();
              calculateReq.setPrincipal(infoAll.getLoanValue());
              calculateReq.setInterestRate(infoAll.getInterestPercent());
              calculateReq.setNumOfPayments(Integer.parseInt(infoAll.getLoanTime()));
              calculateReq.setPaymentStartDate(infoAll.getStartLoanDate());

              val loan =
                  loanDetailLogicRepository.loanDetail(
                      infoAll.getLoanId(), req.getMonthCurrent(), req.getYearCurrent());
              loan.stream()
                  .reduce((first, second) -> second)
                  .ifPresent(
                      interest -> {
                        try {
                          val calculate = calculateLoanNew(calculateReq);

                          int sumTotalValueInterest = 0;
                          int setTotalValuePrinciple = 0;
                          int sumTotalValueInterestOfInstallment = 0;
                          int sumTotalValuePrincipleOfInstallment = 0;

                          for (CalculateInstallments calculation : calculate) {
                            sumTotalValueInterest += calculation.getInterest();
                            setTotalValuePrinciple +=
                                calculation.getPrincipal(); // getTotalDeduction
                            if (calculation.getInstallment() == interest.getInstallment()) {
                              infoAll.setInterestMonth(calculation.getInterest());
                              infoAll.setEarlyMonth(calculation.getTotalDeduction());
                              infoAll.setInstallmentLoan(calculation.getInstallment());

                              sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                              sumTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
                              infoAll.setTotalValueInterest(sumTotalValueInterestOfInstallment);
                              infoAll.setTotalValuePrinciple(sumTotalValuePrincipleOfInstallment);
                              infoAll.setOutStandPrinciple(
                                  calculation.getPrincipalBalance()); // getBalanceLoan
                            }

                            if (calculation.getInstallment()
                                == Integer.parseInt(infoAll.getLoanTime())) {
                              infoAll.setInterestMonthLast(calculation.getInterest());
                              infoAll.setEarlyMonthLast(calculation.getTotalDeduction());
                            }
                          }
                          infoAll.setOutStandInterest(
                              sumTotalValueInterest); // sumTotalValueInterest -
                          // sumTotalValueInterestOfInstallment,
                          // infoAll.setOutStandPrinciple(setTotalValuePrinciple);
                          // //setTotalValuePrinciple - sumTotalValuePrincipleOfInstallment,
                        } catch (ParseException e) {
                          throw new RuntimeException(e);
                        }
                      });
            }
          }
        });

    return listInfoAll;
  }

  // loan
  @Transactional
  public List<DocumentV1ResLoan> searchDocumentV1Loan(
      Long loanId, String getMonthCurrent, Boolean admin, Long empId, String yearCurrent) {
    if (!admin) {
      return null;
    } else {
      String testHistory = "";
      testHistory = String.valueOf(loanId);
      val loanHistory = loanDetailLogicRepository.loanHistory(empId);
      for (int i = 0; i < loanHistory.size(); i++) {
        testHistory = testHistory + ',' + loanHistory.get(i).getLoanId();
      }
      var resLoan =
          documentRepository.documentInfoV1Loan(loanId, getMonthCurrent, testHistory, yearCurrent);
      resLoan.forEach(
          res -> {
            if (res.getGuarantor1() != null) {
              val empCode1 = searchIdOfEmpCode(Long.valueOf(res.getGuarantor1()));
              res.setGuarantorCode1(empCode1.getEmpCode());
            }
            if (res.getGuarantor2() != null) {
              val empCode2 = searchIdOfEmpCode(Long.valueOf(res.getGuarantor2()));
              res.setGuarantorCode2(empCode2.getEmpCode());
            }
            //    function --> calculateLoanOld()
            if (res.getLoanValue() != null) {
              if (!res.getNewLoan() || res.getNewLoan() == null) {
                CalculateReq req = new CalculateReq();
                req.setPrincipal(Double.parseDouble(res.getLoanValue()));
                req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
                req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
                req.setPaymentStartDate(res.getStartLoanDate());
                try {
                  List<CalculateInstallments> resList =
                      calculateLoanOld(
                          req); // ** function --> calculateLoanNew() , calculateLoanOld
                  int sumTotalValueInterest = 0;
                  int setTotalValuePrinciple = 0;
                  int sumTotalValueInterestOfInstallment = 0;
                  int setTotalValuePrincipleOfInstallment = 0;
                  for (int i = 0; i < resList.size(); i++) {
                    sumTotalValueInterest += resList.get(i).getInterest();
                    setTotalValuePrinciple += resList.get(i).getTotalDeduction();
                    if (Integer.parseInt(res.getInstallment()) == resList.get(i).getInstallment()) {
                      res.setMonthInterest(String.valueOf(resList.get(i).getInterest()));
                      res.setMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
                      // Interest
                      sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                      res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
                      // Principle
                      res.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
                      res.setOutStandPrinciple(
                          String.valueOf(resList.get(i).getPrincipalBalance()));
                    }
                    if (Integer.parseInt(res.getLoanTime()) == resList.get(i).getInstallment()) {
                      res.setLastMonthInterest(
                          String.valueOf(
                              res.getInterestLastMonth())); // resList.get(i).getInterest())
                      res.setLastMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
                    }
                    res.setOutStandInterest(
                        String.valueOf(sumTotalValueInterest - sumTotalValueInterestOfInstallment));
                  }
                } catch (ParseException e) {
                  e.printStackTrace();
                }
              } else {
                CalculateReq req = new CalculateReq();
                req.setPrincipal(Double.parseDouble(res.getLoanValue()));
                req.setInterestRate(Double.parseDouble(res.getInterestPercent()));
                req.setNumOfPayments(Integer.parseInt(res.getLoanTime()));
                req.setPaymentStartDate(res.getStartLoanDate()); // "2024-01-01"
                //            if(res.getEmployeeCode().equals("05220")){
                //              System.out.println(res.getLoanValue() + "<----- getLoanValue");
                //              System.out.println(res.getInterestPercent() + " <----
                // getInterestPercent");
                //              System.out.println(res.getLoanTime() + " <---- getLoanTime");
                //              System.out.println(res.getStartLoanDate() + " <----
                // getStartLoanDate");
                //            }
                try {
                  List<CalculateInstallments> resList =
                      calculateLoanNew(
                          req); // ** function --> calculateLoanNew() , calculateLoanOld()
                  int sumTotalValueInterest = 0;
                  int setTotalValuePrinciple = 0;
                  int sumTotalValueInterestOfInstallment = 0;
                  int setTotalValuePrincipleOfInstallment = 0;
                  int sumOutStandInterest = 0;
                  int setOutStandPrinciple = 0;
                  for (int i = 0; i < resList.size(); i++) {
                    //                if(res.getEmployeeCode().equals("05220")){
                    //                   System.out.println(resList.get(i).getInterest() + "<-----
                    // Interest");
                    //                   System.out.println(resList.get(i).getPrincipal() + " <----
                    // Principal");
                    //                }
                    sumOutStandInterest += resList.get(i).getInterest();
                    setOutStandPrinciple += resList.get(i).getPrincipal();
                    if (Integer.parseInt(res.getInstallment()) == resList.get(i).getInstallment()) {
                      res.setMonthInterest(String.valueOf(resList.get(i).getInterest()));
                      res.setMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
                      //
                      // res.setTotalValueInterest(String.valueOf(sumTotalValueInterestOfInstallment));
                      //
                      // res.setTotalValuePrinciple(String.valueOf(setTotalValuePrincipleOfInstallment));
                    }
                    int installmentCurrent = Integer.parseInt(res.getInstallment()) - 1;
                    if (resList.get(i).getInstallment() <= installmentCurrent) {
                      sumTotalValueInterest += resList.get(i).getInterest();
                      setTotalValuePrinciple += resList.get(i).getPrincipal();
                      sumTotalValueInterestOfInstallment = sumTotalValueInterest;
                      setTotalValuePrincipleOfInstallment = setTotalValuePrinciple;
                      res.setTotalValueInterest(String.valueOf(sumTotalValueInterest));
                      res.setTotalValuePrinciple(String.valueOf(setTotalValuePrinciple));
                    }
                    if (Integer.parseInt(res.getLoanTime()) == resList.get(i).getInstallment()) {
                      res.setLastMonthInterest(String.valueOf(resList.get(i).getInterest()));
                      res.setLastMonthPrinciple(String.valueOf(resList.get(i).getTotalDeduction()));
                    }
                  }
                  res.setOutStandInterest(
                      String.valueOf(sumOutStandInterest - sumTotalValueInterestOfInstallment));
                  res.setOutStandPrinciple(
                      String.valueOf(setOutStandPrinciple - setTotalValuePrincipleOfInstallment));
                } catch (ParseException e) {
                  e.printStackTrace();
                }
              }
            }
          });
      return resLoan;
    }
  }

  @Transactional
  public List<DocumentV2ResLoan> searchDocumentV2Loan(
      Long loanId, String getMonthCurrent, String yearCurrent) {
    return documentRepository.documentInfoV2Loan(loanId, getMonthCurrent, yearCurrent);
  }

  // calculate Loan

  // loan New
  @Transactional
  public List<CalculateInstallments> calculateLoanNew(CalculateReq req) throws ParseException {
    double principal = req.getPrincipal();
    double interestRate = req.getInterestRate();
    int numOfPayments = req.getNumOfPayments();

    String dateSt = req.getPaymentStartDate();
    LocalDate date = LocalDate.parse(dateSt);

    // calculate loan
    LocalDate paymentStartDate =
        LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());
    double installment = calculateLoanInstallment(principal, interestRate, numOfPayments);
    List<CalculateInstallments> calculateInstallments =
        createAmortizationTableNew(
            principal, interestRate, numOfPayments, installment, paymentStartDate);

    return calculateInstallments;
  }

  public double calculateLoanInstallment(double principal, double interestRate, int numOfPayments) {
    double monthlyInterestRate = (interestRate / 100) / 12;
    double installment =
        (principal * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numOfPayments));
    return installment;
  }

  public List<CalculateInstallments> createAmortizationTableNew(
      double principal,
      double interestRate,
      int numOfPayments,
      double installment,
      LocalDate paymentStartDate) {
    List<CalculateInstallments> result = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("#");

    double remainingBalance = principal;
    double toralDeduction = principal;
    LocalDate paymentDate = paymentStartDate;
    LocalDate paymentDateDeduction = paymentStartDate;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateFormatterDay = DateTimeFormatter.ofPattern("dd");

    for (int i = 1; i <= numOfPayments; i++) {
      CalculateInstallments cal = new CalculateInstallments();
      LocalDate paymentDateShow = paymentStartDate.plusMonths(i - 1);

      // Balance
      YearMonth currentPaymentMonth = YearMonth.from(paymentDate);
      int daysInMonth = currentPaymentMonth.lengthOfMonth();
      double interest = Math.round((remainingBalance * (interestRate / 100) / 365) * daysInMonth);
      double principalPaid = Math.round(installment - interest);

      if (i == numOfPayments) {
        double principalPaidLast = remainingBalance;
        double installmentSumLastMonth = principalPaidLast - interest;
        // remainingBalance -= principalPaid;
        // paymentDate.format(dateFormatter)

        cal.setInstallment(i);
        cal.setBalanceLoan(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        cal.setDeductionDate(paymentDateShow.format(dateFormatter));
        cal.setAmountDay(paymentDateShow.format(dateFormatterDay));
        cal.setInterest((int) interest);
        cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        cal.setPrincipalBalance(0);
        cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        result.add(cal);
      } else {
        remainingBalance -= principalPaid;
        if (i == 1) {
          cal.setInstallment(i);
          cal.setBalanceLoan(Integer.parseInt(decimalFormat.format(principal)));
          cal.setDeductionDate(paymentDateShow.format(dateFormatter));
          cal.setAmountDay(paymentDateShow.format(dateFormatterDay));
          cal.setInterest(Integer.parseInt(decimalFormat.format(interest)));
          cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaid)));
          cal.setPrincipalBalance(Integer.parseInt(decimalFormat.format(remainingBalance)));
          cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(installment)));
          result.add(cal);
        } else {
          // toralDeduction
          YearMonth currentPaymentMonthDeduction = YearMonth.from(paymentDateDeduction);
          int daysInMonthDeduction = currentPaymentMonthDeduction.lengthOfMonth();
          double interestDeduction =
              Math.round((toralDeduction * (interestRate / 100) / 365) * daysInMonthDeduction);
          double principalPaidDeduction = Math.round(installment - interestDeduction);
          toralDeduction -= principalPaidDeduction;
          paymentDateDeduction = paymentDateDeduction.plusMonths(1);

          cal.setInstallment(i);
          cal.setBalanceLoan(Integer.parseInt(decimalFormat.format(toralDeduction)));
          cal.setDeductionDate(paymentDateShow.format(dateFormatter));
          cal.setAmountDay(paymentDateShow.format(dateFormatterDay));
          cal.setInterest(Integer.parseInt(decimalFormat.format(interest)));
          cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaid)));
          cal.setPrincipalBalance(Integer.parseInt(decimalFormat.format(remainingBalance)));
          cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(installment)));
          result.add(cal);
        }
      }
      paymentDate = paymentDate.plusMonths(1);
    }

    return result;
  }

  // lan old

  @Transactional
  public List<CalculateInstallments> calculateLoanOld(CalculateReq req) throws ParseException {
    double principal = req.getPrincipal();
    double interestRate = req.getInterestRate();
    int numOfPayments = req.getNumOfPayments();

    String dateSt = req.getPaymentStartDate();
    LocalDate date = LocalDate.parse(dateSt);

    // calculate loan
    LocalDate paymentStartDate =
        LocalDate.of(date.getYear(), date.getMonth(), date.getDayOfMonth());

    double installment = calculateLoanInstallment(principal, interestRate, numOfPayments);
    List<CalculateInstallments> calculateInstallments =
        createAmortizationTableOld(principal, interestRate, numOfPayments);

    return calculateInstallments;
  }

  public List<CalculateInstallments> createAmortizationTableOld(
      double principal, double interestRates, int numOfPayments) {
    List<CalculateInstallments> result = new ArrayList<>();
    DecimalFormat decimalFormat = new DecimalFormat("#");

    double loanAmount = principal; // Replace with the actual loan amount
    double interestRate = interestRates; // Replace with the actual interest rate
    int loanTermInMonths = numOfPayments; // Replace with the actual loan term in months

    double monthlyInterestRate = interestRate / 12;
    double monthlyPayment = loanAmount / loanTermInMonths;

    for (int i = 1; i <= loanTermInMonths; i++) {
      CalculateInstallments cal = new CalculateInstallments();
      double interestPayment = Math.round((loanAmount * (interestRate / 100)) / 12);
      double principalPayment = Math.round(monthlyPayment - interestPayment);

      if (i == loanTermInMonths) {
        double principalPaidLast = loanAmount;
        cal.setInstallment(i);
        cal.setBalanceLoan(0);
        cal.setDeductionDate("");
        cal.setAmountDay("");
        cal.setInterest((int) interestPayment);
        cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        cal.setPrincipalBalance(0);
        cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(principalPaidLast)));
        result.add(cal);
      } else {
        loanAmount = Math.round(loanAmount - monthlyPayment);
        cal.setInstallment(i);
        cal.setBalanceLoan(0);
        cal.setDeductionDate("");
        cal.setAmountDay("");
        cal.setInterest(Integer.parseInt(decimalFormat.format(interestPayment)));
        cal.setPrincipal(Integer.parseInt(decimalFormat.format(principalPayment)));
        cal.setPrincipalBalance(Integer.parseInt(decimalFormat.format(loanAmount)));
        cal.setTotalDeduction(Integer.parseInt(decimalFormat.format(monthlyPayment)));
        result.add(cal);
      }
    }

    return result;
  }

  @Transactional
  public List<DocumentStockDevidend> calculateStockDividend(DocumentReq req) {
    var resDividend =
        documentRepository.documentInfoStockDividend(req.getEmpCode(), req.getYearCurrent(), null);
    if (resDividend != null) {
      resDividend.forEach(
          res -> {
            int totalDividend = 0;
            // stock dividend
            double sumYearOld = 0;
            double sumYearCurrent = 0;
            int stockDividend = 0;

            // Dividend of employeeCode ( yearCurrent )
            var resDividendYearCurrent =
                documentRepository.documentInfoStockDividendV1(
                    res.getEmployeeCode(), req.getYearCurrent(), null);
            // Dividend of employeeCode ( yearOld )
            var resDividendYearOld =
                documentRepository.documentInfoStockDividendV1(
                    res.getEmployeeCode(), null, req.getYearOld());

            if (!Objects.requireNonNull(resDividendYearOld).isEmpty()) {
              double stockDividendPercent = Double.parseDouble(req.getStockDividendPercent()) / 100;
              int stockValue = Integer.parseInt(resDividendYearOld.get(0).getStockValue());
              int stockAccumulate =
                  Integer.parseInt(resDividendYearOld.get(0).getStockAccumulate()) - stockValue;
              sumYearOld = (stockAccumulate * stockDividendPercent); // * ((12-0) / 12)
              BigDecimal roundedValue =
                  new BigDecimal(sumYearOld).setScale(2, RoundingMode.HALF_UP);
              sumYearOld = roundedValue.doubleValue();
            }
            for (DocumentStockDevidend documentStockDevidend : resDividendYearCurrent) {
              String stockMonth = documentStockDevidend.getStockMonth();
              int monthDifference = 12 - getMonthNumber(stockMonth);

              if (!stockMonth.equalsIgnoreCase("ธันวาคม")) {
                double sumMonth = 0;
                double stockDividendPercent =
                    Double.parseDouble(req.getStockDividendPercent()) / 100;
                int stockValueMonth = Integer.parseInt(documentStockDevidend.getStockValue());
                sumMonth = ((stockValueMonth * stockDividendPercent) * monthDifference / 12);
                BigDecimal roundedValue =
                    new BigDecimal(sumMonth).setScale(2, RoundingMode.HALF_UP);
                sumYearCurrent += roundedValue.doubleValue();
              }
            }
            stockDividend = (int) (sumYearOld + sumYearCurrent);
            res.setStockDividend(String.valueOf(stockDividend));

            // Interest dividend
            int sumInterest = 0;
            int interestDividend = 0;

            val loanHistory =
                loanDetailLogicRepository.LoanDetailHistoryList(res.getEmployeeCode());

            val loanIds = loanHistory.stream().map(LoanHistoryDto::getLoanId).distinct().toList();

            if (!loanIds.isEmpty()) {
              var loanDividendYearCurrent =
                  documentRepository.loanByIdMergeHistoryOfLoanByIdForDividend(
                      loanIds, req.getYearCurrent());

              loanDividendYearCurrent =
                  loanDividendYearCurrent.stream()
                      .filter(
                          loan ->
                              !(loan.getGg().equals("history")
                                  && loan.getLoanMonth().equals("ธันวาคม")
                                  && loan.getLoanYear().equals("2567")))
                      .toList();

              for (DocumentInfoAllLoanEmpRes loanDividend : loanDividendYearCurrent) {
                sumInterest += loanDividend.getInterest();
              }
              double interestDividendPercent =
                  Double.parseDouble(req.getInterestDividendPercent()) / 100;
              interestDividend = (int) (sumInterest * interestDividendPercent);
              res.setCumulativeInterest(String.valueOf(sumInterest));
              res.setInterestDividend(String.valueOf(interestDividend));
            } else {
              val resSumV2 = calculateStockDividendV2(res.getEmployeeCode(), req);
              res.setCumulativeInterest(resSumV2.getCumulativeInterest());
              res.setInterestDividend(resSumV2.getInterestDividend());

              interestDividend = resSumV2.getInterestDividendIn();
            }
            //  รวมปนผล = ( ปนผลหุน + ปนผลดอกเบี้ย )
            totalDividend = stockDividend + interestDividend;
            res.setTotalDividend(String.valueOf(totalDividend));
          });
    }

    return resDividend;
  }

  @Transactional
  public SumLoanHistoryV2Res calculateStockDividendV2(String employeeCode, DocumentReq req) {
    // Interest dividend
    int sumInterest = 0;
    int interestDividend = 0;

    val res = new SumLoanHistoryV2Res();

    val loanHistory = loanDetailLogicRepository.LoanDetailHistoryListV2(employeeCode);

    val loanIds = loanHistory.stream().map(LoanHistoryV2Res::getLLoanId).distinct().toList();

    if (!loanIds.isEmpty()) {
      var loanDividendYearCurrent =
          documentRepository.loanByIdMergeHistoryOfLoanByIdForDividend(
              loanIds, req.getYearCurrent());

      loanDividendYearCurrent =
          loanDividendYearCurrent.stream()
              .filter(
                  loan ->
                      !(loan.getGg().equals("history")
                          && loan.getLoanMonth().equals("ธันวาคม")
                          && loan.getLoanYear().equals("2567")))
              .toList();

      for (DocumentInfoAllLoanEmpRes loanDividend : loanDividendYearCurrent) {
        sumInterest += loanDividend.getInterest();
      }

      double interestDividendPercent = Double.parseDouble(req.getInterestDividendPercent()) / 100;
      interestDividend = (int) (sumInterest * interestDividendPercent);

      res.setCumulativeInterest(String.valueOf(sumInterest));
      res.setInterestDividend(String.valueOf(interestDividend));
      res.setInterestDividendIn(interestDividend);
    }

    return res;
  }

  private int getMonthNumber(String month) {
    return switch (month) {
      case "มกราคม" -> 1;
      case "กุมภาพันธ์" -> 2;
      case "มีนาคม" -> 3;
      case "เมษายน" -> 4;
      case "พฤษภาคม" -> 5;
      case "มิถุนายน" -> 6;
      case "กรกฎาคม" -> 7;
      case "สิงหาคม" -> 8;
      case "กันยายน" -> 9;
      case "ตุลาคม" -> 10;
      case "พฤศจิกายน" -> 11;
      default -> 12; // For "ธันวาคม" month
    };
  }

  @Transactional
  public List<DocumentInfoAllLoanEmpRes> searchDocumentV1LoanById(Long loanId, Long empId)
      throws ParseException {
    //    if (!admin) {
    //      return null;
    //    } else {
    val empData = documentRepository.employeeById(empId);
    if (loanId == null) {
      val loanHistory = loanDetailLogicRepository.LoanDetailHistoryList(empData.getEmployeeCode());
      loanId = (!loanHistory.isEmpty() ? loanHistory.get(0).getLoanId() : loanId);
    }
    val loanData = documentRepository.loanByIdMergeHistoryOfLoanById(loanId);

    for (val list : loanData) {
      int sumOrdinary = 0;
      list.setDepartmentName(empData.getDepartmentName());
      list.setEmployeeCode(empData.getEmployeeCode());
      list.setFullName(empData.getFullName());
      if (Integer.parseInt(list.getLoanYear()) >= 2567) {
        list.setLoanBalance(list.getLoanBalance() + (list.getLoanOrdinary() - list.getInterest()));
        val calculateReq = new CalculateReq();
        calculateReq.setPrincipal(list.getLoanValue());
        calculateReq.setInterestRate(list.getInterestPercent());
        calculateReq.setNumOfPayments(Integer.parseInt(list.getLoanTime()));
        calculateReq.setPaymentStartDate(list.getStartLoanDate());
        val calculate = calculateLoanNew(calculateReq);
        for (val calculation : calculate) {
          if (calculation.getInstallment() <= list.getInstallment()) {
            sumOrdinary += calculation.getPrincipal();
          }
          if (calculation.getInstallment() == list.getInstallment()) {
            list.setPrincipal(calculation.getPrincipal());
          }
        }
        list.setSumOrdinary(sumOrdinary - list.getPrincipal());
      } else {
        list.setSumOrdinary(list.getLoanOrdinary() * list.getInstallment());
        list.setPrincipal(0);
      }
    }
    return loanData;
    //    }
  }

  @Transactional
  public String calculateLoanbalance(CalculateReq req) throws ParseException {

    var result = documentRepository.documentCalLoanBalance(null, null, 0);
    for (val list : result) {
      double calLoanBalance = 0;
      if (list.getLoanBalance() > 0 && (list.getLoanBalance() > list.getLoanOrdinary())) {
        calLoanBalance = Math.round(list.getLoanBalance() - list.getLoanOrdinary());
      } else {
        calLoanBalance = 0; // list.getLoanBalance();
      }

      var resultOfLoanId =
          documentRepository.documentCalLoanBalanceOfSingle(
              null, null, list.getLoanId()); // list.getLoan().getId()
      if (resultOfLoanId != null) {
        double calResultBL = 0;
        if ((list.getLoanBalance() > list.getLoanOrdinary())) {
          calResultBL = calLoanBalance + resultOfLoanId.getInterest();
        } else {
          calResultBL = calLoanBalance;
        }
        val loanDetailHistory = loanDetailHistoryRepository.findById(resultOfLoanId.getId()).get();
        loanDetailHistory.setLoanBalance(calResultBL);
        loanDetailHistoryRepository.save(loanDetailHistory);
      }
    }
    return "success";
  }

  // TODO: TEST UPDATE LOAN HISTORY MONTH 11
  public void update() throws ParseException {
    val loans = loanRepository.findAll();

    for (val loan : loans) {
      val calculate = new CalculateReq();
      calculate.setPrincipal(loan.getLoanValue());
      calculate.setInterestRate(5);
      calculate.setNumOfPayments(loan.getLoanTime());
      calculate.setPaymentStartDate(loan.getStartLoanDate());

      val filteredHistories =
          loanDetailHistoryRepository.findAll().stream()
              .filter(
                  history ->
                      Objects.equals(history.getLoanMonth(), "พฤศจิกายน")
                          && Objects.equals(history.getLoanYear(), "2567")
                          && history.getLoan() == loan)
              .toList();

      if (!filteredHistories.isEmpty()) {

        val his = filteredHistories.get(0);

        val cals =
            calculateLoanNew(calculate).stream()
                .filter(cal -> cal.getInstallment() == his.getInstallment())
                .toList();

        his.setLoanBalance(cals.get(0).getPrincipalBalance());
        loanDetailHistoryRepository.save(his);
      }
    }
  }

  // Export EXCEL
  @Transactional
  public List<DocumentStockDevidend> calculateStockDividendExcel(DocumentReq req) {
    var resDividend =
        documentRepository.documentInfoStockDividend(req.getEmpCode(), req.getYearCurrent(), null);
    AtomicInteger i = new AtomicInteger(1);
    if (resDividend != null) {
      resDividend.forEach(
          res -> {
            int totalDividend = 0;
            // stock dividend
            double sumYearOld = 0;
            double sumYearCurrent = 0;
            int stockDividend = 0;

            // Dividend of employeeCode ( yearCurrent )
            var resDividendYearCurrent =
                documentRepository.documentInfoStockDividendV1(
                    res.getEmployeeCode(), req.getYearCurrent(), null);
            // Dividend of employeeCode ( yearOld )
            var resDividendYearOld =
                documentRepository.documentInfoStockDividendV1(
                    res.getEmployeeCode(), null, req.getYearOld());

            if (!Objects.requireNonNull(resDividendYearOld).isEmpty()) {
              double stockDividendPercent = Double.parseDouble(req.getStockDividendPercent()) / 100;
              int stockValue = Integer.parseInt(resDividendYearOld.get(0).getStockValue());
              int stockAccumulate =
                  Integer.parseInt(resDividendYearOld.get(0).getStockAccumulate()) - stockValue;
              sumYearOld = (stockAccumulate * stockDividendPercent); // * ((12-0) / 12)
              BigDecimal roundedValue =
                  new BigDecimal(sumYearOld).setScale(2, RoundingMode.HALF_UP);
              sumYearOld = roundedValue.doubleValue();
            }
            for (DocumentStockDevidend documentStockDevidend : resDividendYearCurrent) {
              String stockMonth = documentStockDevidend.getStockMonth();
              int monthDifference = 12 - getMonthNumber(stockMonth);

              if (!stockMonth.equalsIgnoreCase("ธันวาคม")) {
                double sumMonth = 0;
                double stockDividendPercent =
                    Double.parseDouble(req.getStockDividendPercent()) / 100;
                int stockValueMonth = Integer.parseInt(documentStockDevidend.getStockValue());
                sumMonth = ((stockValueMonth * stockDividendPercent) * monthDifference / 12);
                BigDecimal roundedValue =
                    new BigDecimal(sumMonth).setScale(2, RoundingMode.HALF_UP);
                sumYearCurrent += roundedValue.doubleValue();
              }
            }
            stockDividend = (int) (sumYearOld + sumYearCurrent);
            res.setStockDividend(NumberFormatUtils.numberFormat(String.valueOf(stockDividend)));

            // Interest dividend
            int sumInterest = 0;
            int interestDividend = 0;

            val loanHistory =
                loanDetailLogicRepository.LoanDetailHistoryList(res.getEmployeeCode());

            val loanIds = loanHistory.stream().map(LoanHistoryDto::getLoanId).distinct().toList();

            if (!loanIds.isEmpty()) {
              var loanDividendYearCurrent =
                  documentRepository.loanByIdMergeHistoryOfLoanByIdForDividend(
                      loanIds, req.getYearCurrent());

              loanDividendYearCurrent =
                  loanDividendYearCurrent.stream()
                      .filter(
                          loan ->
                              !(loan.getGg().equals("history")
                                  && loan.getLoanMonth().equals("ธันวาคม")
                                  && loan.getLoanYear().equals("2567")))
                      .toList();

              for (DocumentInfoAllLoanEmpRes loanDividend : loanDividendYearCurrent) {
                sumInterest += loanDividend.getInterest();
              }
              double interestDividendPercent =
                  Double.parseDouble(req.getInterestDividendPercent()) / 100;
              interestDividend = (int) (sumInterest * interestDividendPercent);
              res.setCumulativeInterest(
                  NumberFormatUtils.numberFormat(String.valueOf(sumInterest)));
              res.setInterestDividend(
                  NumberFormatUtils.numberFormat(String.valueOf(interestDividend)));
            } else {
              val resSumV2 = calculateStockDividendV2(res.getEmployeeCode(), req);
              res.setCumulativeInterest(
                  NumberFormatUtils.numberFormat(resSumV2.getCumulativeInterest()));
              res.setInterestDividend(
                  NumberFormatUtils.numberFormat(resSumV2.getInterestDividend()));

              interestDividend = resSumV2.getInterestDividendIn();
            }
            //  รวมปนผล = ( ปนผลหุน + ปนผลดอกเบี้ย )
            totalDividend = stockDividend + interestDividend;
            res.setTotalDividend(NumberFormatUtils.numberFormat(String.valueOf(totalDividend)));

            res.setStockAccumulate(NumberFormatUtils.numberFormat(res.getStockAccumulate()));

            res.setIndex(String.format("%05d", i.getAndIncrement()));
          });
    }

    return resDividend;
  }

  public static DocumentStockDevidend calculateSums(List<DocumentStockDevidend> dataList) {
    DocumentStockDevidend sumResult = new DocumentStockDevidend();

    // Calculate the sums
    long stockAccumulateSum =
        dataList.stream()
            .mapToLong(doc -> NumberFormatUtils.parseNumber(doc.getStockAccumulate()))
            .sum();
    long stockDividendSum =
        dataList.stream()
            .mapToLong(doc -> NumberFormatUtils.parseNumber(doc.getStockDividend()))
            .sum();
    long cumulativeInterestSum =
        dataList.stream()
            .mapToLong(doc -> NumberFormatUtils.parseNumber(doc.getCumulativeInterest()))
            .sum();
    long interestDividendSum =
        dataList.stream()
            .mapToLong(doc -> NumberFormatUtils.parseNumber(doc.getInterestDividend()))
            .sum();
    long totalDividendSum =
        dataList.stream()
            .mapToLong(doc -> NumberFormatUtils.parseNumber(doc.getTotalDividend()))
            .sum();

    // Set the calculated sums in the result object
    sumResult.setStockAccumulate(
        NumberFormatUtils.numberFormat(String.valueOf(stockAccumulateSum)));
    sumResult.setStockDividend(NumberFormatUtils.numberFormat(String.valueOf(stockDividendSum)));
    sumResult.setCumulativeInterest(
        NumberFormatUtils.numberFormat(String.valueOf(cumulativeInterestSum)));
    sumResult.setInterestDividend(
        NumberFormatUtils.numberFormat(String.valueOf(interestDividendSum)));
    sumResult.setTotalDividend(NumberFormatUtils.numberFormat(String.valueOf(totalDividendSum)));
    sumResult.setFullName("รวม");

    return sumResult;
  }

  @Transactional
  public ByteArrayOutputStream exportDividends(DocumentReq req) throws IOException {
    val dataList = calculateStockDividendExcel(req);

    if (dataList.isEmpty()) {
      return null;
    }

    val sum = calculateSums(dataList);
    dataList.add(sum);

    return generateExportDividendFile(dataList);
  }

  @Transactional
  public ByteArrayOutputStream generateExportDividendFile(List<DocumentStockDevidend> dataList)
      throws IOException {
    val input = new ClassPathResource("excel-template/template-dividends.xlsx").getInputStream();

    val output = new ByteArrayOutputStream();
    val context = new Context();

    context.putVar("dataList", dataList);
    JxlsHelper.getInstance().processTemplate(input, output, context);

    return output;
  }

  @Transactional
  public List<ReportRes> empForReceiptList(ReportReq req) throws Exception {
    List<ReportRes> resReceipt = new ArrayList<>();

    val find = documentRepository.findByActiveTrueAndEmployee();
    for (val stock : find) {
      req.setStockId(stock.getStockId());
      req.setEmpCode(stock.getEmployeeCode());
      req.setEmpId(stock.getEmpId());
      req.setLoanId(stock.getLoanId());

      var receiptEmp = generateReceiptStockForObj(req, stock);
      resReceipt.add(receiptEmp);

    }

    return resReceipt;
  }

  public ReportRes generateReceiptStockForObj(ReportReq req, StockAndEmployeeCodeRes resEmp)
          throws Exception {
    val currentDate = LocalDate.now();
    val monthNow = DateUtils.getThaiMonthInt(currentDate.getMonthValue());
    val yearNow = String.valueOf(currentDate.getYear() + 543);

    val stockDetails = stockDetailRepository.findAllByStock_Id(req.getStockId());
    val lastStockDetail = stockDetails.get(stockDetails.size() - 1);

    val documentReq = new DocumentReq();
    documentReq.setEmpCode(req.getEmpCode());
    documentReq.setMonthCurrent(req.getMonthCurrent());
    documentReq.setYearCurrent(req.getYearCurrent());
    documentReq.setEmpId(req.getEmpId());
    documentReq.setStockId(req.getStockId());
    documentReq.setLoanId(req.getLoanId());

    EmployeeLoanNew searchEmp;
    if (Objects.equals(req.getMonthCurrent(), monthNow)
            && Objects.equals(req.getYearCurrent(), yearNow)) {
      searchEmp = searchEmployeeLoanNewV2(documentReq);
    } else if (Objects.equals(req.getMonthCurrent(), lastStockDetail.getStockMonth())
            && Objects.equals(req.getYearCurrent(), lastStockDetail.getStockYear())) {
      searchEmp = searchEmployeeLoanNewV2(documentReq);
    } else {
      searchEmp = searchEmployeeLoanOldV2(documentReq);
    }

    val res = new ReportRes();
    res.setMonth(req.getMonthCurrent() + " " + req.getYearCurrent());
    res.setEmployeeCode(req.getEmpCode());
    res.setDepartmentName(resEmp.getDepartmentName());
    res.setFullName(resEmp.getFullName());
    if (searchEmp != null) {
      res.setStockAccumulate(
              searchEmp.getStockAccumulate() != null
                      ? Double.parseDouble(searchEmp.getStockAccumulate())
                      : 0.00);
      res.setStockDetailInstallment(
              searchEmp.getStockDetailInstallment() != null
                      ? searchEmp.getStockDetailInstallment()
                      : 0L);
      res.setStockValue(
              searchEmp.getStockValue() != null ? Double.parseDouble(searchEmp.getStockValue()) : 0.00);
      res.setInstallment(searchEmp.getInstallment() != null ? searchEmp.getInstallment() : 0L);
      res.setInterest(
              searchEmp.getInterestLoanLastMonth() != null
                      ? Long.parseLong(searchEmp.getInterestLoanLastMonth())
                      : 0L);
      res.setPrincipalBalance(
              searchEmp.getLoanBalance() != null
                      ? Double.parseDouble(searchEmp.getLoanBalance())
                      : 0.00);
      res.setTotalDeduction(
              (searchEmp.getLoanOrdinary() != null
                      ? Double.parseDouble(searchEmp.getLoanOrdinary())
                      : 0.00)
                      - (searchEmp.getInterestLoanLastMonth() != null
                      ? Double.parseDouble(searchEmp.getInterestLoanLastMonth())
                      : 0.00));
    }
    res.setTotalPrice(res.getStockValue() + res.getTotalDeduction() + res.getInterest());
    res.setTotalText("(" + ThaiNumeralsUtils.formatThaiWords(res.getTotalPrice()) + ")");

    return res;
  }

}
