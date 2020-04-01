package graphic_interface;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import back_end.XmlDocument;

@SuppressWarnings("serial")
public class ArticleTable extends JTable {
	
	private DefaultTableModel _model = new DefaultTableModel();
	private Map<Integer, XmlDocument> _dictionaryDoc = new HashMap<Integer, XmlDocument>();
	
	public ArticleTable() {
		super();
		setModel(_model);
		_model.addColumn("Título");
		_model.addColumn("Autor");
		_model.addColumn("Categoria");
		_model.addColumn("Data de publicação");
        setAutoCreateRowSorter(true);
        getRowSorter().toggleSortOrder(0);
        getTableHeader().setReorderingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void addRowXmlDoc(int rowInx, XmlDocument doc) {
		_dictionaryDoc.put(rowInx, doc);
	}
	
	public XmlDocument getRowXmlDoc(int inxRow) {
		return _dictionaryDoc.get(inxRow);
	}
	
	public int getSelectedRowModelIndex() {
		int inxView = getSelectedRow();
		if (inxView >= 0) {
			return getRowSorter().convertRowIndexToModel(inxView);
		}
		else {
			return -1;
		}
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
