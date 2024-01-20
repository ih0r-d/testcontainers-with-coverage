package com.example.mapper;

import com.example.dto.ApplianceDto;
import com.example.entities.Appliance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplianceMapper extends CommonMappable<Appliance, ApplianceDto>{
}
