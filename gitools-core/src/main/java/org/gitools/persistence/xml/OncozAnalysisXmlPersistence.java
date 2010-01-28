package org.gitools.persistence.xml;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gitools.analysis.htest.oncozet.OncozAnalysis;
import org.gitools.model.xml.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.persistence.PersistenceContext;

public class OncozAnalysisXmlPersistence
		extends AbstractXmlPersistence<OncozAnalysis> {

	public OncozAnalysisXmlPersistence() throws JAXBException {
		super(OncozAnalysis.class);
	}
	
	@Override
	protected XmlAdapter<?, ?>[] createAdapters() {
		PersistenceContext context = getPersistenceContext();
		return new XmlAdapter<?, ?>[] {
			new PersistenceReferenceXmlAdapter(context)
		};
		
		/*return new XmlAdapter[] {
				new FileXmlAdapter(pathResolver),
				new AnnotationMatrixXmlAdapter(pathResolver),
				new MatrixXmlAdapter(pathResolver),
				new ModuleMapXmlAdapter(context)	};*/
	}

}