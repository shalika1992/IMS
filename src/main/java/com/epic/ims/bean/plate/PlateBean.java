package com.epic.ims.bean.plate;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PlateBean {
    private Map<String, List<String>> plateArray;
    private Map<String, String> swapArray;
    private long id;
}
