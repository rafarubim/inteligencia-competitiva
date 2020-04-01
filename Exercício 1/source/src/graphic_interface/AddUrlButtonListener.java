package graphic_interface;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class AddUrlButtonListener implements ActionListener {
	private UrlList _urlList;
	
	public AddUrlButtonListener(UrlList urlList) {
		_urlList = urlList;
	}

	public void actionPerformed(ActionEvent e) {
		String result = JOptionPane.showInputDialog(null, "Digite uma URL:", "Adicionar", JOptionPane.PLAIN_MESSAGE);
		if (result != null && ! result.equals("")) {
			_urlList.addUrl(result);
			_urlList.saveUrls();
		}
	}
}
