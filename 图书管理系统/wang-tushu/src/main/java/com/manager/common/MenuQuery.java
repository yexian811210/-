package com.manager.common;

import lombok.Data;

@Data
public class MenuQuery {
    private Long userId;
    private Integer parentId;
    private String label;
    private Integer status;
}
