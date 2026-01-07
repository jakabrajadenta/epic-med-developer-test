package com.epicmed.developer.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 0;

    private String name;
    private String email;

    @Builder.Default
    private boolean fetch = false;
}
