package org.kennah.horse.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.kennah.horse.server.model.Race;

public class Utility {
	
	public static void writeObjectFile(List<Race> obj, String nameOfFile){
        try(FileOutputStream f_out = new FileOutputStream(nameOfFile+".data", false);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);) {
            obj_out.writeObject(obj);
        } catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	@SuppressWarnings("unchecked")
	public static List<Race> readObjectFile(String nameOfFile) {
		nameOfFile = nameOfFile+".data";
		List<Race> meet = null;
		File file = new File(nameOfFile);
		if(file.exists()){
			try (FileInputStream fin = new FileInputStream(file);
		        ObjectInputStream objIn = new ObjectInputStream(fin);){
	            Object obj = objIn.readObject();
	            if (obj instanceof List) {
	            	meet = (List<Race>) obj;
	            }
	        } catch (ClassNotFoundException cnfe) {
	        	cnfe.printStackTrace();
	            System.out.println("Exception " + cnfe);
	        } catch (FileNotFoundException fnfe) {
	        	fnfe.printStackTrace();
	            System.out.println("Exception " + fnfe);
	        } catch (IOException ioe) {
	        	ioe.printStackTrace();
	            System.out.println("Exception " + ioe);
	        }
		}else{
			meet = new ArrayList<>();
		}
		return meet;
    }
}
