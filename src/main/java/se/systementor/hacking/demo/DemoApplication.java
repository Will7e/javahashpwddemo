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
			e.printStackTrace();
		}
		while(true) {
			System.console().printf("Ange hash:");
			String hash = System.console().readLine();
			//TODO Läs igenom filen hashadelosen.txt
			//Hittar du denna hash så är ju lösenordet på samma rad!
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

}
