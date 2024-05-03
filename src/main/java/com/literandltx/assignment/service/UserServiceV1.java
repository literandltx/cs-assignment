package com.literandltx.assignment.service;

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
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceV1 implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse save(final CreateUserRequest request) {
        final User model = userMapper.toModel(request);

        dateValidation(model);

        final User saved = userRepository.save(model);

        return userMapper.toDto(saved);
    }

    @Override
    public UserResponse partUpdateById(
            final PartUpdateUserRequest request,
            final Long id
    ) {
        final User model = userMapper.toModel(request);

        dateValidation(model);

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id: " + id));
        if (model.getEmail() != null) {
            user.setEmail(model.getEmail());
        }
        if (model.getFirstname() != null) {
            user.setFirstname(model.getFirstname());
        }
        if (model.getLastname() != null) {
            user.setLastname(model.getLastname());
        }
        if (model.getBirthdate() != null) {
            user.setBirthdate(model.getBirthdate());
        }
        if (model.getAddress() != null) {
            user.setAddress(model.getAddress());
        }
        if (model.getPhoneNumber() != null) {
            user.setPhoneNumber(model.getPhoneNumber());
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponse fullUpdateById(
            final FullUpdateUserRequest request,
            final Long id
    ) {
        final User model = userMapper.toModel(request);

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Cannot find user by id: " + id);
        }

        dateValidation(model);

        return userMapper.toDto(userRepository.save(model));
    }

    @Override
    public ResponseEntity<Void> deleteById(final Long id) {
         userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @Override
    public List<UserResponse> search(
            final LocalDate from,
            final LocalDate to,
            final Pageable pageable
    ) {
        if (!from.isBefore(to)) {
            throw new IllegalArgumentException("Param 'from' must be before 'to', but was 'from': " + from + ", 'to': " + to);
        }

        return userRepository.findUserByBirthdateBetween(from, to, pageable).stream()
                .map(userMapper::toDto)
                .toList();
    }

    private void dateValidation(final User model) {
        if (model.getBirthdate() == null) {
            return;
        }

        final Period period = Period.between(model.getBirthdate(), LocalDate.now());
        if (period.getYears() < 18) {
            throw new UserRegistrationException("User must be at least 18 years old, but now: " + period.getYears());
        }
    }
}
