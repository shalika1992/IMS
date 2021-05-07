package com.epic.ims.bean.common;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Status {
    private String statusCode;
    private String description;
    private String statusCategoryCode;
}
