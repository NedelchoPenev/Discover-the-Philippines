package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.entities.ThingsToDo;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.repository.ThingsToDoRepository;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class ThingsToDoServiceImplTest {

  @Mock
  private ThingsToDoRepository thingsToDoRepository;

  @Mock
  private ImageService imageService;

  @Mock
  private CloudinaryService cloudinaryService;

  @Mock
  private UserService userService;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private ThingsToDoServiceImpl thingsToDoService;

  private ThingsToDo thingsToDo;

  @Before
  public void setUp() throws Exception {
    this.thingsToDo = new ThingsToDo(){{
      setId("test_id");
      setName("test_name");
      setMainImageUrl("test_url");
      setDateAdded(LocalDateTime.now());
      setImagesUrls(new ArrayList<>(){{
        add(new Image(){{
          setId("image_id");
          setUrl("image_url");
        }});
        add(new Image(){{
          setId("image_id1");
          setUrl("image_url1");
        }});
      }});
    }};
  }

  @Test
  public void saveThing_shouldSaveThing() throws IOException {
    ThingsToDoAddServiceModel serviceModel = new ThingsToDoAddServiceModel(){{
      setImages(new ArrayList<>(){{
        add(mock(MultipartFile.class));
      }});
    }};

    Map response = new HashMap<>(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    when(this.modelMapper.map(any(), any())).thenReturn(this.thingsToDo);
    when(this.userService.findUserByUsername(anyString())).thenReturn(mock(User.class));
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);

    this.thingsToDoService.saveThing(serviceModel, mock(Principal.class));

    verify(this.thingsToDoRepository, times(1)).saveAndFlush(this.thingsToDo);
  }

  @Test
  public void findAllPageable_shouldCallFindAll() {
    Pageable pageRequest = PageRequest.of(0, 4);
    List<ThingsToDo> posts = new ArrayList<>(){{
      add(thingsToDo);
    }};

    ThingsToDoMainImageServiceModel serviceModel = new ThingsToDoMainImageServiceModel(){{
      setName("test_name");
    }};

    Page<ThingsToDo> pagedPosts = new PageImpl(posts);
    when(this.thingsToDoRepository.findAll(pageRequest)).thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Page<ThingsToDoMainImageServiceModel> all = this.thingsToDoService.findAllPageable(pageRequest);

    verify(this.thingsToDoRepository, times(1)).findAll(pageRequest);
    Assert.assertEquals(this.thingsToDo.getName(), all.getContent().get(0).getName());
  }

  @Test
  public void findAllProvinces_shouldReturnAllProvinceSorted() {
    List<String> provinces = new ArrayList<>(){{
      add("ccc");
      add("bbb");
      add("aaa");
    }};

    when(this.thingsToDoRepository.getAllProvinces()).thenReturn(provinces);

    List<String> actual = this.thingsToDoService.findAllProvinces();

    Assert.assertEquals("aaa", actual.get(0));
    Assert.assertEquals(provinces.size(), actual.size());
  }

  @Test
  public void findByProvincePageable_shouldCallFindByProvince() {
    Pageable pageRequest = PageRequest.of(0, 4);
    List<ThingsToDo> posts = new ArrayList<>(){{
      add(thingsToDo);
    }};

    ThingsToDoMainImageServiceModel serviceModel = new ThingsToDoMainImageServiceModel(){{
      setName("test_name");
    }};

    Page<ThingsToDo> pagedPosts = new PageImpl(posts);
    when(this.thingsToDoRepository.findByProvince("province1", pageRequest)).thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Page<ThingsToDoMainImageServiceModel> all =
      this.thingsToDoService.findByProvincePageable("province1", pageRequest);

    verify(this.thingsToDoRepository, times(1))
      .findByProvince("province1", pageRequest);
    Assert.assertEquals(this.thingsToDo.getName(), all.getContent().get(0).getName());
  }

  @Test
  public void findByNamePageable_shouldCallFindByNameContains() {
    Pageable pageRequest = PageRequest.of(0, 4);
    List<ThingsToDo> posts = new ArrayList<>(){{
      add(thingsToDo);
    }};

    ThingsToDoMainImageServiceModel serviceModel = new ThingsToDoMainImageServiceModel(){{
      setName("test_name");
    }};

    Page<ThingsToDo> pagedPosts = new PageImpl(posts);
    when(this.thingsToDoRepository.findByNameContains("province1", pageRequest)).thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    Page<ThingsToDoMainImageServiceModel> all =
      this.thingsToDoService.findByNamePageable("province1", pageRequest);

    verify(this.thingsToDoRepository, times(1))
      .findByNameContains("province1", pageRequest);
    Assert.assertEquals(this.thingsToDo.getName(), all.getContent().get(0).getName());
  }

  @Test
  public void deleteById_shouldCallDeleteById() {
    doNothing().when(this.thingsToDoRepository).deleteById("id");

    this.thingsToDoService.deleteById("id");

    verify(this.thingsToDoRepository, times(1)).deleteById("id");
  }

  @Test
  public void findByIdEdit_shouldFindCorrect() {
    ThingsToDoDeleteImageServiceModel serviceModel = new ThingsToDoDeleteImageServiceModel(){{
      setId("test_id");
    }};

    when(this.thingsToDoRepository.findById("test_id"))
      .thenReturn(Optional.ofNullable(this.thingsToDo));
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ThingsToDoDeleteImageServiceModel.class));

    this.thingsToDoService.findByIdEdit("test_id");

    Assert.assertEquals(this.thingsToDo.getId(), serviceModel.getId());
  }

  @Test
  public void deleteImageById_withDifferentUrl_shouldDeleteImage() {
    Image image = new Image(){{
      setId("image_id");
      setUrl("image_url");
    }};

    when(this.thingsToDoRepository.findById("test_id"))
      .thenReturn(Optional.ofNullable(this.thingsToDo));
    when(this.imageService.findByIdWithoutMap("image_id"))
      .thenReturn(image);

    String imageId = this.thingsToDoService.deleteImageById("test_id", "image_id");

    Assert.assertEquals(image.getId(), imageId);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteImageById_withSameUrl_shouldThrowException() {
    Image image = new Image(){{
      setId("image_id");
      setUrl("test_url");
    }};

    when(this.thingsToDoRepository.findById("test_id"))
      .thenReturn(Optional.ofNullable(this.thingsToDo));
    when(this.imageService.findByIdWithoutMap("image_id"))
      .thenReturn(image);

    String imageId = this.thingsToDoService.deleteImageById("test_id", "image_id");
  }

  @Test
  public void editThingToDo_shouldEditCorrect() throws IOException {
    ThingsToDoEditServiceModel serviceModel = new ThingsToDoEditServiceModel(){{
      setMainImageUrl(null);
      setImages(new ArrayList<>(){{
        add(mock(MultipartFile.class));
      }});
    }};

    Map response = new HashMap<>(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    when(this.thingsToDoRepository.findById("test_id"))
      .thenReturn(Optional.ofNullable(this.thingsToDo));
    when(this.userService.findUserByUsername(anyString())).thenReturn(mock(User.class));
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);

    this.thingsToDoService.editThingToDo("test_id", serviceModel, mock(Principal.class));

    ArgumentCaptor<ThingsToDo> captor = ArgumentCaptor.forClass(ThingsToDo.class);

    verify(this.thingsToDoRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(this.thingsToDo.getId(), captor.getValue().getId());
  }

  @Test
  public void findById_shouldFindCorrectThing() {
    ThingsToDoServiceModel serviceModel = new ThingsToDoServiceModel(){{
      setId("test_id");
    }};

    when(this.thingsToDoRepository.findById("test_id"))
      .thenReturn(Optional.ofNullable(this.thingsToDo));
    when(this.modelMapper.map(any(), any())).thenReturn(serviceModel);

    ThingsToDoServiceModel actual = this.thingsToDoService.findById("test_id");

    Assert.assertEquals(this.thingsToDo.getId(), actual.getId());
  }

  @Test
  public void findNewestTakeFour_shouldReturnCorrectSize() {
    List<ThingsToDo> thingsToDoList = new ArrayList<>(){{
      add(thingsToDo);
      add(new ThingsToDo(){{
        setId("thingToDo_2");
        setDateAdded(LocalDateTime.now().minusDays(1));
      }});
      add(new ThingsToDo(){{
        setId("thingToDo_3");
        setDateAdded(LocalDateTime.now().minusDays(2));
      }});
      add(new ThingsToDo(){{
        setId("thingToDo_4");
        setDateAdded(LocalDateTime.now().minusDays(3));
      }});
      add(new ThingsToDo(){{
        setId("thingToDo_5");
        setDateAdded(LocalDateTime.now().minusDays(4));
      }});
    }};


    when(this.thingsToDoRepository.findAll()).thenReturn(thingsToDoList);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(ThingsToDoMainImageServiceModel.class));

    List<ThingsToDoMainImageServiceModel> actual = this.thingsToDoService.findNewestTakeFour();

    Assert.assertEquals(4, actual.size());
  }
}