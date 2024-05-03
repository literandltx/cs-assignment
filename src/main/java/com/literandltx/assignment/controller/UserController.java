package com.literandltx.assignment.controller;

import com.literandltx.assignment.dto.CreateUserRequest;
import com.literandltx.assignment.dto.FullUpdateUserRequest;
import com.literandltx.assignment.dto.PartUpdateUserRequest;
import com.literandltx.assignment.dto.UserResponse;
import com.literandltx.assignment.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@RestController
public class UserController {
    private final UserService userServiceV1;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponse save(
            @RequestBody @Valid final CreateUserRequest request
    ) {
        log.info("Save method called with input {}", request);

        return userServiceV1.save(request);
    }

    @PatchMapping
    @ResponseStatus(code = HttpStatus.OK)
    public UserResponse partUpdateById(
            @RequestBody @Valid final PartUpdateUserRequest request,
            @RequestParam(name = "id") final Long id
    ) {
        log.info("partUpdateById method called with input body: {} and user id: {}.", request, id);

        return userServiceV1.partUpdateById(request, id);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public UserResponse fullUpdateById(
            @RequestBody @Valid final FullUpdateUserRequest request,
            @RequestParam(name = "id") final Long id
    ) {
        log.info("fullUpdateById method called with input body: {} and user id: {}.", request, id);

        return userServiceV1.fullUpdateById(request, id);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Void> deleteById(
            @RequestParam(name = "id") final Long id
    ) {
        log.info("deleteById method called to delete user with user id: {}.", id);

        return userServiceV1.deleteById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserResponse> search(
            @RequestParam(name = "from") final LocalDate from,
            @RequestParam(name = "to") final LocalDate to,
            final Pageable pageable
    ) {
        log.info("search in range method called to delete user with params 'from': {}, 'to': {}, pageRequest: page={}, size={}.",
                from, to, pageable.getPageNumber(), pageable.getPageSize()
        );

       return userServiceV1.search(from, to, pageable);
    }
}
