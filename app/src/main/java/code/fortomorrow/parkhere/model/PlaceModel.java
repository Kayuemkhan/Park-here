package code.fortomorrow.parkhere.model;

// PlaceModel.java
public class PlaceModel {
    private String placeName;
    private String numberOfPlaces;
    private String percentageUp;
    private String details;

    public PlaceModel(String placeName, String numberOfPlaces, String percentageUp, String details) {
        this.placeName = placeName;
        this.numberOfPlaces = numberOfPlaces;
        this.percentageUp = percentageUp;
        this.details = details;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public String getPercentageUp() {
        return percentageUp;
    }

    public String getDetails() {
        return details;
    }
}
