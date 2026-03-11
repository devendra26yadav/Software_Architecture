package kafka;

public class OwnerInfo {

    private String ownerName;
    private String ownerAddress;

    public OwnerInfo() {
    }

    public OwnerInfo(String ownerName, String ownerAddress) {
        this.ownerName = ownerName;
        this.ownerAddress = ownerAddress;
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
