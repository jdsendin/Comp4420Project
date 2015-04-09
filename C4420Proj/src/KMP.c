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
			//sizeOfFile++;
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

int KMP(char *target, int tsize, char *pattern, int psize)
{
	//i = pos; j = i, n = tsize, y = target, x = pattern, m = psize
	int pos = -1;
	int *kmpNext = preKmp(pattern, psize);
	
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
			free(kmpNext);
			return i-pos;
		}
	}
	
	free(kmpNext);	
	return -1;
}

void handleKmpSearch()
{
	//char *PatternString = "In";//pattern to find
	char *PatternString;// = "GCAGAGAG";//pattern to find
	
	struct stat sb; //Used to get all stats on the file
	int fileSize; //used to store the file's size
	int lenTS, lenPS; //Len of the Target and Pattern string respectively
	
	char *TargetText; //Used as the target string
	//char *temp;
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
	
	//Read in the bible into a buffer
	TargetText = readFileContents(bibleFile, fileSize);
	
	if(!TargetText)
	{
		perror("Could not read file");
		return;
	}	
	
	//temp = TargetText;
	
	PatternString = TargetText;
	lenTS = fileSize;
	lenPS = lenTS;
	//lenPS = strlen(PatternString);
	
	printf("lenTS is %d, lenPS is %d\n", lenTS, lenPS);

	//QS(TargetText, lenTS, PatternString, lenPS);
	int found = KMP(TargetText, lenTS, PatternString, lenPS);
	
	if(found >= 0)
	{
		printf("Match found at position: %d\n", found);
		//printf("Matched @: %s\n", temp + found);
	}
	else
	{
		printf("Pattern not found in the Target string\n");
	}
}

int main(int argc, char **argv)
{
	//char *target = "Welcome to the Department of Computer Science course web server.";
	////char target[] = "ABC ABCDAB ABCDABCDABDE";
	////char target[] = "GCATCGCAGAGAGTATACAGTACG";
	//char *temp = target;
	//int tsize = strlen(target);
	//char *pattern = "Department";
	////char pattern[] = "ABCDABD";
	////char pattern[] = "GCAGAGAG";
	//int psize = strlen(pattern);
	
	//printf("Target: %s\n Pattern: %s\n", target, pattern);
	
	handleKmpSearch();
	
	return 0;
}

