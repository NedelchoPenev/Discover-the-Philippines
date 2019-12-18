package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Role;
import com.silenci0.philippines.domain.entities.User;
import com.silenci0.philippines.domain.models.service.*;
import com.silenci0.philippines.repository.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
@SpringBootTest
public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleService roleService;
  @Mock
  private ModelMapper modelMapper;
  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private UserServiceImpl userService;

  private User user;

  @Before
  public void setUp() throws Exception {
    this.user = new User() {{
      setId("user_id");
      setUsername("Username");
      setPassword("1234");
      setRegistrationDate(LocalDate.now());
      setAuthorities(new HashSet<>() {{
        add(new Role() {{
          setAuthority("ROLE_USER");
        }});
        add(new Role() {{
          setAuthority("ROLE_CREATOR");
        }});
      }});
    }};
  }

  @Test
  public void registerUser_whenThereIsNoUsers_shouldAddAllRoles() {
    UserServiceModel serviceModel = new UserServiceModel() {{
      setUsername("Username");
      setPassword("1234");
    }};

    Set<RoleServiceModel> allRoles = new HashSet<>() {{
      add(new RoleServiceModel() {{
        setAuthority("ROLE_USER");
      }});
      add(new RoleServiceModel() {{
        setAuthority("ROLE_CREATOR");
      }});
      add(new RoleServiceModel() {{
        setAuthority("ROLE_ADMIN");
      }});
      add(new RoleServiceModel() {{
        setAuthority("ROLE_ROOT");
      }});
    }};

    when(this.userRepository.count()).thenReturn(0L);
    when(this.roleService.findAllRoles()).thenReturn(allRoles);
    when(this.modelMapper.map(serviceModel, User.class)).thenReturn(this.user);
    when(this.bCryptPasswordEncoder.encode(serviceModel.getPassword())).thenReturn("encrypted");
    when(this.modelMapper.map(this.user, UserServiceModel.class)).thenReturn(serviceModel);
    when(this.userRepository.saveAndFlush(this.user)).thenReturn(this.user);

    UserServiceModel actual = this.userService.registerUser(serviceModel);

    Assert.assertEquals(this.user.getUsername(), actual.getUsername());
    Assert.assertEquals(4, actual.getAuthorities().size());
  }

  @Test
  public void registerUser_whenThereAlreadyUsers_shouldSetRoleToUser() {
    UserServiceModel serviceModel = new UserServiceModel() {{
      setUsername("Username");
      setPassword("1234");
    }};

    when(this.userRepository.count()).thenReturn(2L);
    when(this.roleService.findByAuthority("ROLE_USER")).thenReturn(mock(RoleServiceModel.class));
    when(this.modelMapper.map(serviceModel, User.class)).thenReturn(this.user);
    when(this.bCryptPasswordEncoder.encode(serviceModel.getPassword())).thenReturn("encrypted");
    when(this.modelMapper.map(this.user, UserServiceModel.class)).thenReturn(serviceModel);
    when(this.userRepository.saveAndFlush(this.user)).thenReturn(this.user);

    UserServiceModel actual = this.userService.registerUser(serviceModel);

    Assert.assertEquals(this.user.getUsername(), actual.getUsername());
    Assert.assertEquals(1, actual.getAuthorities().size());
  }

  @Test
  public void loadUserByUsername_withExistingUsername_shouldReturnCorrect() {
    when(this.userRepository.findByUsername(anyString()))
      .thenReturn(Optional.ofNullable(this.user));

    UserDetails actual = this.userService.loadUserByUsername("username");

    Assert.assertEquals(this.user.getUsername(), actual.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void loadUserByUsername_withNotExistingUsername_shouldThrowException() {
    when(this.userRepository.findByUsername("username"))
      .thenReturn(Optional.ofNullable(this.user));

    UserDetails actual = this.userService.loadUserByUsername("wrong_username");
  }

  @Test
  public void findUserByUserName_withExistingUsername_shouldReturnCorrect() {
    UserServiceModel serviceModel = new UserServiceModel() {{
      setUsername("Username");
    }};

    when(this.userRepository.findByUsername(anyString()))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserServiceModel.class))
      .thenReturn(serviceModel);

    UserServiceModel actual = this.userService.findUserByUserName("username");

    Assert.assertEquals(this.user.getUsername(), actual.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void findUserByUserName_withNotExistingUsername_shouldThrowException() {
    UserServiceModel serviceModel = new UserServiceModel() {{
      setUsername("Username");
    }};

    when(this.userRepository.findByUsername("username"))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserServiceModel.class))
      .thenReturn(serviceModel);

    this.userService.findUserByUserName("wrong_username");
  }

  @Test
  public void findUserByUsername_withExistingUsername_shouldReturnCorrect() {
    when(this.userRepository.findByUsername(anyString()))
      .thenReturn(Optional.ofNullable(this.user));

    User actual = this.userService.findUserByUsername("username");

    Assert.assertEquals(this.user.getUsername(), actual.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void findUserByUsername_withNotExistingUsername_shouldThrowException() {
    when(this.userRepository.findByUsername("username"))
      .thenReturn(Optional.ofNullable(this.user));

    this.userService.findUserByUsername("wrong_username");
  }

  @Test
  public void findUserEditByUserName_withExistingUsername_shouldReturnCorrect() {
    UserEditServiceModel serviceModel = new UserEditServiceModel() {{
      setUsername("Username");
    }};

    when(this.userRepository.findByUsername(anyString()))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserEditServiceModel.class))
      .thenReturn(serviceModel);

    UserEditServiceModel actual = this.userService.findUserEditByUserName("username");

    Assert.assertEquals(this.user.getUsername(), actual.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void findUserEditByUserName_withNotExistingUsername_shouldThrowException() {
    UserEditServiceModel serviceModel = new UserEditServiceModel() {{
      setUsername("Username");
    }};

    when(this.userRepository.findByUsername("username"))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserEditServiceModel.class))
      .thenReturn(serviceModel);

    this.userService.findUserEditByUserName("wrong_username");
  }

  @Test
  public void editUserProfile_shouldEditCorrectUser() {
    UserEditServiceModel serviceModel = new UserEditServiceModel(){{
      setId("user_id");
      setUsername("Username");
      setFullName("");
      setAboutMe("about me");
      setEmail("test.email");
      setPassword("1234");
      setProfilePictureUrl("www.test.com");
    }};

    when(this.userRepository.findByUsername("Username"))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(this.bCryptPasswordEncoder.encode(anyString())).thenReturn("encrypted");
    when(this.userRepository.saveAndFlush(this.user)).thenReturn(this.user);
    when(this.modelMapper.map(this.user, UserEditServiceModel.class)).thenReturn(serviceModel);

    UserEditServiceModel actual = this.userService.editUserProfile(serviceModel, "1234");

    Assert.assertEquals(this.user.getId(), actual.getId());
  }

  @Test
  public void findAllUsers_shouldReturnCorrectSize() {
    Pageable pageRequest = PageRequest.of(0, 4);
    List<User> users = new ArrayList<>(){{
      add(user);
      add(user);
      add(user);
      add(user);
    }};

    Page<User> pagedPosts = new PageImpl(users);
    when(this.userRepository.findDistinctByUsernameNot(null, pageRequest))
      .thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(UsersPageServiceModel.class));

    Page<UsersPageServiceModel> actual = this.userService.findAllUsers(mock(Principal.class), pageRequest);

    Assert.assertEquals(users.size(), actual.getContent().size());
  }

  @Test
  public void findByUsernamePage_shouldReturnCorrectSize() {
    Pageable pageRequest = PageRequest.of(0, 4);
    List<User> users = new ArrayList<>(){{
      add(user);
      add(user);
    }};

    Page<User> pagedPosts = new PageImpl(users);
    when(this.userRepository
      .findDistinctByUsernameStartingWithAndUsernameNot(
        "username", "usrnameNot", pageRequest))
      .thenReturn(pagedPosts);
    when(this.modelMapper.map(any(), any())).thenReturn(mock(UsersPageServiceModel.class));

    Page<UsersPageServiceModel> actual =
      this.userService.findByUsernamePage("username", "usrnameNot", pageRequest);

    Assert.assertEquals(users.size(), actual.getContent().size());
  }

  @Test
  public void findCreators_shouldReturnCorrectSize() {
    List<User> users = new ArrayList<>(){{
      add(user);
      add(user);
    }};

    Role role = mock(Role.class);

    when(this.roleService.findByAuthorityReturnRole("ROLE_CREATOR"))
      .thenReturn(role);
    when(this.userRepository.findAllByAuthorities(role))
      .thenReturn(users);
    when(this.modelMapper.map(any(), any()))
      .thenReturn(mock(UserAboutServiceModel.class));

    List<UserAboutServiceModel> actual = this.userService.findCreators();

    Assert.assertEquals(users.size(), actual.size());
  }

  @Test
  public void deleteById_callDeleteByIdFromRepo() {
    doNothing().when(this.userRepository).deleteById("id");

    this.userService.deleteById("id");

    verify(this.userRepository, times(1)).deleteById("id");
  }

  @Test
  public void setUserRole_whenRoleIsUser_shouldCallSave() {
    UserEditServiceModel serviceModel = new UserEditServiceModel(){{
      setId("user_id");
      setAuthorities(new HashSet<>() {{
        add(new RoleServiceModel() {{
          setAuthority("ROLE_USER");
        }});
        add(new RoleServiceModel() {{
          setAuthority("ROLE_CREATOR");
        }});
      }});
    }};

    when(this.userRepository.findById("user_id"))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserEditServiceModel.class))
      .thenReturn(serviceModel);
    when(this.modelMapper.map(serviceModel, User.class))
      .thenReturn(this.user);

    this.userService.setUserRole("user_id", "user");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(this.userRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(this.user.getId(), captor.getValue().getId());
  }

  @Test
  public void setUserRole_whenRoleIsCreator_shouldCallSave() {
    UserEditServiceModel serviceModel = new UserEditServiceModel(){{
      setId("user_id");
      setAuthorities(new HashSet<>() {{
        add(new RoleServiceModel() {{
          setAuthority("ROLE_USER");
        }});
        add(new RoleServiceModel() {{
          setAuthority("ROLE_CREATOR");
        }});
      }});
    }};

    when(this.userRepository.findById("user_id"))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserEditServiceModel.class))
      .thenReturn(serviceModel);
    when(this.modelMapper.map(serviceModel, User.class))
      .thenReturn(this.user);

    this.userService.setUserRole("user_id", "creator");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(this.userRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(this.user.getId(), captor.getValue().getId());
  }

  @Test
  public void setUserRole_whenRoleIsAdmin_shouldCallSave() {
    UserEditServiceModel serviceModel = new UserEditServiceModel(){{
      setId("user_id");
      setAuthorities(new HashSet<>() {{
        add(new RoleServiceModel() {{
          setAuthority("ROLE_USER");
        }});
        add(new RoleServiceModel() {{
          setAuthority("ROLE_CREATOR");
        }});
      }});
    }};

    when(this.userRepository.findById("user_id"))
      .thenReturn(Optional.ofNullable(this.user));
    when(this.modelMapper.map(this.user, UserEditServiceModel.class))
      .thenReturn(serviceModel);
    when(this.modelMapper.map(serviceModel, User.class))
      .thenReturn(this.user);

    this.userService.setUserRole("user_id", "admin");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    verify(this.userRepository, times(1)).saveAndFlush(captor.capture());
    Assert.assertEquals(this.user.getId(), captor.getValue().getId());
  }
}