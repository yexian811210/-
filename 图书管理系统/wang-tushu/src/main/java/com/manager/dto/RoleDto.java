package com.manager.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {

    private Long userId;

    private List<Long> roleIdList;
}
