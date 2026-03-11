package kafka;

public class SpeedRecord {

    private String licencePlate;
    private int speed;
    private int travelTimeInSeconds;

    public SpeedRecord() {
    }

    public SpeedRecord(String licencePlate, int speed, int travelTimeInSeconds) {
        this.licencePlate = licencePlate;
        this.speed = speed;
        this.travelTimeInSeconds = travelTimeInSeconds;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTravelTimeInSeconds() {
        return travelTimeInSeconds;
    }

    public void setTravelTimeInSeconds(int travelTimeInSeconds) {
        this.travelTimeInSeconds = travelTimeInSeconds;
    }

    @Override
    public String toString() {
        return licencePlate + " " + speed + "mph in " + travelTimeInSeconds + "s";
    }
}
