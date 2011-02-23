package diva.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import diva.VariabilityModel;

public class ContextEditorPane extends DiVATableEditorPane {
	
	public ContextEditorPane(DiVATableEditor editor, IWorkbenchPage page, IWorkbenchPart part) {
		super(editor, page, part);
	}
	
	/** Initializes the table tree viewer, creating columns and setting up editing.
	 *  {@link #treeViewerWithColumns} should be valid before calling.
	 */
	protected void initializeTableTreeViewer() {

		Tree tree = treeViewerWithColumns.getTree();
		tree.setLayoutData(new FillLayout());
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		//tree.setBackground(DiVABackgroundProvider.ctxColor);

		createColumns(treeViewerWithColumns);

		treeViewerWithColumns
				.setContentProvider(new ContextContentProvider(editor.getAdaptedFactory()));
		
		// get the model
		Resource r = editor.getEditingDomain().getResourceSet().getResources().get(0);
		
		// Find the VariabilityModel and set the input
		for (EObject o : r.getContents()) {
			if (o instanceof VariabilityModel) {
				treeViewerWithColumns.setInput(o);
				break;
			}
		}
		
		// resize all the columns
		treeViewerWithColumns.expandAll();
		for(TreeColumn c : columns) c.pack();
		treeViewerWithColumns.collapseAll();
	}
	
	class ContextContentProvider extends AdapterFactoryContentProvider {

		public ContextContentProvider(AdapterFactory adapterFactory) {
			super(adapterFactory);
		}

		@Override
		public Object[] getChildren(Object object) {
			if (object instanceof VariabilityModel) {
				return ((VariabilityModel)object).getContext().toArray();
			}
			return super.getChildren(object);
		}

		@Override
		public boolean hasChildren(Object object) {
			if (object instanceof VariabilityModel) {
				return ((VariabilityModel)object).getContext().size() != 0;
			}
			return super.hasChildren(object);
		}

		@Override
		public Object[] getElements(Object object) {
			if (object instanceof VariabilityModel) {
				return ((VariabilityModel)object).getContext().toArray();
			}
			return super.getElements(object);
		}
	}

	/**
	 * @return list of {@link ColumnDescriptor}s
	 * @generated
	 */
	protected List<ColumnDescriptor> getColumnDescriptors() {
		ArrayList<ColumnDescriptor> list = new ArrayList<ColumnDescriptor>();
		list.add(new ColumnDescriptor("Name", null, "name"));
		list.add(new ColumnDescriptor("ID", null, "id"));
		list.add(new ColumnDescriptor("Values", null, "literal"));
		return list;
	}
	

}
