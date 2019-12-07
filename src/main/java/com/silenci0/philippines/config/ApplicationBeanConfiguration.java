package com.silenci0.philippines.config;

import com.silenci0.philippines.domain.entities.Place;
import com.silenci0.philippines.domain.entities.Post;
import com.silenci0.philippines.domain.models.binding.EditProfileBindingModel;
import com.silenci0.philippines.domain.models.binding.ThingsToDoBindingModel;
import com.silenci0.philippines.domain.models.service.PlaceServiceModel;
import com.silenci0.philippines.domain.models.service.PostAddServiceModel;
import com.silenci0.philippines.domain.models.service.ThingsToDoDeleteImageServiceModel;
import com.silenci0.philippines.domain.models.service.UserEditServiceModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<EditProfileBindingModel, UserEditServiceModel>() {
            @Override
            protected void configure() {
                map().setPassword(source.getNewPassword());
            }
        });
        modelMapper.addMappings(new PropertyMap<ThingsToDoDeleteImageServiceModel, ThingsToDoBindingModel>() {
            @Override
            protected void configure() {
                skip(destination.getImages());
            }
        });
        modelMapper.addMappings(new PropertyMap<PlaceServiceModel, Place>() {
            @Override
            protected void configure() {
                skip(destination.getHeaderImage());
            }
        });
        modelMapper.addMappings(new PropertyMap<PostAddServiceModel, Post>() {
            @Override
            protected void configure() {
                skip(destination.getCategories());
            }
        });

        return modelMapper;
    }

}
