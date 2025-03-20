package service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ImageService {

  public byte[] getImageBytesFromPath(String path) {
    try {
      return Files.readAllBytes(Path.of(path));
    } catch (Exception e) {
      System.out.printf("%s\n%s%s\n%s\n", "=".repeat(20), "Getting image file failed, from path: ", path, "=".repeat(20));
      e.printStackTrace();
    }

    return null;
  }

  public void download(String destination, String fileName, String extension, byte[] imageBytes) {
    String filePath = String.format("%s%s%s", destination, fileName, extension);
    try {
      FileOutputStream outStream  = new FileOutputStream(filePath);
      outStream.write(imageBytes);
      outStream.close();
    } catch (IOException e) {
      System.out.printf("%s\n%s%s\n%s\n", "=".repeat(20), "Writing file failed, to: ", filePath, "=".repeat(20));
      e.printStackTrace();
    }
  }
}
