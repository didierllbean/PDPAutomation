package defaultPackage;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import Util.Settings;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import java.awt.Color;

public class FirstScreen extends JFrame implements Runnable {

	private static final long serialVersionUID = 516281090191754013L;
	private JPanel contentPane;
	private Set<String> pages;
	private JTextPane pageNumbersPane;
	private JLabel lblFileName;
	private JLabel lblSelectedFile;
	JButton btnTestProd;
	JButton btnTestStage;
	String temp = "";
	private JLabel lblPages;

	@Override
	public void run() {
		try {
			FirstScreen frame = new FirstScreen();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* Create the frame. */
	@SuppressWarnings({ })
	public FirstScreen() {
		setEnabled(true);
		setForeground(Color.WHITE);
		setBackground(Color.LIGHT_GRAY);
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ecoronado\\Git\\PDPAutomation\\PDPAutomation\\src\\2016-03-15_1139.png_32x32.png"));
		
		setFont(new Font("Dialog", Font.PLAIN, 12));
		
		//Initializes the set of pages to process
		pages = new LinkedHashSet<String>();
		
		//Sets OS Look and Feel
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){
			System.out.println("Couldn't set OS look and feel. Cause: "+e);
		}
		
		//Window definition
		setTitle("Product Page Testing Tool");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 556, 402);
		setResizable(false);
		
		//Locates the window in the middle of any screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
		
		//Sets up main panel
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Adds the Production button
		btnTestProd = new JButton("Production");
		btnTestProd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnTestProd.setEnabled(false);
		btnTestProd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the Product Page numbers
				Util.Settings.production = true;
				proceed();
			}
		});
		btnTestProd.setBounds(397, 225, 141, 41);
		contentPane.add(btnTestProd);
		
		//Adds the Stage button		
		btnTestStage = new JButton("Stage");
		btnTestStage.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnTestStage.setEnabled(false);
		btnTestStage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Set the Product Page numbers
				Util.Settings.production = false;
				proceed();
			}
		});
		btnTestStage.setBounds(397, 171, 141, 41);
		contentPane.add(btnTestStage);
		
		
		//Adds the add file button
		JButton btnAddFile = new JButton("Add File");
		btnAddFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileChooser();
			}
		});
		btnAddFile.setToolTipText("Select a txt file to load page numbers");
		btnAddFile.setBounds(397, 13, 141, 41);
		contentPane.add(btnAddFile);
		
		
		//Adds the cancel button
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnCancel.setBounds(397, 276, 141, 41);
		contentPane.add(btnCancel);
		
		//Adds the pane to enter the page numbers
		pageNumbersPane = new JTextPane();
		pageNumbersPane.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pageNumbersPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				btnTestProd.setEnabled(true);
				btnTestStage.setEnabled(true);
				//If backspace or supreme is selected and the text field is empty, disables the buttons.
				if(arg0.getKeyCode() == 8 || arg0.getKeyCode() == 127){
					if(pageNumbersPane.getCaretPosition()==0){
						btnTestProd.setEnabled(false);
						btnTestStage.setEnabled(false);
					}
				}
				
			}
		});
		pageNumbersPane.setToolTipText("Add Page Numbers");
		pageNumbersPane.setBounds(12, 13, 299, 229);
		
		//Makes the pageNumbersPane scrollable
		JScrollPane scrollPane = new JScrollPane(pageNumbersPane);
		scrollPane.setBounds(22, 42, 337, 275);
		contentPane.add(scrollPane);
		
		//Adds the label to display the selected file name
		lblFileName = new JLabel("");
		lblFileName.setHorizontalAlignment(SwingConstants.CENTER);
		lblFileName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblFileName.setBounds(397, 90, 141, 16);
		lblFileName.setVisible(false);
		contentPane.add(lblFileName);
		
		lblSelectedFile = new JLabel("Selected File:");
		lblSelectedFile.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedFile.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectedFile.setBounds(397, 61, 141, 16);
		lblSelectedFile.setVisible(false);
		contentPane.add(lblSelectedFile);
		
		lblPages = new JLabel("Pages:");
		lblPages.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPages.setBounds(22, 13, 56, 26);
		contentPane.add(lblPages);
		
		//Creates a comboBox to select the type of PDP desired
		@SuppressWarnings("unused")
		String[] options = {"Both PDP","Old PDP","New PDP"};
		
	}
	
	private void proceed(){
		//selectedPDP();
		divideAndClean();
		if(hasValidPageNumbersOnly()){
			setGlobal();
			this.dispose();
			startTest();
			finish();
		}else{
			wrongInput();
		}
	}
	
	/*
	private void selectedPDP(){
		String value = (String)comboBox.getSelectedItem();
		String cookie;
		switch(value){
			case "Old PDP":
				cookie = "A";
				break;
			case "New PDP":
				cookie = "B";
				break;
			default:
				cookie = "BOTH";
				break;
		}
		Util.Settings.llbssCookieValue = cookie;
	}
	*/
	private void finish(){
		//After it's done, tries to open the html file
		String url = System.getProperty("user.dir")+"/test-output/index.html";
	    File file = new File(url);
	    
	    try{
	    	Settings.driver.quit();
	    }catch(NullPointerException n){
	    	JOptionPane.showMessageDialog(null, 
	    			"Couldn't find chromedriver.exe", "Chromedriver error", JOptionPane.ERROR_MESSAGE);
	    	System.out.println(n);
	    	System.exit(1);
	    }
	    
	    //Waits for the reporter to finish creating the file
	    while(!file.exists()){
	    	//Waiting...
	    }
	    
	    try {
	    	//Open the html file
			Desktop.getDesktop().browse(file.toURI());
			
		} catch (IOException e) {
			//If it couldn't open the html file, tries to open the folder
			System.err.println("Couldn't open file. Cause: "+e+". Opening folder.");
			url = System.getProperty("user.dir")+"/test-output";
		    file = new File(url);
		    
		    try{
		    	Desktop.getDesktop().open(file);
		    }catch(IOException e2){
		    	System.err.println("Couldn't open folder. Cause: "+e2+".");
		    }
		}
	    
	    System.exit(0);
	}

	/*If the user typed words or something other than numbers and dividers(\n,',',-)
	  it should display an error message */
	private void wrongInput(){
		pages.clear();
		btnTestProd.setEnabled(false);
		btnTestStage.setEnabled(false);
		lblFileName.setVisible(false);
		lblSelectedFile.setVisible(false);
		JOptionPane.showMessageDialog(contentPane, 
				"Please include Product Page numbers only. ", 
				"Wrong input", JOptionPane.ERROR_MESSAGE);
	}
	
	//Checks if the user entered numbers only and if the page numbers length is correct.
	private boolean hasValidPageNumbersOnly(){
		boolean correct = true;
		for(String page : pages){
			//Checks if each entered line has only numbers with the regex from 0 to 9
			correct = correct && (page.matches("[0-9]+")&& page.length() >= 5 )?true:false;
		}
		return correct;
	}
	
	//Divide the entered text in the panel into an array
	private void divideAndClean(){
		String text = getTextPaneContent();
		//Adds all the lines, removing the ones containing \n (new line) 
		pages.addAll(Arrays.asList(text.split("\\r?\\n")));
		//Removes all blank lines in the text field
		pages.removeIf((s)->s.length()==0);
	}
	
	//Gets the content of the panel
	private String getTextPaneContent(){
		String text = pageNumbersPane.getText();
		
		text = text.replaceAll(",","\n");
		text = text.replaceAll("-","\n");
		return text;
	}
	
	private void setGlobal(){
		Util.Settings.productPages = pages;
	}
	
	private void startTest(){
		TestListenerAdapter tla = new TestListenerAdapter();
		TestNG testng = new TestNG();
		testng.setTestClasses(new Class[] { TestingProductPage.class });
		testng.addListener(tla);
		testng.run();
	}
	
	//Opens the fileChooser displaying only files with .txt extension.
	private void fileChooser(){
		final JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
		fileChooser.setFileFilter(filter);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
		int result = fileChooser.showOpenDialog(contentPane);
		if(result == JFileChooser.APPROVE_OPTION){
			btnTestProd.setEnabled(true);
			btnTestStage.setEnabled(true);
			File selectedFile = fileChooser.getSelectedFile();
			addFileToPane(selectedFile);
		}
	}
	
	//Adds the selected file to the pane
	private void addFileToPane(File file){
		try{
			Stream<String> lines = Files.lines(file.toPath());
			lblSelectedFile.setVisible(true);
			lblFileName.setVisible(true);
			lblFileName.setText(file.getName().toString());
			lines.parallel().forEach( s -> temp += s + "\n" );
			lines.close();
		}catch(IOException e){
			JOptionPane.showMessageDialog(contentPane, "Couldn't open file. Cause: "+e, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		//Inserts the contents of the file to the existing pane
		try{
			 StyledDocument document = (StyledDocument) pageNumbersPane.getDocument();
		     document.insertString(document.getLength(), temp, null);
		}catch(BadLocationException e){
			System.out.println("Couldn't append file, reason: "+ e);
		}
		
	}
}