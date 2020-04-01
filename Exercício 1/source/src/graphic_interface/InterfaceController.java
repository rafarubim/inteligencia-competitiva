package graphic_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class InterfaceController {
	public static void setupInterface() {
		
		JFrame mainFrame = new JFrame("RSS Reader Rafa");
		mainFrame.setSize(1400, 800);
		mainFrame.setLocation(250, 100);
		mainFrame.setResizable(true);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		
		JPanel contentPane = (JPanel) mainFrame.getContentPane();
		contentPane.setBackground(Color.WHITE);
		
		// Define 3 partitions
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		contentPane.add(splitPane);
		
		JPanel leftPane = new JPanel(new BorderLayout());
		splitPane.setLeftComponent(leftPane);
		
		JSplitPane middleRightPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setRightComponent(middleRightPane);
		
		JPanel middlePane = new JPanel(new BorderLayout());
		middleRightPane.setLeftComponent(middlePane);
		
		JPanel rightPane = new JPanel(new BorderLayout());
		middleRightPane.setRightComponent(rightPane);
		
		// First partition
		PartitionPane firstPartitionPane = new PartitionPane("URLs");
		leftPane.add(firstPartitionPane);
		
		JPanel urlsPane = new JPanel(new BorderLayout());
		firstPartitionPane.setContent(urlsPane);
				
		JButton addUrlButton = new JButton("Add URL");
		urlsPane.add(addUrlButton, BorderLayout.PAGE_START);
		
		JScrollPane listScroll = new JScrollPane();
		urlsPane.add(listScroll, BorderLayout.CENTER);
		listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		UrlList urlList = new UrlList();
		listScroll.setViewportView(urlList);
		addUrlButton.addActionListener(new AddUrlButtonListener(urlList));
		
		// Second partition
		PartitionPane secondPartitionPane = new PartitionPane("Artigos");
		middlePane.add(secondPartitionPane);
		
		JScrollPane tableScroll = new JScrollPane();
		secondPartitionPane.setContent(tableScroll);
		
		ArticleTable articleTable = new ArticleTable();
		tableScroll.setViewportView(articleTable);
		
		// Third Partition
		PartitionPane thirdPartitionPane = new PartitionPane("Descrição");
		rightPane.add(thirdPartitionPane);
		
		JPanel descriptionPane = new JPanel(new BorderLayout());
		thirdPartitionPane.setContent(descriptionPane);
		
		JButton readAll = new JButton("Ler tudo");
		descriptionPane.add(readAll, BorderLayout.PAGE_END);
		
		JScrollPane textScroll = new JScrollPane();
		descriptionPane.add(textScroll, BorderLayout.CENTER);
		
		JTextArea descriptionText = new JTextArea();
		textScroll.setViewportView(descriptionText);
		descriptionText.setEditable(false);
		descriptionText.setLineWrap(true);
		
		// Listener
		urlList.addListSelectionListener(new UrlListListener(urlList, articleTable));
		articleTable.getSelectionModel().addListSelectionListener(new TableListener(articleTable, descriptionText));
		readAll.addActionListener(new ReadAllListener(articleTable));

		mainFrame.setVisible(true);
	}
}
