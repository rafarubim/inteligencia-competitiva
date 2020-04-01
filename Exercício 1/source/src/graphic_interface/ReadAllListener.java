package graphic_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import back_end.XmlDocument;

public class ReadAllListener implements ActionListener {
	
	private ArticleTable _articleTable;
	private JFrame _descFrame = null;
	
	public ReadAllListener(ArticleTable articleTable) {
		_articleTable = articleTable;
	}

	public void actionPerformed(ActionEvent e) {
		
		int inxRow = _articleTable.getSelectedRowModelIndex();
		
		XmlDocument xmlDoc = null;
		
		xmlDoc = _articleTable.getRowXmlDoc(inxRow);
		
		if (xmlDoc != null) {
			if (_descFrame != null) {
				_descFrame.dispose();
			}
			_descFrame = new JFrame("Artigo");
			_descFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			_descFrame.setBounds(500, 200, 800, 600);
			_descFrame.setVisible(true);
			
			JPanel contentPane = (JPanel) _descFrame.getContentPane();
			
			JScrollPane scrollPane = new JScrollPane();
			contentPane.add(scrollPane);
			
			JTextArea textArea = new JTextArea();
			scrollPane.setViewportView(textArea);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			
			StringBuilder strBuilder = new StringBuilder();
			
			String title = null;
			String author = null;
			String source = null;
			String category = null;
			String pubDate = null;
			String link = null;
			String description = null;
			String comments = null;
			
			try {
				title = xmlDoc.getTagText("title");
			}
			catch (Exception ex) {}
			try {
				author = xmlDoc.getTagText("author");
			}
			catch (Exception ex) {}
			try {
				source = xmlDoc.getTagText("source");
			}
			catch (Exception ex) {}
			try {
				category = xmlDoc.getTagText("category");
			}
			catch (Exception ex) {}
			try {
				pubDate = xmlDoc.getTagText("pubDate");
			}
			catch (Exception ex) {}
			try {
				link = xmlDoc.getTagText("link");
			}
			catch (Exception ex) {}
			try {
				description = xmlDoc.getTagText("description");
			}
			catch (Exception ex) {}
			try {
				comments = xmlDoc.getTagText("comments");
			}
			catch (Exception ex) {}
			
			if (source != null) {
				strBuilder.append("Source: " + source + "\n");
			}
			strBuilder.append("Link: " + link + "\n");
			if (category != null) {
				strBuilder.append("Category: " + category + "\n");
			}
			if (author != null) {
				strBuilder.append("Author: " + author + "\n");
			}
			if (pubDate != null) {
				strBuilder.append(pubDate + "\n");
			}
			strBuilder.append("\t\t" + title + "\n\n");
			strBuilder.append(description + "\n\n");
			if (comments != null) {
				strBuilder.append("Comments: " + comments);
			}
			
			textArea.setText(strBuilder.toString());
		}
	}
	
}
