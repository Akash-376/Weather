package com.wise.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wise.model.WeatherData;

/**
 * Repository interface for managing weather data entities.
 */
public interface WeatherRepository extends JpaRepository<WeatherData, Integer>{
	
	/**
     * Find the latest weather data for a specific city recorded after a given timestamp.
     *
     * @param city      The name of the city.
     * @param timestamp The timestamp representing the point in time after which the weather data was recorded.
     * @return An optional containing the latest weather data for the specified city and timestamp, or empty if not found.
     */
	Optional<WeatherData> findTop1ByCityAndTimestampAfterOrderByTimestampDesc(String city, LocalDateTime timestamp);
}
