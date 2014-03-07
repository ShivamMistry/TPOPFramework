package com.tpop.practical.practical14;

/**
 * Created with IntelliJ IDEA.
 * User: sm1334
 * Date: 28/02/14
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class City {

    private final String name;
    private final String country;
    private final String state;
    private final String timezone;
    private final double longitude, latitude;

    public City(final String name, final String country, final String state, final String timezone,
                final double longitude, final double latitude) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.timezone = timezone;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getTimezone() {
        return timezone;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean equals(Object o) {
        return o instanceof City && ((City) o).latitude == latitude && ((City) o).longitude == longitude
                && ((City) o).getName().equals(name);
    }

    public String toString() {
        return String.format("(City: %s Country: %s)",
                name, country);
    }

}
