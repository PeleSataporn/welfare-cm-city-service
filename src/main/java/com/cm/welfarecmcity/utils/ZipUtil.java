package com.cm.welfarecmcity.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.val;
import org.springframework.core.io.InputStreamResource;

public class ZipUtil {
  private ZipUtil() {}

  public static InputStreamResource createZipFile(
      List<InputStreamResource> pdfList, List<String> nameList, String pdfName) throws IOException {
    val zipOutput = new ByteArrayOutputStream();

    try (val zipOutputStream = new ZipOutputStream(zipOutput)) {
      for (int i = 0; i < pdfList.size(); i++) {
        addToZip(zipOutputStream, pdfList.get(i), pdfName, nameList.get(i), i);
      }
    }

    return new InputStreamResource(new ByteArrayInputStream(zipOutput.toByteArray()));
  }

  private static void addToZip(
      ZipOutputStream zipOutputStream,
      InputStreamResource pdf,
      String pdfName,
      String name,
      int index)
      throws IOException {
    val zipEntry = new ZipEntry(pdfName + "_" + name + "_" + (index + 1) + ".pdf");
    zipOutputStream.putNextEntry(zipEntry);

    try (val inputStream = pdf.getInputStream()) {
      val buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        zipOutputStream.write(buffer, 0, bytesRead);
      }
    }

    zipOutputStream.closeEntry();
  }

  public static InputStreamResource createZipFileR5(
      List<InputStreamResource> pdfList, List<String> nameList) throws IOException {
    val zipOutput = new ByteArrayOutputStream();

    try (val zipOutputStream = new ZipOutputStream(zipOutput)) {
      for (int i = 0; i < pdfList.size(); i++) {
        addToZipR5(zipOutputStream, pdfList.get(i), nameList.get(i));
      }
    }

    return new InputStreamResource(new ByteArrayInputStream(zipOutput.toByteArray()));
  }

  private static void addToZipR5(
      ZipOutputStream zipOutputStream, InputStreamResource pdf, String name) throws IOException {
    val zipEntry = new ZipEntry(name + ".pdf");
    zipOutputStream.putNextEntry(zipEntry);

    try (val inputStream = pdf.getInputStream()) {
      val buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        zipOutputStream.write(buffer, 0, bytesRead);
      }
    }

    zipOutputStream.closeEntry();
  }
}
