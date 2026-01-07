package com.epicmed.developer.assessment.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultErrorAttribute {

    private int status;
    private String message;
    private String error;
    private String path;
    private String timestamp;
    private List<DetailError> details;
}
