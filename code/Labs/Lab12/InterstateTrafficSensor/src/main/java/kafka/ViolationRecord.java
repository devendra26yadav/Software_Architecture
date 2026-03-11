package kafka;

public class ViolationRecord {

    private String licencePlate;
    private int speed;
    private String ownerName;
    private String ownerAddress;

    public ViolationRecord() {
    }

    public ViolationRecord(String licencePlate, int speed, String ownerName, String ownerAddress) {
        this.licencePlate = licencePlate;
        this.speed = speed;
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }
}
