import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/*
 * Comp4420 Winter 2015 Project: Implementing QuickSearch and other variants. 
 * Then, comparing and contrasting them.
 * Chibuzor Alumba
 * Jaydee Sendin
 */

public class C4420Proj {
	public static void main(String[] args)
	{
		/*
		 * NOTE: not sure how you want to run this. i.e. if you want 
		 * to pass in the file to read as args or just hard code it. 
		 * so just make the appropriate change
		 */
		long startTime, endTime, elapsedTime;
		double smallestTime, seconds;
		String inputFile = "textFiles/kjvdat.txt";
		String patternString = "";
		
		String file = readFile(inputFile);
		
		patternString = file.substring(30000, 40000);
		
		//START time recorder
		startTime = System.nanoTime();
		testingQuickSearch(patternString, file);
		endTime = System.nanoTime();				
		//END time recorder
		
		//Print time taken in seconds
		elapsedTime = endTime - startTime;
		seconds = ((double)elapsedTime / 1000000000.0);
		System.out.println("Time taken in seconds = " +
			seconds);
			//TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));
		
		//Record the smallest time
		smallestTime = seconds;
		
		//START time recorder
		startTime = System.nanoTime();
		testingFastQuickSearch(patternString, file);
		endTime = System.nanoTime();
		//END time recorder
		
		//Print time taken in seconds
		elapsedTime = endTime - startTime;
		seconds = ((double)elapsedTime / 1000000000.0);
		System.out.println("Time taken in seconds = " +
			seconds);
			
		//Record the smallest time
		smallestTime = Math.min(smallestTime, seconds);
		
		//START time recorder
		startTime = System.nanoTime();
		testingBruteForceSearch(patternString, file);
		endTime = System.nanoTime();
		//END time recorder
		
		//Print time taken in seconds
		elapsedTime = endTime - startTime;
		seconds = ((double)elapsedTime / 1000000000.0);
		System.out.println("Time taken in seconds = " +
			seconds);
			
		//Record the smallest time
		smallestTime = Math.min(smallestTime, seconds);
		
		System.out.println("Smallest time taken in seconds= " +
			smallestTime);
	}
	
	private static void testingBruteForceSearch(String P, String T)
	{
		System.out.println("\nStarting BruteForceSearch");
		int lenP = P.length();
		int lenT = T.length();
		
		for(int i = 0; i < lenT-lenP; i++)
		{
			boolean found = true;
			
			for(int j = 0; j < lenP; j++)
			{
				if(P.charAt(j) != T.charAt(i+j))
				{
					found = false;
				}
			}
			
			if(found)
			{
				System.out.format("Match starting at index: %d%n", i);
			}
		}
	}
	
	// Reads the file for its contents and returns a the contents as a single string.
	private static String readFile(String inputFile) {
		String text = "";
		
		BufferedReader br ;
		
		try {
			br = new BufferedReader(new FileReader(inputFile));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	
	        while (line != null) 
	        {
	            sb.append(line);
	            line = br.readLine();
	        }
	        text = sb.toString();
	        br.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return text;
	}
	
	private static void testingQuickSearch(String P, String T)
	{
		System.out.println("\nStarting QuickSearch");
		//String P = "server";
		//String T = "Welcome to the Department of Computer Science course web server.";
		
		int lenT = T.length();
		int lenP = P.length();
		int init = lenP + 1;
		
		HashMap qsbc = new HashMap();
		
		qsbc = preQuickSearch(T, P, lenP);
		
		//System.out.println(qsbc.toString());
		// --> QuickSearch is O(lenP*lenT)	
		QuickSearch(P, lenP, T, lenT, qsbc);
	}
	
	private static String testingFastQuickSearch(String P, String T)
	{
		System.out.println("\nStarting FastQuickSearch");
		//String P = "GCAGTCAG";
		//String T = "GCATCGCAGTCAGTATACAGTAC";
		int pos = getPos(P, P.length(), T);
		HashMap next = new HashMap();
		HashMap shift = new HashMap();
		int j = 0;
		int charPos = P.length() - 1;
		
		
		// calculate the next table for P.substring(0, pos)
		// uses the same preprocessing of old quick search
		// which finds the index of the right most occurance of a character in pattern string.
		// NOTE: counting starts from 1 and is from right to left.
		String patternPrefix = P.substring(0, pos);
		next = preQuickSearch(T, patternPrefix, patternPrefix.length());
		
		// calculates the shift table for P
		// again uses the same preprocessing as old quick search.
		shift = preQuickSearch(T, P, P.length());
		
		while(j <= T.length() - P.length())
		{
			while(!(P.charAt(pos) == T.charAt(j + pos)))
			{
				j = j + ((Integer)next.get(""+T.charAt(j+pos))).intValue();
				if( j > T.length() - P.length())
				{
					return "No Match Found";
				}
			}
			if(P.equals(T.substring(j, j + P.length())))
			{
				System.out.println("Match starting at index: " + j);
			}
			
			if(T.length() > j+P.length())
			{
				j = j + ((Integer)shift.get(""+T.charAt(j+P.length()))).intValue();
			}
			else
			{
				j = T.length();
			}
		}
		return "";
	}
	
	// gets the position of a character in P that produces the greatest expected shift value
	private static int getPos(String P, int lenP, String T)
	{
		HashMap prePos = new HashMap();
		int maxES = 0;
		int pos = 0;
		int ES = 0; 
		// tells us the expected shift value at index j
		// NOTE: default values in ES are 0.
		
		//init all prepos to -1
		for(int i = 0; i < T.length(); i++)
		{
			prePos.put(""+T.charAt(i), -1);
		}
				
		for(int j = 0; j < lenP; j++)
		{			
			// calculate the expected shift for P.charAt(j)
			ES = ES + prePos.size() - (j - ((Integer) prePos.get(""+P.charAt(j))).intValue());
			
			//update the value in prePos
			prePos.put(""+P.charAt(j), j);
			
			// check if this is currently the highest ES
			if(ES >= maxES)
			{
				maxES = ES;
				pos = j;
			}
		}
	
		return pos;
	}
		
	//O(lenP*lenT)
	private static void QuickSearch(String p, int lenP, String t, int lenT, HashMap qsbc) 
	{
		int j = 0;
		int charPos;
		
		while(j <= lenT - lenP)
		{
			charPos = j + lenP;
			//charPos just tells us where the end of the matching ends.
			if(p.equals(t.substring(j, charPos)))
			{
				System.out.println("Match starting at index: " + j);
			}
			
			if(charPos < lenT)
			{
				j += ((Integer) qsbc.get(""+t.charAt(charPos))).intValue();
				// j is the bad shift = what index to start the searching/matching
				// j = j + qsbc[last char in the substring]
				// which makes sure that we are still possibly including chars
				// in the previous substring for the next search shift.		
			}
			else
			{
				j = lenT;
			}
		}	
	}
	
	/*
	 * For each character in the pattern string (the search string) 
	 * This function, finds the rightmost occurance of that character in the pattern string.
	 * Counting starts from right to left of the pattern string.
	 * This is O(lenP+lenT)
	 */
	private static HashMap preQuickSearch(String T, String P, int lenP)
	{
		HashMap qsbc = new HashMap();
		int init = lenP + 1;
		
		//Improvement: Assuming the language of the text string (T) is English we have two options for some improvement.
		// i. if lenT >> 26 then its better to just go through the 26 distinct chars instead of going through the entire 
		// 	  chars in T. i.e. init qsbc will be length 26 and value = len(T) + 1.
		// ii. if lenT < 26, then we can just go through the entire chars in T.
		// O(lenT)
		for(int i = 0; i < T.length(); i++)
		{		
			qsbc.put(""+T.charAt(i), new Integer(init));
		}
		
		for(int i = 0; i < lenP; i++)
		{
			qsbc.put(""+P.charAt(i), new Integer(lenP - i) );
		}		
		return qsbc;
	}
}
