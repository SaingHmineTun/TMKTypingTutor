package it.saimao.tmk_typing.model;

public class Key {
    private String engShift, eng,taiShift, tai;

    public Key(String engShift, String eng, String taiShift, String tai) {
        this.engShift = engShift;
        this.eng = eng;
        this.taiShift = taiShift;
        this.tai = tai;
    }

    public String getEngShift() {
        return engShift;
    }

    public void setEngShift(String engShift) {
        this.engShift = engShift;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getTaiShift() {
        return taiShift;
    }

    public void setTaiShift(String taiShift) {
        this.taiShift = taiShift;
    }

    public String getTai() {
        return tai;
    }

    public void setTai(String tai) {
        this.tai = tai;
    }
}
