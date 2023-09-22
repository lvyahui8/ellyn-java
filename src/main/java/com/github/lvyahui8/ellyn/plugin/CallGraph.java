package com.github.lvyahui8.ellyn.plugin;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class CallGraph {

    @Getter
    private Set<Integer> methods = new HashSet<>();

    @Getter
    private Set<Long> edges = new HashSet<>();

    public void add(int from,int to) {
        methods.add(from);
        methods.add(to);
        edges.add(((long)from) << 32 | to);
    }
}
