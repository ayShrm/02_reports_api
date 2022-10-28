package com.ayush.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ayush.request.SearchRequest;
import com.ayush.response.SearchResponse;

public interface ReportsService {

	public List<String> getUniquePlanName();

	public List<String> getUniquePlanStatuses();

	public List<SearchResponse> search(SearchRequest request);

	//public HttpServletResponse generateExcel();
	public void generateExcel(HttpServletResponse response) throws Exception;

	public void generatePdf(HttpServletResponse response) throws Exception;
}
