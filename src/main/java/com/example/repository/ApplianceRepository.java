package com.example.repository;

import com.example.entities.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRepository extends JpaRepository<Appliance, Long> {

    @Query(value = """
             update appliances set amount = amount + 1 where id = :id
            """, nativeQuery = true)
    @Modifying
    void updateApplianceAmountById(@Param("id") long id);
}
