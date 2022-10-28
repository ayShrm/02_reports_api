package com.ayush.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ayush.entity.EligDtls;
import com.ayush.repository.EligibilityDtlsRepo;

@Component
public class AppRunner implements ApplicationRunner{
	
	@Autowired
	private EligibilityDtlsRepo repo;
	
	@Override
	public void run(ApplicationArguments args) throws Exception{
		EligDtls entity1=new EligDtls();
		entity1.setEligId(1);
		entity1.setName("John");
		entity1.setMobile((long) 1247483622);
		entity1.setGender('M');
		entity1.setSsn((long) 673446828);
		entity1.setPlanName("SNAP");
		entity1.setPlanStatus("Approved");
		repo.save(entity1);
		
		EligDtls entity2=new EligDtls();
		entity2.setEligId(2);
		entity2.setName("Daniel");
		entity2.setMobile((long) 782387322);
		entity2.setGender('M');
		entity2.setSsn((long) 943894729);
		entity2.setPlanName("APLAN");
		entity2.setPlanStatus("Denied");
		repo.save(entity2);
		
		EligDtls entity3=new EligDtls();
		entity3.setEligId(3);
		entity3.setName("Rosita");
		entity3.setMobile((long) 74764838);
		entity3.setGender('F');
		entity3.setSsn((long) 457798745);
		entity3.setPlanName("CCAP");
		entity3.setPlanStatus("Closed");
		repo.save(entity3);
	}

}
