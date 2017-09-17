package io.anuke.codetesting.pdf;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfMain{
	//BAD!!!!!
	static String[] badWords = {"he", "for", "you", "I"};
	
	public static void main(String[] args) throws Exception{
		System.out.println(splitMax(new int[]{0, -5, 2, 4, 5, 5}));
		System.out.println(getMax(new int[]{3, 5, 3, 3, 3, 1}));
	}
	
	//returns the maximum difference between the two subarrays
	static int splitMax(int[] a){
		//this sorts everything in O(nlog(n)) time
		mergeSort(a);
		
		int[] min = new int[a.length/2];
		int[] max = new int[a.length/2];
		
		for(int i = 0; i < a.length/2; i ++){
			min[i] = a[i];
			max[i] = a[i + a.length/2];
		}
		
		return sum(max) - sum(min);
	}
	
	//returns the maximum difference between the two subarrays
	//should work in O(nlog(n)) time
	static int splitMax2(int[] a){
		//this sorts everything, so the highest elements are in the right half, and lowest in the left half
		mergeSort(a);
		
		//sum of min/max subarrays
		int sumMin = 0;
		int sumMax = 0;
		
		for(int i = 0; i < a.length/2; i ++){
			sumMin += a[i]; //the "min" subarray is in a[i ... a.length/2], so add those to sumMin
			sumMax += a[i + a.length/2];//the "max" subarray is in a[a.length/2 ... a.length-1], so add those to sumMax
		}
		
		//return the sum
		return sumMax - sumMin;
	}
	
	static int sum(int[] array){
		int result = 0;
		for(int i = 0; i < array.length; i ++){
			result += array[i];
		}
		return result;
	}
	
	static void mergeSort(int[] array){
		Arrays.sort(array);
	}
	
	//this should run in O(nlog(n)) time
	static int getMax(int[] a){
		//sort to make sure everything's in order
		mergeSort(a);
		
		//current number that is being counted
		int current = 0;
		//occurences in a row of that number
		int occurences = 1;
		
		//go through each number
		for(int i = 0; i < a.length; i ++){
			//if the number isn't current, reset the counter and set current
			if(current != a[i]){
				occurences = 1;
				current = a[i];
			}else{
				//else, increment occurences in a row of that number
				occurences ++;
				
				//if it's more than length/2, we're done, return it
				if(occurences > a.length/2){
					return current;
				}
			}
		}
		
		//probably poor design, but when nothing is found, it returns the integer max value
		//I'm using Java here, so I can't pass an integer reference and return a bool "found" like in C++
		return Integer.MAX_VALUE;
	}
	
	public static void mai(String[] args) throws Exception{
		String path = "/home/anuke/Downloads/iliad.pdf";
		
		PDDocument doc = PDDocument.load(new File(path));
		int pages = doc.getNumberOfPages();
		print(pages);
		
		
		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setStartPage(0);
		stripper.setEndPage(pages);
		String out = stripper.getText(doc);
		print("Length: " + out.length());
		
		StringBuilder result = new StringBuilder();
		
		int last = 0;
		
		int totalstuff = 0;
		
		while(true){
			int indexLike = out.indexOf("like", last);
			int indexAs = out.indexOf("as", last);
			
			if(indexLike == -1 && indexAs == -1) break;
			
			int start = Math.min(indexLike == -1 ? Integer.MAX_VALUE : indexLike, indexAs == -1 ? Integer.MAX_VALUE : indexAs);
			boolean islike = start == indexLike;
			
			if(out.charAt(start-1) != ' ' || out.charAt(islike ? start+4 : start+2) != ' '){
				last = start+1;
				continue;
			}
			
			int end = start;
			for(; end < start + 1000; end ++){
				char c = out.charAt(end);
				if(c == '.' || c == ',' || c == '!' || c == '?' || c == ';'){
					break;
				}
			}
			
			boolean isbad = false;
			
			for(String bad : badWords){
				if(out.indexOf(bad, start) < end){
					isbad = true;
					break;
				}
			}
			
			if(end - start >= 8 && !isbad){
				
				result.append(out.substring(start, end).replace('\n', ' '));
				result.append('\n');
			}
			
			last = start + 1;
			totalstuff ++;
		}
		
		Files.write(Paths.get("out.txt"), result.toString().getBytes());
		print("Done: " + totalstuff);
	}
	
	static void print(Object o){
		System.out.println(o);
	}
}
