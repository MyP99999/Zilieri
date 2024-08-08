package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.City;

import java.util.List;

public interface CityService {
    List<City> findByNumeStartingWith(String query);
}
