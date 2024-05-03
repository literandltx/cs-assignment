package com.literandltx.assignment.mapper;

import com.literandltx.assignment.config.MapperConfig;
import com.literandltx.assignment.dto.CreateUserRequest;
import com.literandltx.assignment.dto.FullUpdateUserRequest;
import com.literandltx.assignment.dto.PartUpdateUserRequest;
import com.literandltx.assignment.dto.UserResponse;
import com.literandltx.assignment.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(final CreateUserRequest request);

    User toModel(final FullUpdateUserRequest request);

    User toModel(final PartUpdateUserRequest request);

    UserResponse toDto(final User model);
}
