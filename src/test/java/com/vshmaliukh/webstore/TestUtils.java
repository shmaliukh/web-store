package com.vshmaliukh.webstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestUtils {

    private TestUtils() {
    }

    public static boolean isUnmodifiableList(List list) {
        return list.getClass().isInstance(Collections.unmodifiableList(new ArrayList<>()));
    }

}
