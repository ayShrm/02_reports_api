package com.ayush.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ayush.entity.EligDtls;

public interface EligibilityDtlsRepo extends JpaRepository<EligDtls, Integer>{

	@Query("select distinct(planName) from EligibilityDtls")
	public List<String> findPlanNames();
	
	@Query("select distinct(planStatus) from EligibilityDtls")
	public List<String> findPlanStatuses();
}
