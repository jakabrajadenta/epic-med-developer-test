package com.epicmed.developer.assessment.dto;

import com.epicmed.developer.assessment.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DummyDto {

    private List<User> users;
    private Integer total;
    private Integer skip;
    private Integer limit;
}
