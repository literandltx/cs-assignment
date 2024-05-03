package com.literandltx.assignment.service;

import com.literandltx.assignment.dto.CreateUserRequest;
import com.literandltx.assignment.dto.FullUpdateUserRequest;
import com.literandltx.assignment.dto.PartUpdateUserRequest;
import com.literandltx.assignment.dto.UserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public interface UserService {
    UserResponse save(
            final CreateUserRequest request
    );

    UserResponse partUpdateById(
            final PartUpdateUserRequest request,
            final Long id
    );

    UserResponse fullUpdateById(
            final FullUpdateUserRequest request,
            final Long id
    );

    ResponseEntity<Void> deleteById(
            final Long id
    );

    List<UserResponse> search(
            final LocalDate from,
            final LocalDate to,
            final Pageable pageable
    );
}
