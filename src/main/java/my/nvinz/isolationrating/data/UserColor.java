package my.nvinz.isolationrating.data;

public enum UserColor {
    GREEN("green"),
    ORANGE("orange"),
    RED("red");

    private String color;

    UserColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
