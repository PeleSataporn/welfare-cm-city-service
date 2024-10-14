package com.cm.welfarecmcity.utils.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.val;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtils {
  public static byte[] resizeImage(MultipartFile file, int width) throws IOException {
    val originalImage = ImageIO.read(file.getInputStream());
    val originalWidth = originalImage.getWidth();
    val originalHeight = originalImage.getHeight();
    val height = (originalHeight * width) / originalWidth;

    val resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    val bufferedResizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    val g2d = bufferedResizedImage.createGraphics();
    g2d.drawImage(resizedImage, 0, 0, null);
    g2d.dispose();

    val byteArray = new ByteArrayOutputStream();
    ImageIO.write(bufferedResizedImage, "jpg", byteArray);
    return byteArray.toByteArray();
  }
}
