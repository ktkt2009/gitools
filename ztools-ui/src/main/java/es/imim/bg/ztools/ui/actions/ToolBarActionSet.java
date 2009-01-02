package es.imim.bg.ztools.ui.actions;

import javax.swing.JToolBar;

public final class ToolBarActionSet extends ActionSet {

	private static final long serialVersionUID = 6924230823891805344L;

	public ToolBarActionSet() {
		super(new BaseAction[] {
				FileActionSet.openAnalysisAction,
				FileActionSet.closeAction,
				BaseAction.separator,
				/*EditActionSet.columnSelectionModeAction,
				EditActionSet.rowSelectionModeAction,
				EditActionSet.cellSelectionModeAction,
				BaseAction.separator,*/
				EditActionSet.selectAllAction,
				EditActionSet.unselectAllAction,
				BaseAction.separator,
				TableActionSet.hideSelectedColumnsAction,
				TableActionSet.showAllColumns,
				TableActionSet.hideSelectedRowsAction,
				TableActionSet.showAllRows,
				BaseAction.separator,
				TableActionSet.moveColsLeftAction,
				TableActionSet.moveColsRightAction,
				TableActionSet.moveRowsUpAction,
				TableActionSet.moveRowsDownAction,
				BaseAction.separator,
				TableActionSet.sortSelectedColumnsAction	
		});
	}
	
	public JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		
		for (BaseAction a : actions)
			if (a instanceof SeparatorAction)
				toolBar.addSeparator();
			else
				toolBar.add(a);
		
		return toolBar;
	}
}
