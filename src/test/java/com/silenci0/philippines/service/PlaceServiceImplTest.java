package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Image;
import com.silenci0.philippines.domain.entities.Place;
import com.silenci0.philippines.domain.models.service.AllPlacesServiceModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import com.silenci0.philippines.domain.models.service.UserServiceModel;
import com.silenci0.philippines.repository.PlaceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class PlaceServiceImplTest {
  @InjectMocks
  private PlaceServiceImpl placeService;

  @Mock
  private PlaceRepository placeRepository;

  @Mock
  private UserService userService;

  @Mock
  private ModelMapper modelMapper;

  @Mock
  private CloudinaryService cloudinaryService;

  private Place place;

  @Before
  public void setUp() throws Exception {
    this.place = new Place(){{
      setId("test_id");
      setName("first");
      setHeaderImage(mock(Image.class));
      setArticle("test article");
      setInfo("test info");
      setDateAdded(LocalDateTime.now());
    }};
  }

  @Test
  public void savePlace_shouldSavePlace() throws IOException {
    Map response = new HashMap(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    when(this.placeRepository.saveAndFlush(this.place)).thenReturn(this.place);
    when(this.modelMapper.map(any(), any())).thenReturn(this.place);
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);

    PlaceServiceModel serviceModel = new PlaceServiceModel(){{
      setId("test_id");
      setName("test name");
      setHeaderImage(mock(MultipartFile.class));
      setArticle("test article");
      setInfo("test info");
    }};

    this.placeService.savePlace(serviceModel, mock(Principal.class));

    verify(this.placeRepository, times(1)).saveAndFlush(this.place);
  }

  @Test
  public void findAll_shouldReturnCorrectSize() {
    List<Place> places = new ArrayList<>() {{
      add(place);
      add(place);
      add(place);
      add(place);
      add(place);
    }};

    when(this.placeRepository.findAll()).thenReturn(places);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(AllPlacesServiceModel.class));

    List<AllPlacesServiceModel> all = this.placeService.findAll();

    Assert.assertEquals(places.size(), all.size());
  }

  @Test
  public void findNewestTakeFour_shouldReturnCorrectSizeAndOrder() {
    List<Place> places = new ArrayList<>() {{
      add(place);
      add(new Place(){{
        setName("second");
        setDateAdded(LocalDateTime.now().minusDays(1));
      }});
      add(new Place(){{
        setName("third");
        setDateAdded(LocalDateTime.now().minusDays(2));
      }});
      add(new Place(){{
        setName("fourth");
        setDateAdded(LocalDateTime.now().minusDays(3));
      }});
      add(new Place(){{
        setName("fifth");
        setDateAdded(LocalDateTime.now().minusDays(4));
      }});
      add(new Place(){{
        setName("sixth");
        setDateAdded(LocalDateTime.now().minusDays(5));
      }});
    }};

    AllPlacesServiceModel serviceModel = new AllPlacesServiceModel(){{
      setName("first");
    }};

    when(this.placeRepository.findAll()).thenReturn(places);
    when(this.modelMapper.map(this.place, AllPlacesServiceModel.class))
      .thenReturn(serviceModel);

    List<AllPlacesServiceModel> all = this.placeService.findNewestTakeFour();

    Assert.assertEquals(4, all.size());
    Assert.assertEquals(this.place.getName(), all.get(0).getName());
  }

  @Test
  public void findById_withCorrectId_shouldReturnCorrect() {
    PlaceServiceModel serviceModel = new PlaceServiceModel(){{
      setId("test_id");
    }};

    when(this.placeRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.place));
    when(this.modelMapper.map(this.place, PlaceServiceModel.class))
      .thenReturn(serviceModel);

    PlaceServiceModel actual = this.placeService.findById("test_id");

    Assert.assertEquals(serviceModel.getId(), actual.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void findById_withWrongId_shouldThrowException() {
    when(this.placeRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.place));
    when(this.modelMapper.map(any(), any()))
      .thenReturn(mock(PlaceServiceModel.class));

    PlaceServiceModel actual = this.placeService.findById("wrong_id");
  }

  @Test
  public void findAllPageable_shouldCallFindAll() {
    Page<Place> places = mock(Page.class);
    when(this.placeRepository.findAll(isA(Pageable.class))).thenReturn(places);

    Pageable pageRequest = PageRequest.of(0, 4);
    Page<AllPlacesServiceModel> pageable = this.placeService.findAllPageable(pageRequest);

    verify(this.placeRepository, times(1)).findAll(pageRequest);
  }

  @Test
  public void findByNamePlace_shouldCallFindByNameStartingWith() {
    Page<Place> places = mock(Page.class);
    Pageable pageRequest = PageRequest.of(0, 4);

    when(this.placeRepository.findByNameStartingWith("test name", pageRequest))
      .thenReturn(places);

    Page<AllPlacesServiceModel> pageable = this.placeService
      .findByNamePlace("test name", pageRequest);

    verify(this.placeRepository, times(1))
      .findByNameStartingWith("test name", pageRequest);
  }

  @Test
  public void deleteById_shouldDeletePlace() {
    doNothing().when(this.placeRepository).deleteById("test_id");

    this.placeService.deleteById("test_id");

    verify(this.placeRepository, times(1)).deleteById("test_id");
  }

  @Test
  public void editPlace() throws IOException {
    Map response = new HashMap(){{
      put("secure_url", "test-url");
      put("public_id", "test-id");
    }};

    when(this.placeRepository.findById("test_id")).thenReturn(Optional.ofNullable(this.place));
    when(this.cloudinaryService.uploadImageGetFullResponse(any()))
      .thenReturn(response);
    when(this.userService.findUserByUserName(any())).thenReturn(mock(UserServiceModel.class));
    when(this.placeRepository.saveAndFlush(this.place)).thenReturn(this.place);

    PlaceServiceModel serviceModel = new PlaceServiceModel(){{
      setId("test_id");
      setName("test name");
      setHeaderImage(mock(MultipartFile.class));
      setArticle("test article");
      setInfo("test info");
    }};

    this.placeService.editPlace("test_id", serviceModel, mock(Principal.class));

    verify(this.placeRepository, times(1)).saveAndFlush(this.place);
  }
}