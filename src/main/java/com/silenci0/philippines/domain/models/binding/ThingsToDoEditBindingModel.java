package com.silenci0.philippines.domain.models.binding;

import com.silenci0.philippines.domain.models.service.ImageDeleteServiceModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ThingsToDoEditBindingModel {
  private String id;
  private String name;
  private List<MultipartFile> images;
  private List<ImageDeleteServiceModel> imagesUrls;
  private String mainImageUrl;
  private String province;
  private String overview;
  private String externalLink;

  public ThingsToDoEditBindingModel() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<MultipartFile> getImages() {
    return images;
  }

  public void setImages(List<MultipartFile> images) {
    this.images = images;
  }

  public List<ImageDeleteServiceModel> getImagesUrls() {
    return imagesUrls;
  }

  public void setImagesUrls(List<ImageDeleteServiceModel> imagesUrls) {
    this.imagesUrls = imagesUrls;
  }

  public String getMainImageUrl() {
    return mainImageUrl;
  }

  public void setMainImageUrl(String mainImageUrl) {
    this.mainImageUrl = mainImageUrl;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getExternalLink() {
    return externalLink;
  }

  public void setExternalLink(String externalLink) {
    this.externalLink = externalLink;
  }
}
