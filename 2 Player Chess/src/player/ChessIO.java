package player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChessIO {
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static String readMove(){
		while (true) {
			try {
				return br.readLine();
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	
}
