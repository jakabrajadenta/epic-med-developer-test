package com.epicmed.developer.assessment.service;

import com.epicmed.developer.assessment.cache.HazelcastCacheManager;
import com.epicmed.developer.assessment.client.UserRestClient;
import com.epicmed.developer.assessment.dto.PageResponseDto;
import com.epicmed.developer.assessment.dto.UserRequestDto;
import com.epicmed.developer.assessment.dto.UserResponseDto;
import com.epicmed.developer.assessment.exception.GeneralException;
import com.epicmed.developer.assessment.model.User;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.epicmed.developer.assessment.util.constant.GeneralConstant.EMPTY_STRING;
import static com.epicmed.developer.assessment.util.constant.GeneralConstant.KEY_DUMMY_DTO;

@Log4j2
@Service
@AllArgsConstructor
public class UserService {

    private ModelMapper modelMapper;
    private UserRestClient userRestClient;
    private HazelcastCacheManager hazelcastCacheManager;

    public PageResponseDto<UserResponseDto> getUsers(UserRequestDto requestDto) {
        log.info("UserService::getUsers started");
        validateRequest(requestDto);

        var dummyDto = hazelcastCacheManager.getDummyDto(KEY_DUMMY_DTO);
        if (Objects.isNull(dummyDto) || requestDto.isFetch()) {
            log.info("UserService::getUsers fetch user data");
            dummyDto = userRestClient.getUsers();
            hazelcastCacheManager.setDummyDto(KEY_DUMMY_DTO,dummyDto);
        }
        var allUsers = dummyDto.getUsers();
        var filteredUsers = filterAndMapUsers(allUsers, requestDto);
        return createPaginatedResponse(filteredUsers, requestDto);
    }

    private void validateRequest(UserRequestDto requestDto) {
        if (Objects.isNull(requestDto)) {
            throw new GeneralException("Validate, request cannot be null");
        }

        if (requestDto.getPage() < 1) {
            throw new GeneralException("Validate, page must be greater than 0");
        }
        if (requestDto.getSize() < 1) {
            throw new GeneralException("Validate, size must be greater than 0");
        }
        if (requestDto.getSize() > 100) {
            throw new GeneralException("Validate, size cannot exceed 100");
        }
    }

    private List<UserResponseDto> filterAndMapUsers(List<User> users, UserRequestDto requestDto) {
        return users.stream()
                .filter(user -> matchesNameFilter(user, requestDto.getName()))
                .filter(user -> matchesEmailFilter(user, requestDto.getEmail()))
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .toList();
    }

    private boolean matchesNameFilter(User user, String nameFilter) {
        if (StringUtils.isBlank(nameFilter)) {
            return true;
        }
        var searchTerm = nameFilter.trim().toLowerCase();
        var firstName = user.getFirstName() != null ? user.getFirstName().toLowerCase() : EMPTY_STRING;
        var lastName = user.getLastName() != null ? user.getLastName().toLowerCase() : EMPTY_STRING;
        return firstName.contains(searchTerm) || lastName.contains(searchTerm);
    }

    private boolean matchesEmailFilter(User user, String emailFilter) {
        if (StringUtils.isBlank(emailFilter)) {
            return true;
        }
        if (user.getEmail() == null) {
            return false;
        }
        return user.getEmail().toLowerCase().contains(emailFilter.trim().toLowerCase());
    }


    private PageResponseDto<UserResponseDto> createPaginatedResponse(List<UserResponseDto> users, UserRequestDto requestDto) {
        var page = requestDto.getPage();
        var size = requestDto.getSize();
        var totalItems = users.size();
        var totalPages = (int) Math.ceil((double) totalItems / size);

        if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        var startIndex = (page - 1) * size;
        var endIndex = Math.min(startIndex + size, totalItems);

        if (startIndex >= totalItems) {
            return PageResponseDto.<UserResponseDto>builder()
                    .page(page)
                    .size(size)
                    .totalItems(totalItems)
                    .totalPages(totalPages)
                    .data(Collections.emptyList())
                    .build();
        }

        var paginatedData = users.subList(startIndex, endIndex);
        return PageResponseDto.<UserResponseDto>builder()
                .page(page)
                .size(size)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .data(paginatedData)
                .build();
    }
}
