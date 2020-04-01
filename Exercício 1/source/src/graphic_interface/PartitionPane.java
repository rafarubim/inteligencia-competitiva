package graphic_interface;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PartitionPane extends JPanel {
	
	JComponent _content = null;
	
	public PartitionPane(String title) {
		setLayout(new BorderLayout());
		
		JLabel titleLabel = new JLabel(title);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(titleLabel, BorderLayout.PAGE_START);
	}
	
	public void setContent(JComponent content) {
		if (_content != null) {
			remove(_content);
		}
		_content = content;
		add(_content, BorderLayout.CENTER);
	}
}
