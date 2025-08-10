package com.learning.logi.graph.api.domain.route.dto;

import com.learning.logi.graph.api.domain.optimization.dto.RouteDTO;
import com.learning.logi.graph.api.domain.route.enums.SolverStatus;

import java.util.List;

public record StatusResponse(String jobId, String status, List<RouteDTO> routes) { }

