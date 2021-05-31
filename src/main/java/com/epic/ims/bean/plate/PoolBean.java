package com.epic.ims.bean.plate;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PoolBean {
    List<ArrayList<String>> poolList;
}
