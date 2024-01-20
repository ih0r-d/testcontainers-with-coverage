package com.example.service;

import com.example.entities.Appliance;

import java.util.List;

public interface ApplianceService {

    Appliance getApplianceById(long id);
    List<Appliance> getAllAppliances();

    Appliance create(Appliance appliance);

    void remove(long id);
}
