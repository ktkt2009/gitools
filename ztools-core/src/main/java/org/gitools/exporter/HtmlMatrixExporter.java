package org.gitools.exporter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.figure.MatrixFigure;

import edu.upf.bg.GenericFormatter;

public class HtmlMatrixExporter extends AbstractHtmlExporter {
	
	public HtmlMatrixExporter() {
		super();
	}
	
	public void exportMatrixFigure(MatrixFigure figure) {
        
		File templatePath = getTemplatePath();
		if (templatePath == null)
			throw new RuntimeException("Unable to locate templates path !");
		
		try {
			copy(new File(templatePath, "media"), basePath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		TemplateEngine eng = new TemplateEngine();
		eng.setFileLoaderPath(templatePath);
		eng.init();
		
		try {
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("fmt", new GenericFormatter());
			context.put("figure", figure);
			context.put("matrix", figure.getMatrixView());
			context.put("cellDecoration", new ElementDecoration());
			eng.setContext(context);
			
			File file = new File(basePath, indexName);
			//eng.loadTemplate("/vm/exporter/html/matrixfigure.vm");
			eng.loadTemplate("matrixfigure.vm");
			eng.render(file);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
