package graphic_interface;

import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import back_end.HttpRequest;
import back_end.XmlDocument;

public class UrlListListener implements ListSelectionListener {
	
	private UrlList _urlList;
	private ArticleTable _articleTable;
	
	public UrlListListener(UrlList urlList, ArticleTable articleTable) {
		_urlList = urlList;
		_articleTable = articleTable;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		DefaultTableModel tableModel = (DefaultTableModel) _articleTable.getModel();
		tableModel.setRowCount(0);
		
		UrlList urlList = (UrlList) e.getSource();
		
		String selectedUrl =(String) urlList.getSelectedValue();
		
		if (selectedUrl != null) {
			try {
				String rssInfo = HttpRequest.get(selectedUrl);
				
				if (rssInfo != null) {
					XmlDocument entireXml = new XmlDocument(rssInfo);
					
					_urlList.setUrlXmlDoc(entireXml);
					
					List<XmlDocument> articleDocs = entireXml.getTagDoc("channel").getTagDocs("item");
					
					for(int inx = 0; inx < articleDocs.size(); inx++) {
						XmlDocument currentArticle = articleDocs.get(inx);
						String title = null;
						String author = null;
						String category = null;
						String pubDate = null;
						
						try {
							title = currentArticle.getTagText("title");
						}
						catch(Exception ex) {}
						try {
							author = currentArticle.getTagText("author");
						}
						catch(Exception ex) {}
						try {
							category = currentArticle.getTagText("category");
						}
						catch(Exception ex) {}
						try {
							pubDate = currentArticle.getTagText("pubDate");
						}
						catch(Exception ex) {}
						
						tableModel.addRow(new String[]{title, author, category, pubDate});
						_articleTable.addRowXmlDoc(inx, currentArticle);
					}
				}
				//http://feeds.bbci.co.uk/news/world/rss.xml
			}
			catch(Exception ex) {
				
			}
		}
	}
}
