package se.systementor.hacking.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	
    @Override
    public void run(String... args) {
		try {
			ensurePreHashedFileExists();
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(e);
		}
		while(true) {
			Scanner scan = new Scanner(System.in);
			System.out.println("Ange hash:");
			String hash = scan.nextLine();
			//TODO Läs igenom filen hashadelosen.txt
			String result =	findPassword(hash);
			//Hittar du denna hash så är ju lösenordet på samma rad!
			System.out.println(result);
		}
		
    }


	private void ensurePreHashedFileExists() throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		File preHashadeLosen = new File("hashadelosen.txt");
		if(preHashadeLosen.exists() && !preHashadeLosen.isDirectory()) {
			return;
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("hashadelosen.txt", true)));

		File dictionaryFile = new File("example.dict.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(dictionaryFile))) {
			String line;
			while ((line = br.readLine()) != null) {
			   String hash = getHash(line);
			   out.println( hash + " " + line );
			}
		}		

				 
	}


	private String getHash(String line) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(line.getBytes());
		String myHash = String.format("%032x", new BigInteger(1, md.digest()));
		return myHash;
	}


	public String findPassword(String hash) {
		// The name of the file to open
		String fileName = "hashadelosen.txt";

		// This will reference one line at a time
		String line = null;
		try {
			// FileReader reads text files in the default encoding
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				// If there is matching with hash and first line in hashadelosen.txt
				if (line.startsWith(hash)) {
						// Extract the substring after the checkString
						String nextString = line.substring(hash.length());
						System.out.println("The text after '" + hash + "' is: " + nextString);
						return nextString;
				}
			}
			// Always close files
			bufferedReader.close();
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		System.out.println("No text found starting with '" + hash + "'.");
		return "";
	}

}
