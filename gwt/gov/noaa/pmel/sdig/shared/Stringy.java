package gov.noaa.pmel.sdig.shared;

public interface Stringy {
    default int sCompare(String s1, String s2) {
        return s1 == null ?
               ( s2 == null ? 0 : -1 ) :
               ( s2 == null ? 1 : s1.trim().compareTo(s2.trim()));
    }

    default boolean sAreTheSame(String s1, String s2) {
        return sCompare(s1,s2) == 0;
    }
}
