package com.epic.ims.bean.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class Status {
    private String statusCode;
    private String description;
    private String statusCategoryCode;

}
