package com.github.lvyahui8.ellyn.plugin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Method {
    int id;
    String name;
    String fullName;
    String clazz;
}
