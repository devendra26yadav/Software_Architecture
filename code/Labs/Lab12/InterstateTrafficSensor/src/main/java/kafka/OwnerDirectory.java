package kafka;

import org.springframework.stereotype.Component;

@Component
public class OwnerDirectory {

    public OwnerInfo findOwner(String licencePlate) {
        if (licencePlate.startsWith("AA")) {
            return new OwnerInfo("Alice Anderson", "Fairfield, IA");
        }
        if (licencePlate.startsWith("AS")) {
            return new OwnerInfo("Aiden Stone", "Chicago, IL");
        }
        if (licencePlate.startsWith("BB")) {
            return new OwnerInfo("Benjamin Brooks", "Des Moines, IA");
        }
        if (licencePlate.startsWith("BS")) {
            return new OwnerInfo("Bella Stewart", "St. Paul, MN");
        }
        if (licencePlate.startsWith("CC")) {
            return new OwnerInfo("Charlotte Clark", "Kansas City, MO");
        }
        if (licencePlate.startsWith("CS")) {
            return new OwnerInfo("Caleb Scott", "Madison, WI");
        }
        if (licencePlate.startsWith("EE")) {
            return new OwnerInfo("Emma Edwards", "Omaha, NE");
        }
        if (licencePlate.startsWith("ES")) {
            return new OwnerInfo("Ethan Sanders", "Cedar Rapids, IA");
        }
        if (licencePlate.startsWith("FF")) {
            return new OwnerInfo("Frank Foster", "Austin, TX");
        }
        if (licencePlate.startsWith("FS")) {
            return new OwnerInfo("Fiona Sims", "Minneapolis, MN");
        }
        if (licencePlate.startsWith("GA")) {
            return new OwnerInfo("Grace Allen", "Milwaukee, WI");
        }
        if (licencePlate.startsWith("GS")) {
            return new OwnerInfo("Gavin Sullivan", "Rochester, MN");
        }
        if (licencePlate.startsWith("FBS")) {
            return new OwnerInfo("Felix Bishop", "Naperville, IL");
        }
        if (licencePlate.startsWith("FB")) {
            return new OwnerInfo("Faith Bennett", "Dallas, TX");
        }
        if (licencePlate.startsWith("FAS")) {
            return new OwnerInfo("Finn Ashton", "St. Louis, MO");
        }
        if (licencePlate.startsWith("FA")) {
            return new OwnerInfo("Freya Adams", "Phoenix, AZ");
        }
        if (licencePlate.startsWith("FGS")) {
            return new OwnerInfo("Flynn Graves", "Denver, CO");
        }
        if (licencePlate.startsWith("FG")) {
            return new OwnerInfo("Flora Green", "Indianapolis, IN");
        }
        return new OwnerInfo("Unknown Owner", "Unknown Address");
    }
}
