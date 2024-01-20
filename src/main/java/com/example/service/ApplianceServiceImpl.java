package com.example.service;

import com.example.entities.Appliance;
import com.example.exceptions.ApplianceNotFoundException;
import com.example.repository.ApplianceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplianceServiceImpl implements ApplianceService {

    private final ApplianceRepository applianceRepository;

    @Override
    public Appliance getApplianceById(long id) {
        var appliance = applianceRepository.findById(id)
                .orElseThrow(ApplianceNotFoundException::new);
        applianceRepository.updateApplianceAmountById(id);
        return appliance;
    }

    @Override
    public List<Appliance> getAllAppliances() {
        var appliances = applianceRepository.findAll(Sort.by("id").descending());
        appliances.forEach(item -> applianceRepository.updateApplianceAmountById(item.getId()));
        return appliances;
    }

    @Override
    public Appliance create(Appliance appliance) {
        return applianceRepository.save(appliance);
    }

    @Override
    public void remove(long id) {
        applianceRepository.deleteById(id);
    }
}
