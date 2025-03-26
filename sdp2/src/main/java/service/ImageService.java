package service;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.imageio.ImageIO;

import domein.machine.report.Image;
import domein.machine.report.Report;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

import lombok.NoArgsConstructor;
import repository.ImageDaoJpa;
import repository.ReportDaoJpa;

@NoArgsConstructor
public class ImageService {

  private ImageDaoJpa imageDaoJpa;
  private ReportService reportService;

  public byte[] getImageBytesFromPath(String path) {
    try {
      return Files.readAllBytes(Path.of(path));
    } catch (Exception e) {
      System.out.printf("%s\n%s%s\n%s\n", "=".repeat(20), "Getting image file failed, from path: ", path, "=".repeat(20));
      e.printStackTrace();
    }

    return null;
  }

  public String getImageNameFromPath(String path) {
    Path pathName = Path.of(path);
    return pathName.getFileName().toString();
  }

  public String getImageExtensionFromPath(String path) {
    String imageName = getImageNameFromPath(path);
    return imageName.substring(imageName.indexOf("."));
  }

  public void download(String destination, Image image) {
    String filePath = String.format("%s%s%s", destination, image.getName(), image.getExtension());
    try {

      FileOutputStream outStream  = new FileOutputStream(filePath);
      outStream.write(image.getData());
      outStream.close();
    } catch (IOException e) {
      System.out.printf("%s\n%s%s\n%s\n", "=".repeat(20), "Writing file failed, to: ", filePath, "=".repeat(20));
      e.printStackTrace();
    }
  }

  public List<Image> getAllImagesByReportId(int id) {
    return reportService.getReportById(id).getImages();
  }

  public void addImage() {
    reportService.addReport(new Report());
  }

}
