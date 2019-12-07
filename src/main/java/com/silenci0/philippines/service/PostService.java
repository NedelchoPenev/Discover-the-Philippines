package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.models.service.AllPostsServiceModel;
import com.silenci0.philippines.domain.models.service.PostAddServiceModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.security.Principal;

public interface PostService {

  void savePost(PostAddServiceModel serviceModel, Principal principal) throws IOException;

  Page<AllPostsServiceModel> findAllPageable(Pageable pageable);
}
