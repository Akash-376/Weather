package com.wise.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wise.model.WeatherData;
import com.wise.repository.WeatherRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the WeatherService interface that retrieves weather data
 * for a given city from an external API and stores it in the database.
 */
@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Autowired
    private WeatherRepository weatherRepository;

    // API key for OpenWeatherMap
    private static final String API_KEY = "862113a4d4b74bacccccbcbad1e67955";
    
    // API URL template for OpenWeatherMap
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    /**
     * Retrieves weather data for a given city from the database. If the data is
     * not available or is older than 30 minutes, fetches the latest data from the
     * external API, saves it to the database, and returns the updated weather data.
     *
     * @param city The name of the city for which weather data is requested.
     * @return WeatherData object containing the weather information for the city.
     */
    @Override
    public WeatherData getWeatherByCity(String city) {
        // Calculate the timestamp for 30 minutes ago
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);

        // Attempt to retrieve the most recent weather data from the database
        Optional<WeatherData> recentWeatherData = weatherRepository
                .findTop1ByCityAndTimestampAfterOrderByTimestampDesc(city, thirtyMinutesAgo);

        if (recentWeatherData.isPresent()) {
            // Return the cached weather data if available and within the time limit
            return recentWeatherData.get();
        } else {
            try {
                // If data is not available or outdated, fetch it from the API
                WeatherData weatherCurrentData = fetchDataFromApi(city);

                // Save the newly fetched data to the database
                saveWeatherDataToDatabase(weatherCurrentData);

                // Retrieve and return the updated weather data
                recentWeatherData = weatherRepository
                        .findTop1ByCityAndTimestampAfterOrderByTimestampDesc(city, thirtyMinutesAgo);

                return recentWeatherData.orElse(null);
            } catch (Exception e) {
                // Log any errors that occur during the process
                logger.error("Error fetching or saving weather data for city '{}': {}", city, e.getMessage());
                return null;
            }
        }
    }

    /**
     * Fetches weather data for a given city from the OpenWeatherMap API.
     *
     * @param city The name of the city for which weather data is requested.
     * @return WeatherData object containing the fetched weather information.
     * @throws IOException            If an I/O error occurs during the HTTP request.
     * @throws InterruptedException   If the HTTP request is interrupted.
     */
    private WeatherData fetchDataFromApi(String city) throws IOException, InterruptedException {
        // Construct the API URL with the city name and API key
        String url = String.format(API_URL, city, API_KEY);

        // Create an HTTP request to the OpenWeatherMap API
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();

        // Send the HTTP request and get the response
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse the API response and create a WeatherData object
        return parseApiResponse(response.body(), city);
    }

    /**
     * Parses the JSON response from the OpenWeatherMap API and creates a WeatherData object.
     *
     * @param responseBody The JSON response from the OpenWeatherMap API.
     * @param city         The name of the city for which weather data is requested.
     * @return WeatherData object containing the parsed weather information.
     */
    private WeatherData parseApiResponse(String responseBody, String city) {
        // Use Gson library to parse the JSON response
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

        // Extract relevant weather information from the JSON response
        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        LocalDateTime date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateTimestamp),
                ZoneId.systemDefault()
        );
        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int temp = (int) (temperatureKelvin - 273.15);
        JsonArray weatherArray = jsonObject.getAsJsonArray("weather");
        JsonObject weatherObject = weatherArray.get(0).getAsJsonObject();
        String description = weatherObject.get("description").getAsString();
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        double longitude = jsonObject.getAsJsonObject("coord").get("lon").getAsDouble();
        double latitude = jsonObject.getAsJsonObject("coord").get("lat").getAsDouble();

        // Create a WeatherData object with the extracted information
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(city);
        weatherData.setDescription(description);
        weatherData.setLongitude(longitude);
        weatherData.setLatitude(latitude);
        weatherData.setTemp(temp);
        weatherData.setHumidity(humidity);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setTimestamp(date);

        return weatherData;
    }

    /**
     * Saves weather data to the database.
     *
     * @param weatherData The WeatherData object to be saved.
     */
    private void saveWeatherDataToDatabase(WeatherData weatherData) {
        try {
            // Save the WeatherData object to the database
            weatherRepository.save(weatherData);

            // Log a success message
            logger.info("Weather data saved for city '{}'", weatherData.getCity());
        } catch (Exception e) {
            // Log an error message if the save operation fails
            logger.error("Error saving weather data to the database: {}", e.getMessage());
        }
    }
}
