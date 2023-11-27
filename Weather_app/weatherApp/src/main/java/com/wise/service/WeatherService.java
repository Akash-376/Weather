package com.wise.service;

import com.wise.model.WeatherData;
/**
 * Service interface for retrieving weather data.
 */
public interface WeatherService {
	
	/**
     * Get weather data for a specific city.
     *
     * @param city The name of the city for which weather data is requested.
     * @return The weather data for the specified city.
     */
	public WeatherData getWeatherByCity(String city);
}
