package gov.noaa.pmel.sdig.shared;

import gov.noaa.pmel.sdig.shared.bean.TypedString;

import java.util.List;

public interface Stringy {

    default boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    default boolean isEmpty(List<TypedString> strs) {
        if ( strs == null || strs.isEmpty()) {
            return true;
        }
        for (TypedString str : strs) {
            if ( str.hasContent()) {
                return false;
            }
        }
        return true;
    }

    default int sCompare(String s1, String s2) {
        return s1 == null ?
               ( s2 == null ? 0 : -1 ) :
               ( s2 == null ? 1 : s1.trim().compareTo(s2.trim()));
    }

    default int sNullEmptyCompare(String s1, String s2) {
        return (s1 == null || s1.trim().length() == 0) ?
                (( s2 == null || s2.trim().length() == 0) ? 0 : -1 ) :
                (( s2 == null || s2.trim().length() == 0) ? 1 : s1.trim().compareTo(s2.trim()));
    }

    default boolean sAreTheSame(String s1, String s2) {
        return sCompare(s1,s2) == 0;
    }

    default boolean sAreEffectivelyTheSame(String s1, String s2) {
        return sNullEmptyCompare(s1,s2) == 0;
    }

    Stringy sClone();
}
