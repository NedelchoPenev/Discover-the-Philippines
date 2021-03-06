package com.silenci0.philippines.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {

  String uploadImage(MultipartFile multipartFile) throws IOException;

  Map uploadImageGetFullResponse(MultipartFile multipartFile) throws IOException;

  Map deleteImage(String public_id) throws IOException;
}
