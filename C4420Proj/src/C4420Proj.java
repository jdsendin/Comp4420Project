import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;

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


		String inputFile = "textFiles/kjvdat.txt";
		PrintWriter writer = null; 
		
		try {
			writer = new PrintWriter("textFiles/output.txt");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		String file = readFile(inputFile);
		Search search = new Search(file, writer);
		
		//testQS(file, search, writer);
		testFQS(file, search, writer);
		//testBrute(file, search, writer);
		writer.close();		
	}
	
	private static void testQS(String file, Search search, PrintWriter writer)
	{
		String patternString = "";
		
		writer.write(String.format("Testing Can't Find String%n"));
		patternString = "YouCantFindThis";
		timeQuickSearch(search, patternString, writer);
		
		writer.write(String.format("%nTesting Can Find String%n"));		
		patternString = file.substring(0, 1);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(30000, 30010);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(20050, 20100);
		timeQuickSearch(search, patternString, writer);		

		patternString = file.substring(10000, 10100);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(30100, 30300);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(20900, 21200);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(0, 400);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(200, 700);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(2500, 3100);
		timeQuickSearch(search, patternString, writer);
		
		patternString = file.substring(0, file.length());
		timeQuickSearch(search, patternString, writer);

	}
	
	private static void testFQS(String file, Search search, PrintWriter writer)
	{
		String patternString = "";
		
		writer.write(String.format("Testing Can't Find String%n"));
		patternString = "YouCantFindThis";
		timeFastQuickSearch(search, patternString, writer);
		
		writer.write(String.format("%nTesting Can Find String%n"));		
		patternString = file.substring(0, 1);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(30000, 30010);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(20050, 20100);
		timeFastQuickSearch(search, patternString, writer);		

		patternString = file.substring(10000, 10100);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(30100, 30300);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(20900, 21200);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(0, 400);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(200, 700);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(2500, 3100);
		timeFastQuickSearch(search, patternString, writer);
		
		patternString = file.substring(0, file.length());
		timeFastQuickSearch(search, patternString, writer);
	}
	
	private static void testBrute(String file, Search search, PrintWriter writer)
	{
		String patternString = "";

		writer.write(String.format("Testing Can't Find String%n"));
		patternString = "YouCantFindThis";
		timeIt(search, patternString, writer);
		
		writer.write(String.format("%nTesting Can Find String%n"));		
		patternString = file.substring(0, 1);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(30000, 30010);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(20050, 20100);
		timeIt(search, patternString, writer);		

		patternString = file.substring(10000, 10100);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(30100, 30300);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(20900, 21200);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(0, 400);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(200, 700);
		timeIt(search, patternString, writer);
		
		patternString = file.substring(2500, 3100);
		timeIt(search, patternString, writer);

		patternString = file.substring(0, file.length());
		timeIt(search, patternString, writer);
	}
	
	private static void timeQuickSearch(Search search, String patternString, PrintWriter writer)
	{
		long startTime, endTime, elapsedTime;
		double smallestTime, seconds;
		
		writer.write(String.format("%nLength of Pattern String: %d%n", patternString.length()));
		
		//START time recorder for QS
		writer.write(String.format("Quick Search:%n"));

		startTime = System.nanoTime();
		search.testingFastQuickSearch(patternString);
		endTime = System.nanoTime();				
		//END time recorder
		
		//Print time taken in seconds
		elapsedTime = endTime - startTime;
		seconds = ((double)elapsedTime / 1000000000.0);
		writer.write(String.format("Time taken in seconds = %f%n", seconds));
		// End QS
	}
	
	private static void timeFastQuickSearch(Search search, String patternString, PrintWriter writer)
	{
		long startTime, endTime, elapsedTime;
		double smallestTime, seconds;
		
		writer.write(String.format("%nLength of Pattern String: %d%n", patternString.length()));
		
		
		//START time recorder for QS
		writer.write(String.format("Fast Quick Search:%n"));
		startTime = System.nanoTime();
		search.testingFastQuickSearch(patternString);
		endTime = System.nanoTime();				
		//END time recorder
		
		//Print time taken in seconds
		elapsedTime = endTime - startTime;
		seconds = ((double)elapsedTime / 1000000000.0);
		writer.write(String.format("Time taken in seconds = %f%n", seconds));
		// End QS
	}
	
	
	private static void timeIt(Search search, String patternString, PrintWriter writer)
	{
		long startTime, endTime, elapsedTime;
		double smallestTime, seconds;
		
		writer.write(String.format("%nLength of Pattern String: %d%n", patternString.length()));	
		
		//START time recorder
		writer.write(String.format("Brute Force Search:%n"));
		startTime = System.nanoTime();
		search.testingBruteForceSearch(patternString);
		endTime = System.nanoTime();				
		//END time recorder
		
		//Print time taken in seconds
		elapsedTime = endTime - startTime;
		seconds = ((double)elapsedTime / 1000000000.0);
		writer.write(String.format("Time taken in seconds = %f%n", seconds));
		//TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS));	
		// END BruteForce
		
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
}

class Search
{
	private String T;
	private int lenT;
	private PrintWriter writer;
	
	private long startTime, endTime, elapsedTime;
	private double smallestTime, seconds;
	
	public Search(String file, PrintWriter writer)
	{
		T = file;
		lenT = file.length();
		this.writer = writer;
	}
	
	public void testingQuickSearch(String P)
	{
		
		int lenP = P.length();
		int init = lenP + 1;
		
		HashMap qsbc = new HashMap();
		
		qsbc = preQuickSearch(P, lenP);
		
		// --> QuickSearch is O(lenP*lenT)	
		QuickSearch(P, lenP, qsbc);
	}
	
	public void testingBruteForceSearch(String P)
	{
		int lenP = P.length();
		
		for(int i = 0; i <= lenT-lenP; i++)
		{
			boolean found = true;
			
			for(int j = 0; j < lenP; j++)
			{
				if(P.charAt(j) != T.charAt(i+j))
				{
					found = false;
					break;
				}
			}

			if(found)
			{
				System.out.format("Match starting at index: %d%n", i);
			}
		}
	}
	
	public String testingFastQuickSearch(String P)
	{
		int pos = getPos(P, P.length());
		HashMap next = new HashMap();
		HashMap shift = new HashMap();
		int j = 0;
		int charPos = P.length() - 1;
		
		
		// calculate the next table for P.substring(0, pos)
		// uses the same preprocessing of old quick search
		// which finds the index of the right most occurance of a character in pattern string.
		// NOTE: counting starts from 1 and is from right to left.
		String patternPrefix = P.substring(0, pos);
		next = preQuickSearch(patternPrefix, patternPrefix.length());
		
		// calculates the shift table for P
		// again uses the same preprocessing as old quick search.
		shift = preQuickSearch(P, P.length());		
		
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
	private int getPos(String P, int lenP)
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
	private void QuickSearch(String p, int lenP, HashMap qsbc) 
	{
		int j = 0;
		int charPos;
		
		while(j <= lenT - lenP)
		{
			charPos = j + lenP;
			//charPos just tells us where the end of the matching ends.
			if(p.equals(T.substring(j, charPos)))
			{
				System.out.println("Match starting at index: " + j);
			}
			if(charPos < lenT)
			{
				j += ((Integer) qsbc.get(""+T.charAt(charPos))).intValue();
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
	private HashMap preQuickSearch(String P, int lenP)
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
