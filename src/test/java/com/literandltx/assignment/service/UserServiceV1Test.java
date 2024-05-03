package com.literandltx.assignment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.literandltx.assignment.dto.CreateUserRequest;
import com.literandltx.assignment.dto.FullUpdateUserRequest;
import com.literandltx.assignment.dto.PartUpdateUserRequest;
import com.literandltx.assignment.dto.UserResponse;
import com.literandltx.assignment.exception.custom.UserRegistrationException;
import com.literandltx.assignment.mapper.UserMapper;
import com.literandltx.assignment.model.User;
import com.literandltx.assignment.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class UserServiceV1Test {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceV1 userService;

    @ParameterizedTest
    @ValueSource(ints = {18, 19, 34})
    public void save_ValidRequest_UserSaved(int age) {
        // Given
        String email = "email@example.com";
        String firstname = "firstname";
        String lastname = "lastname";
        LocalDate birthdate = LocalDate.now().minusYears(age);
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthdate(birthdate)
                .build();

        User model = new User(email, firstname, lastname, birthdate);

        // When
        when(userMapper.toModel(request)).thenReturn(model);
        when(userRepository.save(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(new UserResponse());

        UserResponse response = userService.save(request);

        // Then
        assertNotNull(response);
        verify(userMapper, times(1)).toModel(request);
        verify(userRepository, times(1)).save(model);
        verify(userMapper, times(1)).toDto(model);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 16, 17})
    public void save_UnderAgeUser_ExceptionThrown(int age) {
        // Given
        String email = "email@example.com";
        String firstname = "firstname";
        String lastname = "lastname";
        LocalDate birthdate = LocalDate.now().minusYears(age);
        CreateUserRequest request = CreateUserRequest.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthdate(birthdate)
                .build();

        User user = new User(email, firstname, lastname, birthdate);

        // When
        when(userMapper.toModel(request)).thenReturn(user);

        // Then
        assertThrows(UserRegistrationException.class, () -> userService.save(request));
    }

    @Test
    void deleteById_UserExists_DeletedSuccessfully() {
        // Given
        Long userId = 1L;

        // When
        ResponseEntity<Void> responseEntity = userService.deleteById(userId);

        // Then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void search_ValidParameters_ReturnsUserResponses() {
        // Given
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 12, 31);
        Pageable pageable = Pageable.unpaged();

        // When
        when(userRepository.findUserByBirthdateBetween(from, to, pageable)).thenReturn(Collections.emptyList());
        List<UserResponse> userResponses = userService.search(from, to, pageable);

        // Then
        assertNotNull(userResponses);
        assertTrue(userResponses.isEmpty());
        verify(userRepository, times(1)).findUserByBirthdateBetween(from, to , pageable);
        verify(userMapper, times(0)).toDto(any());
    }

    @Test
    void search_FromAfterTo_ThrowsIllegalArgumentException() {
        // Given
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(1990, 12, 31);
        Pageable pageable = Pageable.unpaged();

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.search(from, to, pageable));
        assertEquals("Param 'from' must be before 'to', but was 'from': 2000-01-01, 'to': 1990-12-31", exception.getMessage());
        verify(userRepository, never()).findUserByBirthdateBetween(any(), any(), eq(pageable));
    }

    @ParameterizedTest
    @ValueSource(ints = {18, 34})
    void fullUpdateById_ValidRequest_UserUpdatedSuccessfully(int years) {
        // Given
        String email = "email@example.com";
        String firstname = "firstname";
        String lastname = "lastname";
        LocalDate birthdate = LocalDate.now().minusYears(years);
        FullUpdateUserRequest request = FullUpdateUserRequest.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthdate(birthdate)
                .build();
        Long userId = 1L;
        User model = new User(email, firstname, lastname, birthdate);

        // When
        when(userMapper.toModel(request)).thenReturn(model);
        when(userRepository.save(model)).thenReturn(model);
        when(userMapper.toDto(model)).thenReturn(new UserResponse());
        when(userRepository.existsById(userId)).thenReturn(true);
        UserResponse response = userService.fullUpdateById(request, userId);

        // Then
        assertNotNull(response);
        verify(userMapper, times(1)).toModel(request);
        verify(userRepository, times(1)).save(model);
        verify(userMapper, times(1)).toDto(model);
    }

    @Test
    void fullUpdateById_UserNotFound_ThrowsEntityNotFoundException() {
        // Given
        FullUpdateUserRequest request = FullUpdateUserRequest.builder()
                .email("test@example.com")
                .firstname("John")
                .lastname("Doe")
                .birthdate(LocalDate.of(1990, 1, 1))
                .build();
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> userService.fullUpdateById(request, userId));
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toDto(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 13, 17})
    void fullUpdateById_InvalidDate_ThrowsUserRegistrationException(int years) {
        // Given
        String email = "email@example.com";
        String firstname = "firstname";
        String lastname = "lastname";
        LocalDate birthdate = LocalDate.now().minusYears(years);
        FullUpdateUserRequest request = FullUpdateUserRequest.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthdate(birthdate)
                .build();
        Long userId = 1L;
        User model = new User(email, firstname, lastname, birthdate);

        // When
        when(userMapper.toModel(request)).thenReturn(model);
        when(userRepository.existsById(userId)).thenReturn(true);

        // Then
        assertThrows(UserRegistrationException.class, () -> userService.fullUpdateById(request, userId));
    }

    @Test
    void partUpdateById_ValidRequest_UserUpdatedSuccessfully() {
        // Given
        String email = "email@example.com";
        String firstname = "firstname";
        String lastname = "lastname";
        LocalDate birthdate = LocalDate.now().minusYears(20);
        String address = "address";
        String phoneNumber = "";

        PartUpdateUserRequest request = PartUpdateUserRequest.builder().build();
        User modelFromDb = new User(email, firstname, lastname, birthdate, address, phoneNumber);
        Long id = 1L;

        // When
        when(userMapper.toModel(request)).thenReturn(modelFromDb);
        when(userRepository.findById(id)).thenReturn(Optional.of(modelFromDb));
        when(userRepository.save(modelFromDb)).thenReturn(modelFromDb);
        when(userMapper.toDto(modelFromDb)).thenReturn(new UserResponse());
        UserResponse response = userService.partUpdateById(request, id);

        // Then
        assertNotNull(response);
    }

}