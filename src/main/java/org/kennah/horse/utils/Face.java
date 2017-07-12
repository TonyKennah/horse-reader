package org.kennah.horse.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Face {

	public static String getFace(){
		StringBuffer sb = new StringBuffer();
		try{
			URL face = new URL("https://gist.githubusercontent.com/TonyKennah/af955187f4b80b44f029c9f776142f6f/raw/e076b6bdebce40687d502404aba57212f9aca2f8/face.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(face.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				sb.append(inputLine+"\n");		
			in.close();
		}
		catch(Exception e){
			System.out.println("EEEEEEK");
		}
		return sb.toString();
	}
}
