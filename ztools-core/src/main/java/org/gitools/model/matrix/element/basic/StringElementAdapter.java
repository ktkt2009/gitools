package org.gitools.model.matrix.element.basic;

import javax.xml.bind.annotation.XmlRootElement;

import org.gitools.model.matrix.element.AbstractElementAdapter;


@XmlRootElement
public class StringElementAdapter 
		extends AbstractElementAdapter {

	public StringElementAdapter() {
		super(String.class);
	}
	
	@Override
	public Object getValue(Object element, int index) {
		return element;
	}

	@Override
	public void setValue(Object element, int index, Object value) {
		throw new UnsupportedOperationException(
				getClass().getSimpleName() + " doesn't support change string value.");
	}

}