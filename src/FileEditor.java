import java.util.*;

import net.dv8tion.jda.core.entities.User;

import java.io.*;

public class FileEditor {
	
	public static void setgamestate(String state) {
		if(state == "gameover" || state == "setup" || state == "distribution" || state == "inprogress" || state == "blank") {
			try{
		         FileOutputStream fos= new FileOutputStream("gamestate.txt");
		         ObjectOutputStream oos= new ObjectOutputStream(fos);
		         oos.writeObject(state);
		         oos.close();
		         fos.close();
		       }catch(IOException ioe){
		            ioe.printStackTrace();
		        }
		}
		
		else
			return;
	}
	
	public static String getgamestate() {
		String state;
		try
        {
            FileInputStream fis = new FileInputStream("gamestate.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            state = (String)ois.readObject();
            ois.close();
            fis.close();
         }catch(IOException ioe){
             ioe.printStackTrace();
             return null;
          }catch(ClassNotFoundException c){
             System.out.println("Class not found");
             c.printStackTrace();
             return null;
          }
		return state;
	}
	
	public static void addplayer(String id, String filename) {
		ArrayList<String> users = new ArrayList();
		if(recallarray(filename) != null) {
			users = recallarray(filename);
		}
		users.add(id);
		storearray(users, "players.txt");
	}
	
	public static void removeplayer(String id, String filename) {
		ArrayList<String> users = new ArrayList();
		if(recallarray(filename) == null) {
			return;
		}
		users = recallarray(filename);
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i) == id) {
				users.remove(i);
			}
		}
		storearray(users, "players.txt");
	}
	
	
	public static void storearray(ArrayList<String> array, String filename) {
		try{
	         FileOutputStream fos= new FileOutputStream(filename);
	         ObjectOutputStream oos= new ObjectOutputStream(fos);
	         oos.writeObject(array);
	         oos.close();
	         fos.close();
	       }catch(IOException ioe){
	            ioe.printStackTrace();
	        }
	}
	
	public static ArrayList<String> recallarray(String filename){
		ArrayList<String> users = new ArrayList();
		try
        {
            FileInputStream fis = new FileInputStream("players.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (ArrayList<String>)ois.readObject();
            ois.close();
            fis.close();
         }catch(IOException ioe){
             ioe.printStackTrace();
             return null;
          }catch(ClassNotFoundException c){
             System.out.println("Class not found");
             c.printStackTrace();
             return null;
          }
        
        return users;
	}
	
	public static void clearfile(String filename) {	
		try {
			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream save = new ObjectOutputStream(file);
			save.writeObject("");
			save.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
