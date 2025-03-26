package domein.machine.report;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Image implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private int imageId;

  @Lob
  @Column(nullable = false)
  @Setter
  private byte[] data;

  @Column(nullable = false)
  @Setter
  private String name;

  @Column(nullable = false)
  @Setter
  private String extension;

  public Image(byte[] data, String name, String extension) {
    setData(data);
    setName(name);
    setExtension(extension);
  }

  public String getNameFile() {
    Matcher fileNameMatcher = Pattern.compile("[^/\\\\]+$").matcher(name);
    if(fileNameMatcher.find()) {
      return fileNameMatcher.group(0);
    }
    return null;
  }
}
