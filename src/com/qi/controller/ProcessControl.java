package com.qi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qi.entity.BaseReturn;
import com.qi.service.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessControl {
	
	@Autowired
	private ProcessService processService;
	
	@RequestMapping("/listProcess")
	public @ResponseBody BaseReturn listProcess(){
		BaseReturn baseReturn = new BaseReturn();
		return baseReturn;
		
	}
}
