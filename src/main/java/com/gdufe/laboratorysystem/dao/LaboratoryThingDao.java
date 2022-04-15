package com.gdufe.laboratorysystem.dao;

import com.gdufe.laboratorysystem.entity.LaboratoryThing;
import org.springframework.stereotype.Repository;

@Repository
public interface LaboratoryThingDao {
    LaboratoryThing add(LaboratoryThing aa);
     LaboratoryThing ad();
}
