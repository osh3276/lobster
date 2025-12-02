package org.lobster.use_case.browse_airport;

/**
 * Input data for the Browse Airport use case.
 */
public class BrowseAirportInputData {
    private final String airportCode;
    /**
     * Constructs the input data with a sanitized airport code.
     *
     * @param airportCode the airport code entered by the user,
     *                    possibly null or unformatted
     */
    public BrowseAirportInputData(String airportCode) {
        this.airportCode = (airportCode == null ? "" : airportCode.trim().toUpperCase());
    }
    /**
     * Returns the sanitized airport code.
     *
     * @return the processed airport code string
     */
    public String getAirportCode() {
        return airportCode;
    }
}
