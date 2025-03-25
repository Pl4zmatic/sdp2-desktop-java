package repository;

import domein.machine.report.Image;

public class ImageDaoJpa extends GenericDaoJpa<Image>{

  public ImageDaoJpa() {
    super(Image.class);
  }
  
}
