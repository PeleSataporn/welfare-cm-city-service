package com.cm.welfarecmcity.api.document;

import com.cm.welfarecmcity.api.document.model.DocumentRes;
import com.cm.welfarecmcity.dto.DocumentDto;
import com.cm.welfarecmcity.dto.bean.AppPropertiesConfigBean;
import com.cm.welfarecmcity.dto.bean.FileConfigBean;
import com.cm.welfarecmcity.exception.entity.DocumentException;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import lombok.val;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentsService {

  private static final Logger log = LoggerFactory.getLogger(DocumentsService.class);
  private final AppPropertiesConfigBean appProperties;

  @Autowired
  public DocumentsService(AppPropertiesConfigBean appProperties) {
    this.appProperties = appProperties;
  }

  @Autowired private DocumentsRepository documentRepository;

  public long saveDocument(String name, MultipartFile pdfFile) throws IOException {
    val document = new DocumentDto();
    document.setName(name);
    document.setPdfFile(pdfFile.getBytes());

    return documentRepository.save(document).getId();
  }

  public DocumentDto getDocument(Long id) {
    return documentRepository
        .findById(id)
        .orElseThrow(() -> new DocumentException("Document not found"));
  }

  public List<DocumentRes> search() {
    return documentRepository.findAllByOrderByCreateDateDesc().stream()
        .map(
            doc ->
                DocumentRes.builder()
                    .id(doc.getId())
                    .name(doc.getName())
                    .createDate(doc.getCreateDate())
                    .size(doc.getPdfFile() != null ? (long) doc.getPdfFile().length : 0)
                    .build())
        .collect(Collectors.toList());
  }

  public void deleted(Long id) throws IOException {
    val doc = getDocument(id);
    documentRepository.deleteById(doc.getId());
  }

  public List<FileConfigBean> getFileTestForHost(FileConfigBean req) {
    String host = "103.253.72.208";
    String user = "root";
    String password = "Tri@#tirL";
    String remoteDirectoryPath =
        appProperties.getPathFile()
            + "/"
            + req.getMonth()
            + "/"
            + req.getYear(); // Remote folder containing PDF files
    List<FileConfigBean> textAllFile = new ArrayList<>();

    JSch jsch = new JSch();
    Session session = null;
    ChannelSftp sftpChannel = null;

    try {
      // Set up SSH session
      session = jsch.getSession(user, host, 22);
      session.setPassword(password);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      // Open an SFTP channel
      Channel channel = session.openChannel("sftp");
      channel.connect();
      sftpChannel = (ChannelSftp) channel;

      // List all files in the remote directory
      Vector<ChannelSftp.LsEntry> fileList = sftpChannel.ls(remoteDirectoryPath);

      // Loop through the files and process only PDFs
      for (ChannelSftp.LsEntry entry : fileList) {
        String fileName = entry.getFilename();
        if (fileName.endsWith(".pdf")) {
          System.out.println("Processing PDF: " + fileName);

          // Open an InputStream for the remote PDF file
          String remoteFilePath = remoteDirectoryPath + "/" + fileName;
          InputStream inputStream = sftpChannel.get(remoteFilePath);

          // Load the PDF document using PDFBox
          // PDDocument document = PDDocument.load(inputStream);

          // Use PDFTextStripper to extract text from the PDF
          // PDFTextStripper pdfStripper = new PDFTextStripper();
          // String pdfContent = pdfStripper.getText(document);

          // Print the content or process it further
          System.out.println("Content of PDF: " + fileName);
          // System.out.println(pdfContent);

          // Close the PDF document and input stream

          // ByteArrayInputStream copiedStream = copyInputStream(inputStream);
          byte[] fileByte = convertInputStreamToByteArray(inputStream);

          // byte[] fileByte = inputStream.readAllBytes();

          FileConfigBean fileConfigBean = new FileConfigBean();
          fileConfigBean.setFileData(fileByte);
          fileConfigBean.setFileName(fileName);
          textAllFile.add(fileConfigBean);

          // document.close();
          inputStream.close();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      // Disconnect the session and channel
      if (sftpChannel != null) {
        sftpChannel.exit();
      }
      if (session != null) {
        session.disconnect();
      }
    }
    return textAllFile;
  }

  public List<FileConfigBean> getFile(FileConfigBean req) {
    // Path to the directory containing PDF files
    String directoryPath =
        "D:\\Project_Icoop\\file-test-add\\" + req.getMonth() + "\\" + req.getYear();
    String directoryPathToSever =
        appProperties.getPathFile() + "/" + req.getMonth() + "/" + req.getYear();
    // appProperties.getPathFile() + "/"  + month + "/" + year, "D:\\Project_Icoop\\file-test-add\\"
    // + month + "\\" + year
    List<FileConfigBean> textAllFile = new ArrayList<>();

    log.debug(" directoryPathToSever : {} ", directoryPathToSever);

    File directory = new File(directoryPathToSever);

    // Ensure it's a valid directory
    if (directory.isDirectory()) {
      // Loop through all files in the directory
      for (File file : Objects.requireNonNull(directory.listFiles())) {
        if (file.isFile() && file.getName().endsWith(".pdf")) {
          System.out.println("Processing PDF: " + file.getName());

          log.debug(" Processing PDF: {} ", file.getName());

          try (InputStream inputStream = new FileInputStream(file)) {
            // Load the PDF document
            // PDDocument document = PDDocument.load(file);

            // Extract text from the PDF
            // PDFTextStripper pdfStripper = new PDFTextStripper();
            // String text = pdfStripper.getText(document);

            // Print the content of the PDF
            // System.out.println(text);

            System.out.println(file.getName());

            // ByteArrayInputStream copiedStream = copyInputStream(inputStream);
            byte[] fileByte = convertInputStreamToByteArray(inputStream);

            FileConfigBean fileConfigBean = new FileConfigBean();
            fileConfigBean.setFileData(fileByte);
            fileConfigBean.setFileName(file.getName());
            fileConfigBean.setMonth(req.getMonth());
            fileConfigBean.setYear(req.getYear());
            textAllFile.add(fileConfigBean);

            // Close the document
            // document.close();

          } catch (Exception e) {
            System.err.println("Error reading PDF file: " + file.getName());
            e.printStackTrace();
          }
        }
      }
    } else {
      System.out.println("The specified path is not a directory.");
      textAllFile.add(new FileConfigBean());
    }
    return textAllFile;
  }

  public static byte[] convertInputStreamToByteArray(InputStream inputStream) throws Exception {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[1024];
    int bytesRead;

    while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, bytesRead);
    }

    return buffer.toByteArray();
  }

  public static ByteArrayInputStream copyInputStream(InputStream inputStream) throws Exception {
    byte[] byteArray = convertInputStreamToByteArray(inputStream);
    return new ByteArrayInputStream(byteArray); // Return a new ByteArrayInputStream
  }

  public Map<String, String> addFile(MultipartFile file, String month, String year) {
    String directoryPath = "D:\\Project_Icoop\\file-test-add\\" + month + "\\" + year;
    String directoryPathToSever = appProperties.getPathFile() + "/" + month + "/" + year;
    // appProperties.getPathFile() + "/"  + month + "/" + year, "D:\\Project_Icoop\\file-test-add\\"
    // + month + "\\" + year
    Map<String, String> response = new HashMap<>();
    if (!file.isEmpty()) {
      try {

        // Create the directory if it doesn't exist
        File uploadDir = new File(directoryPathToSever);
        if (!uploadDir.exists()) {
          uploadDir.mkdirs(); // Create directories if they don't exist
        }

        // Delete old files in the directory
        //                File[] existingFiles = uploadDir.listFiles();
        //                if (existingFiles != null) {
        //                    for (File existingFile : existingFiles) {
        //                        if (existingFile.isFile()) {
        //                            existingFile.delete(); // Delete old files
        //                        }
        //                    }
        //                }

        // Specify the path where the file will be saved
        String filePath = directoryPathToSever + File.separator + file.getOriginalFilename();
        File destFile = new File(filePath);
        file.transferTo(destFile);
        response.put("message", "File uploaded successfully!");
      } catch (IOException e) {
        response.put("message", "Failed to upload file!");
        e.printStackTrace();
      }
    } else {
      response.put("message", "No file selected!");
    }
    return response;
  }

  public Map<String, String> addFile2(MultipartFile file, String month, String year) {
    String directoryPath = "D:\\Project_Icoop\\file-test-add\\" + month + "\\" + year;
    String directoryPathToServer = appProperties.getPathFile() + "/" + month + "/" + year;
    Map<String, String> response = new HashMap<>();

    if (!file.isEmpty()) {
      try {
        // Create the directory if it doesn't exist
        File uploadDir = new File(directoryPathToServer);
        if (!uploadDir.exists()) {
          uploadDir.mkdirs(); // Create directories if they don't exist
        }

        // Specify the path where the file will be saved
        Path destPath =
            new File(directoryPathToServer, Objects.requireNonNull(file.getOriginalFilename()))
                .toPath();

        // Copy the file to the destination path
        Files.copy(file.getInputStream(), destPath, StandardCopyOption.REPLACE_EXISTING);
        response.put("message", "File uploaded successfully!");
      } catch (IOException e) {
        response.put("message", "Failed to upload file!");
        e.printStackTrace();
      }
    } else {
      response.put("message", "No file selected!");
    }

    return response;
  }

  public List<List<String>> readFileExcelForLoan() {
    String filePath = "excel-import-loan/test-impoer-data-loan.xlsx";
    InputStream fileStream = getClass().getClassLoader().getResourceAsStream(filePath);

    if (fileStream == null) {
      throw new RuntimeException("File not found in resources: " + filePath);
    }

    return readExcelColumnsFromResource(fileStream);
  }

  public static List<List<String>> readExcelColumnsFromResource(InputStream fileStream) {
    List<List<String>> dataList = new ArrayList<>();

    try (Workbook workbook = WorkbookFactory.create(fileStream)) {
      Sheet sheet = workbook.getSheetAt(0); // Read first sheet

      if (sheet == null || sheet.getRow(0) == null) {
        System.out.println("The sheet is empty.");
        return dataList;
      }

      // Read each row and store values in a list
      for (Row row : sheet) {
        List<String> rowData = new ArrayList<>();
        for (Cell cell : row) {
          String cellValue = getCellValue(cell);
          if (!cellValue.isEmpty()) {
            rowData.add(cellValue);
          }
        }
        if (!rowData.isEmpty()) {
          dataList.add(rowData);
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataList;
  }


  private static String getCellValue(Cell cell) {
    if (cell == null) return "";
    switch (cell.getCellType()) {
      case STRING:
        return cell.getStringCellValue().trim();
      case NUMERIC:
        // Check if it's a whole number (integer)
        double numericValue = cell.getNumericCellValue();
        if (numericValue == Math.floor(numericValue)) {
          return String.valueOf((long) numericValue); // Convert to long to remove .0
        } else {
          return String.valueOf(numericValue);
        }
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case FORMULA:
        return cell.getCellFormula();
      default:
        return "";
    }
  }

}
