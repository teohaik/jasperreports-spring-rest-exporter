package gr.teohaik.jasperdemo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import gr.teohaik.jasperdemo.beans.OrderModel;
import gr.teohaik.jasperdemo.beans.SampleBean;
import gr.teohaik.jasperdemo.service.MockOrderService;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("api")
public class JasperDemoController {

	@Resource
	MockOrderService mockOrderService;

	@GetMapping(value = "doc")
	public void getDocument(HttpServletResponse response) throws IOException, JRException {

		String sourceFileName = ResourceUtils
				.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "SampleJasperTemplate.jasper").getAbsolutePath();
		List<SampleBean> dataList = new ArrayList<SampleBean>();
		SampleBean sampleBean = new SampleBean();
		sampleBean.setName("some name");
		sampleBean.setColor("red");
		dataList.add(sampleBean);
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		Map parameters = new HashMap();
		JasperPrint jasperPrint = JasperFillManager.fillReport(sourceFileName, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
	}

	@GetMapping(value = "invoice")
	public void getInvoice(HttpServletResponse response) throws IOException, JRException {

		// Fetching the .jrxml file from the resources folder.
		final InputStream stream = this.getClass().getResourceAsStream("/invoice.jrxml");

		// Compile the Jasper report from .jrxml to .japser
		final JasperReport report = JasperCompileManager.compileReport(stream);


		OrderModel order = mockOrderService.getOrderByCode(" PLAISIO AEBE");

		final Map<String, Object> parameters = parameters(order, Locale.forLanguageTag("en"));

		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(Collections.singletonList("Invoice"));

		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, beanColDataSource);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		response.setContentType("application/pdf");
		response.addHeader("Content-Disposition", "inline; filename=jasper.pdf;");
	}



	// Fill template order params
	private Map<String, Object> parameters(OrderModel order, Locale locale) {
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("logo", getClass().getResourceAsStream("/logo-bank.png"));
		parameters.put("order",  order);
		parameters.put("REPORT_LOCALE", locale);
		return parameters;
	}
}
