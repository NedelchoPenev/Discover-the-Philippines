package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.AllPicturesServiceModel;
import com.silenci0.philippines.domain.models.service.ImageAddServiceModel;
import com.silenci0.philippines.domain.models.service.ImageEditServiceModel;
import com.silenci0.philippines.domain.models.service.ImageServiceModel;
import com.silenci0.philippines.repository.ImageRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ImageServiceImplTest {

  @Mock
  private ImageRepository imageRepository;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private CloudinaryService cloudinaryService;

  @Mock
  private UserService userService;

  @InjectMocks
  private ImageServiceImpl imageService;

  private Image image;

  @Before
  public void setUp() throws Exception {
    this.image = new Image() {{
      setId("test id");
      setUploadDate(LocalDateTime.now());
      setProvince("Test Province");
      setPlace("Test Place");
    }};
  }

  @Test
  public void saveImage_shouldSaveCorrectImage(){
    when(this.imageRepository.saveAndFlush(this.image)).thenReturn(this.image);

    this.imageService.saveImage(this.image);

    verify(this.imageRepository, times(1)).saveAndFlush(this.image);
  }

  @Test
  public void findAll_shouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(image);
      add(image);
      add(image);
      add(image);
    }};

    when(this.imageRepository.findAll()).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ImageServiceModel.class));

    List<ImageServiceModel> all = this.imageService.findAll();

    Assert.assertEquals(images.size(), all.size());
  }

  @Test
  public void findAllByCategory_findByProvince_ShouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    ImageServiceModel serviceModel = new ImageServiceModel(){{
      setProvince("test");
    }};

    when(this.imageRepository.findAll()).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Map<String, Set<ImageServiceModel>> province = this.imageService.findAllByCategory("province");

    Assert.assertEquals(1, province.size());
  }

  @Test
  public void findAllByCategory_findByPlace_ShouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    ImageServiceModel serviceModel = new ImageServiceModel(){{
      setPlace("test");
    }};

    when(this.imageRepository.findAll()).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Map<String, Set<ImageServiceModel>> place = this.imageService.findAllByCategory("place");

    Assert.assertEquals(1, place.size());
  }

  @Test
  public void findAllByCategory_findByUploader_ShouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    when(this.imageRepository.findAll()).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ImageServiceModel.class));

    Map<String, Set<ImageServiceModel>> province = this.imageService.findAllByCategory("uploader");

    Assert.assertEquals(0, province.size());
  }

  @Test(expected = IllegalStateException.class)
  public void findAllByCategory_withIncorrectCategory_ShouldThrowException() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    when(this.imageRepository.findAll()).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ImageServiceModel.class));

    Map<String, Set<ImageServiceModel>> province = this.imageService.findAllByCategory("test");
  }

  @Test
  public void findAllFromCategoryByName_findByProvince_ShouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    when(this.imageRepository.findByProvince("test province")).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ImageServiceModel.class));

    List<ImageServiceModel> actual = this.imageService
      .findAllFromCategoryByName("province", "test province");

    Assert.assertEquals(images.size(), actual.size());
  }

  @Test
  public void findAllFromCategoryByName_findByPlace_ShouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    when(this.imageRepository.findByPlace("test place")).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ImageServiceModel.class));

    List<ImageServiceModel> actual = this.imageService
      .findAllFromCategoryByName("place", "test place");

    Assert.assertEquals(images.size(), actual.size());
  }

  @Test
  public void findAllFromCategoryByName_findByUploader_ShouldReturnCorrectSize() {
    List<Image> images = new ArrayList<>() {{
      add(image);
      add(new Image(){{
        setUploadDate(LocalDateTime.now());
      }});
    }};

    when(this.imageRepository.findByUploaderUsername("test uploader")).thenReturn(images);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ImageServiceModel.class));

    List<ImageServiceModel> actual = this.imageService
      .findAllFromCategoryByName("uploader", "test uploader");

    Assert.assertEquals(images.size(), actual.size());
  }

  @Test(expected = IllegalStateException.class)
  public void findAllFromCategoryByName_withIncorrectCategory_ShouldThrowException() {
    List<ImageServiceModel> actual = this.imageService
      .findAllFromCategoryByName("incorrect", "test uploader");
  }

  @Test
  public void saveImages_shouldSaveImages() throws IOException {
    Map response = new HashMap(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    when(this.imageRepository.saveAndFlush(this.image)).thenReturn(this.image);
    when(this.modelMapper.map(any(), any())).thenReturn(this.image);
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);
    when(this.userService.findUserByUsername(any())).thenReturn(mock(User.class));

    ImageAddServiceModel imageServiceModel = new ImageAddServiceModel(){{
      setImages(new ArrayList<>(){{
        add(mock(MultipartFile.class));
      }});
    }};

    this.imageService.saveImages(imageServiceModel, mock(Principal.class));

    verify(this.imageRepository, times(1)).saveAndFlush(this.image);
  }

  @Test
  public void findAllPageable_shouldCallFindAll() {
    Page<Image> categories = mock(Page.class);
    when(this.imageRepository.findAll(isA(Pageable.class))).thenReturn(categories);

    Pageable pageRequest = PageRequest.of(0, 4);
    Page<AllPicturesServiceModel> pageable = this.imageService.findAllPageable(pageRequest);

    verify(this.imageRepository, times(1)).findAll(pageRequest);
  }

  @Test
  public void deleteById_ShouldDeleteImage() throws IOException {
    this.image.setPublic_id("public_id");
    Map response = new HashMap(){{
      put("result", "test-result");
    }};

    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));
    when(this.cloudinaryService.deleteImage("public_id"))
      .thenReturn(response);
    String actual = this.imageService.deleteById("test_id");

    Assert.assertEquals(response.get("result"), actual);
  }

  @Test
  public void findById_shouldFindCorrect() {
    ImageEditServiceModel serviceModel = new ImageEditServiceModel(){{
      setId("test id");
      setProvince("Test Province");
      setPlace("Test Place");
    }};

    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    ImageEditServiceModel actual = this.imageService.findById("test_id");

    Assert.assertEquals(this.image.getId(), actual.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findById_shouldThrowException() {
    ImageEditServiceModel serviceModel = new ImageEditServiceModel(){{
      setId("test id");
      setProvince("Test Province");
      setPlace("Test Place");
    }};

    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    this.imageService.findById("wrong_id");
  }

  @Test
  public void findByIdWithOutMap_shouldFindCorrect() {
    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));

    Image actual = this.imageService.findByIdWithoutMap("test_id");

    Assert.assertEquals(this.image.getId(), actual.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findByIdWithOutMap_withWrongId_shouldThrowException() {
    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));

    Image actual = this.imageService.findByIdWithoutMap("wrong_id");
  }

  @Test
  public void editImage_withoutPlace_shouldSetNewPlaceAsPlace() {
    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));
    when(this.imageRepository.saveAndFlush(this.image)).thenReturn(this.image);

    ImageEditServiceModel serviceModel = new ImageEditServiceModel(){{
      setId("test id");
      setPlaceNew("new place");
    }};

    this.imageService.editImage("test_id", serviceModel);

    ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);

    verify(this.imageRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(serviceModel.getPlaceNew(), captor.getValue().getPlace());
  }

  @Test
  public void editImage_withPlace_shouldSetPlace() {
    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));
    when(this.imageRepository.saveAndFlush(this.image)).thenReturn(this.image);

    ImageEditServiceModel serviceModel = new ImageEditServiceModel(){{
      setId("test id");
      setPlace("new place");
    }};

    this.imageService.editImage("test_id", serviceModel);

    ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);

    verify(this.imageRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(serviceModel.getPlace(), captor.getValue().getPlace());
  }

  @Test(expected = IllegalArgumentException.class)
  public void editImage_withoutWrongId_shouldThrowException() {
    when(this.imageRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.image));
    when(this.imageRepository.saveAndFlush(this.image)).thenReturn(this.image);

    ImageEditServiceModel serviceModel = new ImageEditServiceModel(){{
      setId("test id");
      setPlaceNew("new place");
    }};

    this.imageService.editImage("wrong_id", serviceModel);
  }
}