package defaultPackage;

import java.awt.EventQueue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

	public static void main(String[] args) {
		
		//Deletes the old versions of the testNG results
		try {
			Files.deleteIfExists(Paths.get(System.getProperty("user.dir")+"\\test-output\\index.html"));
		} catch (IOException e) {
		}

		//Starts the window
		EventQueue.invokeLater(new FirstScreen());
	}
	
}