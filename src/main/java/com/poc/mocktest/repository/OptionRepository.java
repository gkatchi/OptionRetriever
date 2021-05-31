package com.poc.mocktest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.poc.mocktest.model.OptionData;

public interface OptionRepository extends JpaRepository<OptionData, Long> {

	

}
