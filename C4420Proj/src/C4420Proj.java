import java.util.HashMap;

/*
 * Comp4420 Winter 2015 Project: Implementing QuickSearch and other variants. Then, comparing and contrasting them.
 * Chibuzor Alumba
 * Jaydee Sendin
 */

public class C4420Proj {
	public static void main(String[] args)
	{
			testingQuickSearch();
	}
	
	private static void testingQuickSearch()
	{
		String P = "Department";
		String T = "Welcome to the Department of Computer Science course web server.";
		
		int lenT = T.length();
		int lenP = P.length();
		int init = lenP + 1;
		
		HashMap qsbc = new HashMap();

		//Improvement: Assuming the language of the text string (T) is English we have two options for some improvement.
		// i. if lenT >> 26 then its better to just go through the 26 distinct chars instead of going through the entire 
		// 	  chars in T.
		// ii. if lenT < 26, then we can just go through the entire chars in T.
		for(int i = 0; i < lenT; i++)
		{		
			qsbc.put(""+T.charAt(i), new Integer(init));
		}
		
		preQuickSearch(P, lenP, qsbc);
		QuickSearch(P, lenP, T, lenT, qsbc);
	}
	
	private static void QuickSearch(String p, int lenP, String t, int lenT, HashMap qsbc) {
		int j = 0;
		int charPos;
		
		while(j <= lenT - lenP)
		{
			charPos = j + lenP;
			if(p.equalsIgnoreCase(t.substring(j, charPos)))
			{
				System.out.println("Match starting at index: " + j);
			}
			
			if(charPos < lenT)
			{
				j += ((Integer) qsbc.get(""+t.charAt(charPos))).intValue();
			}
			else
			{
				j = lenT;
			}
		}	
	}

	public static HashMap preQuickSearch(String P, int lenP, HashMap qsbc)
	{
		for(int i = 0; i < lenP; i++)
		{
			qsbc.put(""+P.charAt(i), new Integer(lenP - i) );
		}		
		return qsbc;
	}
}