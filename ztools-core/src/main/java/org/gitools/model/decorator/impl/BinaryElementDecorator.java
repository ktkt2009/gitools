package org.gitools.model.decorator.impl;

import java.awt.Color;

import org.gitools.datafilters.BinaryCutoffFilter;
import org.gitools.datafilters.BinaryCutoffFilter.BinaryCutoffCmp;
import org.gitools.matrix.MatrixUtils;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.matrix.element.IElementAdapter;

import edu.upf.bg.GenericFormatter;
import edu.upf.bg.colorscale.util.ColorConstants;

public class BinaryElementDecorator extends ElementDecorator {

	private static final long serialVersionUID = 8832886601133057329L;

	private static final Color defaultColor = new Color(20, 120, 250);
	
	private int valueIndex;
	
	private double cutoff;
	private BinaryCutoffCmp cutoffCmp;
	
	private Color color;
	private Color nonSignificantColor;
	
	private GenericFormatter fmt = new GenericFormatter("<");
	
	public BinaryElementDecorator(IElementAdapter adapter) {
		super(adapter);
	
		valueIndex = getPropertyIndex(new String[] {
				"value" });
		
		cutoff = 1.0;
		
		cutoffCmp = BinaryCutoffFilter.EQ;
		
		color = defaultColor;
		nonSignificantColor = ColorConstants.nonSignificantColor;
	}

	public int getValueIndex() {
		return valueIndex;
	}
	
	public void setValueIndex(int valueIndex) {
		this.valueIndex = valueIndex;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public BinaryCutoffCmp getCutoffCmp() {
		return cutoffCmp;
	}
	
	public void setCutoffCmp(BinaryCutoffCmp cutoffCmp) {
		this.cutoffCmp = cutoffCmp;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public double getCutoff() {
		return cutoff;
	}
	
	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getNonSignificantColor() {
		return nonSignificantColor;
	}
	
	public void setNonSignificantColor(Color color) {
		this.nonSignificantColor = color;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	@Override
	public void decorate(
			ElementDecoration decoration, 
			Object element) {
		
		decoration.reset();
		
		if (element == null) {
			decoration.setBgColor(ColorConstants.emptyColor);
			decoration.setToolTip("Empty cell");
			return;
		}
		
		Object value = adapter.getValue(element, valueIndex);
		
		double v = MatrixUtils.doubleValue(value);
		
		boolean isSig = cutoffCmp.compare(v, cutoff);
		
		final Color c = Double.isNaN(v) ? ColorConstants.notANumberColor
				: isSig ? color : nonSignificantColor;
		
		decoration.setBgColor(c);
		decoration.setToolTip(fmt.format(v));
	}
}
