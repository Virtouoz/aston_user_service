package com.learn.assembler;

import com.learn.controller.UserController;
import com.learn.dto.UserResponseDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponseDto, EntityModel<UserResponseDto>> {

    @Override
    public EntityModel<UserResponseDto> toModel(UserResponseDto dto) {
        EntityModel<UserResponseDto> model = EntityModel.of(dto);
        model.add(linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all"));
        return model;
    }

    public CollectionModel<EntityModel<UserResponseDto>> toCollectionModel(List<UserResponseDto> dtos) {
        List<EntityModel<UserResponseDto>> models = dtos.stream()
                .map(this::toModel)
                .toList();
        CollectionModel<EntityModel<UserResponseDto>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return collection;
    }
}