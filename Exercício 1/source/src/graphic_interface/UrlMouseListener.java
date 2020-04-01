package graphic_interface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UrlMouseListener extends MouseAdapter {
	
	private UrlList _urlList;
	
	public UrlMouseListener(UrlList urlList) {
		_urlList = urlList;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			_urlList.setSelectedIndex(_urlList.locationToIndex(e.getPoint()));
			_urlList.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
