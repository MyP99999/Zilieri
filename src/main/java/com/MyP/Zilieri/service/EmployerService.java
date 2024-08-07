package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.Employer;

public interface EmployerService {
    Employer saveEmployer(Employer employer);


    Employer findEmployerByUserId(Integer userId);


    Employer findEmployerById(Integer id);


    void deleteEmployer(Integer id);

}
