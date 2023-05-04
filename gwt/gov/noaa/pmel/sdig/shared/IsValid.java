package gov.noaa.pmel.sdig.shared;

public interface IsValid {

    public boolean isValid();

    public void validate() throws IllegalStateException;
}
