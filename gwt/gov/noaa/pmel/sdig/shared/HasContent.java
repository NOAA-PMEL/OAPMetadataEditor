package gov.noaa.pmel.sdig.shared;

public interface HasContent {
    public boolean hasContent();
    default boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
