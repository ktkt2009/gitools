package es.imim.bg.ztools.ui.colormatrix;

import java.text.DecimalFormat;

import es.imim.bg.colorscale.ColorScale;
import es.imim.bg.colorscale.LogColorScale;

public class CellDecorationConfig {
	public boolean showColors = true;
	
	public DecimalFormat textFormat = new DecimalFormat("#.######");
	
	public CellDecoration.TextAlignment textAlign = 
		CellDecoration.TextAlignment.left;
	
	public ColorScale scale = new LogColorScale();
	
	public CellDecorationConfig() {
		this.showColors = true;

		this.textFormat = new DecimalFormat("#.######");
		
		this.textAlign = CellDecoration.TextAlignment.left;
		
		this.scale = new LogColorScale();
	}
}