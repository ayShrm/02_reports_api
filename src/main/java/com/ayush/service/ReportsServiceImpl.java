package com.ayush.service;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.ayush.entity.EligDtls;
import com.ayush.repository.EligibilityDtlsRepo;
import com.ayush.request.SearchRequest;
import com.ayush.response.SearchResponse;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReportsServiceImpl implements ReportsService {

	@Autowired
	private EligibilityDtlsRepo elgRepo;

	@Override
	public List<String> getUniquePlanName() {
		return elgRepo.findPlanNames();
	}

	@Override
	public List<String> getUniquePlanStatuses() {
		return elgRepo.findPlanStatuses();
	}

	@Override
	public List<SearchResponse> search(SearchRequest request) {

		List<SearchResponse> response = new ArrayList<>();

		EligDtls queryBuilder = new EligDtls();

		String planName = request.getPlanName();
		if (planName != null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}

		String planStatus = request.getPlanStatus();
		if (planStatus != null && !planStatus.equals("")) {
			queryBuilder.setPlanStatus(planStatus);
		}

		LocalDate planStartDate = request.getPlanStartDate();
		if (planStartDate != null) {
			queryBuilder.setPlanStartDate(planStartDate);
		}

		LocalDate planEndDate = request.getPlanEndDate();
		if (planEndDate != null) {
			queryBuilder.setPlanEndDate(planEndDate);
		}

		Example<EligDtls> example = Example.of(queryBuilder);

		List<EligDtls> entities = elgRepo.findAll(example);
		for (EligDtls entity : entities) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(entity, sr); // BeanUtils is used to copy the data from one object to another
													// object if the variables are same.
			response.add(sr);
		}
		return response;
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception{
		List<EligDtls> entities = elgRepo.findAll();

//			String[] header = { "S.No.", "Name", "Mobile", "Gender", "SSN" };

//			for (int col = 0; col < header.length; col++) {
//				Cell cell = headerRow.createCell(col);
//				cell.setCellValue(header[col]);
//			}
		
			
		try (HSSFWorkbook workBook = new HSSFWorkbook()) {
			HSSFSheet sheet = workBook.createSheet();
			HSSFRow headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue("Name");
			headerRow.createCell(1).setCellValue("Email");
			headerRow.createCell(2).setCellValue("Mobile");
			headerRow.createCell(3).setCellValue("Gender");
			headerRow.createCell(4).setCellValue("SSN");

			int rowIdx = 1;
			for (EligDtls entity : entities) {
				HSSFRow dataRow = sheet.createRow(rowIdx++);
				dataRow.createCell(0).setCellValue(entity.getName());
				dataRow.createCell(1).setCellValue(entity.getEmail());
				dataRow.createCell(2).setCellValue(entity.getMobile());
				dataRow.createCell(3).setCellValue((String.valueOf(entity.getGender())));
				dataRow.createCell(4).setCellValue(entity.getSsn());
			}
			ServletOutputStream outputStream = response.getOutputStream();
			workBook.write(outputStream);
			workBook.close();
			outputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
		List<EligDtls> entities = elgRepo.findAll();

		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("Search Report", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);

		Font font1 = FontFactory.getFont(FontFactory.HELVETICA);
		font1.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Name", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-mail", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Phone No.", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Gender", font1));
		table.addCell(cell);

		cell.setPhrase(new Phrase("SSN", font1));
		table.addCell(cell);

		for(EligDtls entity: entities) {
			table.addCell(entity.getName());
			table.addCell(entity.getEmail());
			table.addCell(String.valueOf(entity.getMobile()));
			table.addCell(String.valueOf(entity.getGender()));
			table.addCell(String.valueOf(entity.getSsn()));
		}
		document.add(table);
		document.close();
	}

}
