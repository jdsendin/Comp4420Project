/*
 * KMP.c
 * 
 * Copyright 2015 chibs <chibs@CHIBS-ULTIMATE>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */


#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <time.h>

//Read the file contents
char* readFileContents(char *filename, int fileSize)
{
	char *buffer;
	FILE *fr; /*declare the file pointer*/
	
	//Read the bytes out of the file
	if(filename == NULL || fileSize <= 0)
	{
		perror("invalid file OR filesize passed");
		return NULL;
	}
	else
	{				
		printf("Filename is: %s\n", filename);
		
		//Load the file
		fr = fopen(filename, "rb"); /*open the file to read its bytes*/
		
		if(fr)
		{
			int sizeOfFile = fileSize + 1;// * sizeof(char);
			
			buffer = (char*) calloc(sizeOfFile, sizeof(char));
			
			if(buffer)
			{
				fread(buffer, 1,fileSize, fr);
				//printf("File contents is: %s\n", buffer);
			}
			else
			{
				printf("Buffer not defined\n");
				return NULL;
			}
		}
		else
		{
			printf("File not open\n");
			return NULL;
		}
		
		//printf("The file contains \n%s\n", buffer);
		fclose(fr); /*Close the file prior to exiting the routine*/
	}
	
	return buffer;
}

int *preKmp(char *pattern, int psize) {
	int i, k;
	
	//This is the array that will contain
	//the pre-computed shift values for the algorithm
	int *kmpNext = malloc(sizeof(int)*psize);
	
	if(!kmpNext)
	{
		return NULL;
	}
	
	//Start k at -1
	k = kmpNext[0] = -1;
   
	//Go through the entire pattern array
	for(i = 1; i < psize; i++)
	{
		//Keep skipping ahead as long as the pattern 
		//we are considering does not match the current pattern
		while (k > -1 && pattern[i] != pattern[k+1])
		{
			k = kmpNext[k];
		}
				
		if (pattern[i] == pattern[k+1])
		{
			k++;
		}
		
		kmpNext[i] = k;
   }
   
   return kmpNext;
}

int KMP(char *target, int tsize, char *pattern, int psize, int *kmpNext)
{
	//i = pos; j = i, n = tsize, y = target, x = pattern, m = psize
	int pos = -1;
	//int *kmpNext = preKmp(pattern, lenPS);
	int i;
	
	if(!kmpNext)
	{
		return -1;
	}
	
	for(i = 0; i < tsize; i++)
	{
		//Keep skipping ahead as long as the pattern 
		//we are considering does not match the target
		while(pos > -1 && pattern[pos+1] != target[i])
		{
			pos = kmpNext[pos];
		}
		
		//If the current target matches
		//the next pattern we are considering
		if(target[i] == pattern[pos+1])
		{
			pos++;
		}
		
		//If we get to the end of the pattern
		//array, then we found the pattern
		if(pos == psize - 1)
		{
			//free(kmpNext);
			//return i-pos;
			
			//ADDITION: to help find ALL the matching strings
			//not just the first matching string			
			printf("Pattern Matched @: %d\n", (i-pos));
			pos = -1;
		}
	}
	
	free(kmpNext);	
	return 1;
}

char *getSubstring(char *source, int start, int end)
{
	char *result;
	int diff, sourceLen;
	
	if(!source || end <= start)
	{
		return NULL;
	}
	
	diff = end - start;
	sourceLen = strlen(source);
	if(sourceLen < diff || start >= sourceLen)
	{
		return NULL;
	}
	
	result = calloc(diff, sizeof(char) + 1);
	
	strncpy(result, source+start, diff);
	
	return result;
}

void runKmpOnTargetSubstring(char *TargetText, int lenTS, int subStart, int subEnd)
{
	printf("\n\nRunning KMP on substring Start: %d - End: %d\n", subStart, subEnd);
	
	char *PatternString;//pattern to find
	clock_t start, end;
	int *kmpNext;//used to store the kmp preprocessing values
	float seconds;
	
	//Test new pattern string
	int lenPS = subEnd - subStart;
	
	PatternString = getSubstring(TargetText, subStart, subEnd);	
	kmpNext = preKmp(PatternString, lenPS);
	start = clock();
	KMP(TargetText, lenTS, PatternString, lenPS, kmpNext);
	end = clock();
	seconds = (float)(end - start) / CLOCKS_PER_SEC;	
	printf("Time taken to run Pattern string = %f seconds\n",
			seconds);
	//End Test new pattern string
	
	free(PatternString);
	//free(start);
	//free(end);
}

void handleKmpSearch()
{	
	struct stat sb; //Used to get all stats on the file
	int fileSize; //used to store the file's size
	int lenTS;//Len of the Target string
	int start, end;
	
	char *TargetText; //Used as the target string
	char *bibleFile = "textFiles/kjvdat.txt";
	//char *bibleFile = "textFiles/testBible.txt";
	
	//Testing searching for a Pattern string
	//using the bible as the Target string
	
	//Check if we could get stats for the file
	if(stat(bibleFile, &sb) == -1)
	{
		perror("stat");
		exit(EXIT_FAILURE);
	}
	
	fileSize = sb.st_size;
	lenTS = fileSize;
	
	//Read in the bible into a buffer
	TargetText = readFileContents(bibleFile, fileSize);
	
	if(TargetText == NULL)
	{
		perror("Could not read file");
		return;
	}	
	
	//Test new pattern string
	start = 0;
	end = 1;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern stringda
	
	//Test new pattern string
	start = 30000;
	end = 30010;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 20050;
	end = 20100;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 10000;
	end = 10100;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 30100;
	end = 30300;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 20900;
	end = 30200;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 0;
	end = 400;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 200;
	end = 700;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 2500;
	end = 3100;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//Test new pattern string
	start = 0;
	end = lenTS;
	runKmpOnTargetSubstring(TargetText, lenTS, start, end);
	//End Test new pattern string
	
	//ADDITION: all the positions the pattern string gets 
	//matched at will be printed so no need to check for the
	//return value
	
	free(TargetText);
}

int main(int argc, char **argv)
{	
	handleKmpSearch();
	
	return 0;
}

