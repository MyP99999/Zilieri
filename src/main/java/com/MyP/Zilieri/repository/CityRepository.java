package com.MyP.Zilieri.repository;

import com.MyP.Zilieri.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByNumeStartingWith(String query, Pageable pageable);

}
