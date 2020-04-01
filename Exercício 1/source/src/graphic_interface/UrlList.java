package graphic_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;

import back_end.XmlDocument;

@SuppressWarnings("serial")
public class UrlList extends JList<String> {
	
	private DefaultListModel<String> _model = new DefaultListModel<String>();
	
	private JPopupMenu _popupmenu = new JPopupMenu();
	private XmlDocument _doc;
	private String urlsFilePath = "urls.txt";
	
	public UrlList() {
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setModel(_model);
		loadUrls();
		
		add(_popupmenu);
		JMenuItem editItem = new JMenuItem("edit");
		_popupmenu.add(editItem);
		editItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int inx = getSelectedIndex();
				
				String newName = JOptionPane.showInputDialog(null, "Escolha uma nova URL:", "Editar", JOptionPane.PLAIN_MESSAGE);
				if (newName != null && ! newName.equals("")) {
					_model.remove(inx);
					_model.insertElementAt(newName, inx);
					saveUrls();
				}
			}
		});
		JMenuItem deleteItem = new JMenuItem("delete");
		_popupmenu.add(deleteItem);
		deleteItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int inx = getSelectedIndex();
				_model.remove(inx);
				saveUrls();
			}
		});
		
		addMouseListener(new UrlMouseListener(this));
	}
	
	public void addUrl(String url) {
		_model.addElement(url);		
	}
	
	public void rmUrlInx(int index) {
		_model.remove(index);
	}
	
	public void setUrlXmlDoc(XmlDocument doc) {
		_doc = doc;
	}
	
	public XmlDocument getUrlXmlDoc() {
		return _doc;
	}
	
	public JPopupMenu getPopupMenu() {
		return _popupmenu;
	}
	
	public void saveUrls() {
		
		File txtFile = new File(urlsFilePath);
		try {
			PrintStream printStream = new PrintStream(txtFile);
			try {
				for (int inx = 0; inx < _model.size(); inx++) {
					printStream.println(_model.get(inx));
				}
			}
			catch(Exception ex) {
				throw ex;
			}
			finally {
				printStream.close();
			}
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void loadUrls() {
		File txtFile = new File(urlsFilePath);
		try {
			txtFile.createNewFile();
			FileReader fileReader = new FileReader(txtFile);
			BufferedReader reader = new BufferedReader(fileReader);
			try {
				String url;
				while((url = reader.readLine()) != null) {
					_model.addElement(url);
				}
			}
			catch (Exception ex) {
				throw ex;
			}
			finally {
				reader.close();
			}
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
