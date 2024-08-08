package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.entities.City;
import com.MyP.Zilieri.repository.CityRepository;
import com.MyP.Zilieri.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> findByNumeStartingWith(String query) {
        Pageable pageable = PageRequest.of(0, 7); // First page, 7 results

        return cityRepository.findByNumeStartingWith(query, pageable);
    }
}
