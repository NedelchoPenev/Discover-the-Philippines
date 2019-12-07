package com.silenci0.philippines.service;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
  private final Cloudinary cloudinary;

  @Autowired
  public CloudinaryServiceImpl(Cloudinary cloudinary) {
    this.cloudinary = cloudinary;
  }

  @Override
  public String uploadImage(MultipartFile multipartFile) throws IOException {
    File file = File
        .createTempFile("temp-file", multipartFile.getOriginalFilename());
    multipartFile.transferTo(file);

    return this.cloudinary.uploader()
        .upload(file, new HashMap())
        .get("secure_url").toString();
  }

  @Override
  public Map uploadImageGetFullResponse(MultipartFile multipartFile) throws IOException {
    File file = File
      .createTempFile("temp-file", multipartFile.getOriginalFilename());
    multipartFile.transferTo(file);

    return this.cloudinary.uploader()
      .upload(file, new HashMap());
  }

  @Override
  public Map deleteImage(String public_id) throws IOException {
    return this.cloudinary.uploader().destroy(public_id, Map.of());
  }
}
