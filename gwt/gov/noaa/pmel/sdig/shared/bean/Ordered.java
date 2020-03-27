package gov.noaa.pmel.sdig.shared.bean;

public abstract class Ordered {
    protected int position;
    protected Ordered() {
        this.position = -1;
    }
    protected Ordered(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
}
