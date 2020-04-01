package graphic_interface;

import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import back_end.XmlDocument;

public class TableListener implements ListSelectionListener {
	
	ArticleTable _articleTable;
	private JTextArea _descText;
	
	public TableListener(ArticleTable articleTable, JTextArea descText) {
		_articleTable = articleTable;
		_descText = descText;
	}

	public void valueChanged(ListSelectionEvent e) {
		
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		int inxRow = _articleTable.getSelectedRowModelIndex();
		
		XmlDocument xmlDoc = _articleTable.getRowXmlDoc(inxRow);
		
		String description = null;
		try {
			description = xmlDoc.getTagText("description");
		}
		catch(Exception ex) {}
		
		if (description != null) {
			_descText.setText(description);
		}
		else {
			_descText.setText("");
		}
	}
}
