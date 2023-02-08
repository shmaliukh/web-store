package com.vshmaliukh.webstore;

import java.util.*;

public class TestUtils {

    private TestUtils() {
    }

    public static boolean isUnmodifiableList(List list) {
        return list.getClass().isInstance(Collections.unmodifiableList(new ArrayList<>()));
    }

    public static boolean isUnmodifiableSet(Set set) {
        return set.getClass().isInstance(Collections.unmodifiableSet(new HashSet<>()));
    }

//    TODO is it better to use 'checkCollectionIsInstance' implementation instead of 'isUnmodifiableSet' and 'isUnmodifiableList' methods ?
//    public static boolean checkCollectionIsInstance(Collection collectionToCheck, Collection instance){
//        return collectionToCheck.getClass().isInstance(instance);
//    }

}
