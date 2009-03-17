package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import es.imim.bg.ztools.aggregation.AggregatorFactory;
import es.imim.bg.ztools.aggregation.IAggregator;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.table.sort.SortCriteria;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialog.sort.SortDialog;
import es.imim.bg.ztools.ui.dialog.sort.SortDialogSimple;

public class SortAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;
	private SortSubject sortSubject;
	
	public enum SortSubject {
		ROW, COLUMN, BOTH
	}

	public SortAction(SortSubject sortSubject) {
		super(null);	
		this.sortSubject = sortSubject;
		switch (this.sortSubject) {
		case COLUMN:
			setName("Sort columns ...");
			setDesc("Sort columns ...");
			break;
		case ROW:
			setName("Sort rows ...");
			setDesc("Sort rows ...");
			break;
		case BOTH:
			setName("Sort rows and columns ...");
			setDesc("Sort rows and columns ...");
			break;
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITable table = getTable();
		if (table == null)
			return;
				
		//select properties
		List<IElementProperty> cellProps = table.getCellAdapter().getProperties();
		ListIterator<IElementProperty> i = cellProps.listIterator();
		Object[] props = new Object[cellProps.size()];
		int counter = 0;
		while (i.hasNext()) {
			IElementProperty ep = i.next();
			props[counter] = ep.getName();
			counter++;
		}
		
		SortDialogSimple d = new SortDialogSimple(
				AppFrame.instance(),
				getName(),
				true,
				props,
				AggregatorFactory.getAggregatorsArray());
		
		List<SortCriteria> criteriaList = d.getCriteriaList();
		if (criteriaList.size() == 0)
			return;
		
		//SortDialog d;		
		switch (this.sortSubject) {
			case COLUMN:
				//d = new SortColumnsDialog(AppFrame.instance(), props);
				//criteriaList = d.getValueList();
				new SortColumnsAction(table, criteriaList, false);
				AppFrame.instance()
					.setStatusText("Columns sorted.");
				break;
			case ROW:
				//d = new SortRowsDialog(AppFrame.instance(), props);
				//criteriaList = d.getValueList();
				new SortRowsAction(table, criteriaList, false);
				AppFrame.instance()
					.setStatusText("Rows sorted.");
				break;
			case BOTH:
				//d = new SortRowsDialog(AppFrame.instance(), props);
				//criteriaList = d.getValueList();
				new SortRowsAction(table, criteriaList, false);
				new SortColumnsAction(table, criteriaList, false);
				AppFrame.instance()
					.setStatusText("Rows and columns sorted.");
				break;
		}
				

		
		
		
	}
}
