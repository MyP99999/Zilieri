package com.MyP.Zilieri.controller;


import com.MyP.Zilieri.entities.City;
import com.MyP.Zilieri.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;


    @GetMapping("/search")
    public List<City> searchCities(@RequestParam String query) {
        return cityService.findByNumeStartingWith(query);
    }
}
