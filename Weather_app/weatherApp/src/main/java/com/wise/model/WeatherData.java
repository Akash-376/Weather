package com.wise.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing weather data.
 */
@Entity
@Data
@NoArgsConstructor
public class WeatherData {
	// Primary key for the entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // City name for which weather data is recorded
    private String city;

    // Description of the weather conditions
    private String description;

    // Longitude coordinate of the city
    private double longitude;

    // Latitude coordinate of the city
    private double latitude;

    // Temperature in the city
    private double temp;

    // Humidity percentage in the city
    private Integer humidity;

    // Wind speed in the city
    private double windSpeed;

    // Timestamp representing the time when weather data was recorded
    private LocalDateTime timestamp;
	
	
	
}
