package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Status;


public interface StatusService {

	List<Status> ReadListStatus();
	
}
