package domein.machine.report;

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
public class Image {
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
}
