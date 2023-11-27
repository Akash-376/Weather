package com.wise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wise.model.WeatherData;
import com.wise.service.WeatherService;


@CrossOrigin
@Controller
public class WeatherController {
	// Autowired to inject WeatherService bean
	@Autowired
	private WeatherService weatherService;
	
	/**
     * Handles the GET request for the home page.
     *
     * @return The name of the Thymeleaf template (index.html) to be rendered.
     */
	@GetMapping("/")
    public String index() {
        return "index";
    }
	
	/**
     * Handles the POST request for storing weather data to the database.
     *
     * @param city  The name of the city for which weather data is requested.
     * @param model The Spring MVC model to pass data to the view.
     * @return The name of the Thymeleaf template (weather.html) to be rendered with weather data.
     */
	@PostMapping("/storeData")
    public String storeDataToDB(@RequestParam String city, Model model) {
        if (isValidCity(city)) {
            WeatherData currentData = weatherService.getWeatherByCity(city.trim());
            model.addAttribute("weatherResult", currentData);
            return "weather";
        } else {
            // Add an attribute to indicate the validation error
            model.addAttribute("invalidCity", true);
            return "index"; // Return to the home page
        }
    }

    private boolean isValidCity(String city) {
    	
        return city != null && !city.isEmpty();
    }

	
}
