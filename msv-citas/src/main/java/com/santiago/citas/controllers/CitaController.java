package com.santiago.citas.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.santiago.citas.dto.CitaRequest;
import com.santiago.citas.dto.CitaResponse;
import com.santiago.citas.services.CitaService;
import com.santiago.commons.controllers.CommonController;


@RestController
@RequestMapping("api/citas")
public class CitaController  extends CommonController<CitaRequest, CitaResponse, CitaService>{
	
	public CitaController(CitaService service) {
		super(service);
	}

}
