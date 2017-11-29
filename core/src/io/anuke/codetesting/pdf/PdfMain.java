package io.anuke.codetesting.pdf;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import io.anuke.ucore.UCore;

public class PdfMain{
	//BAD!!!!!
	static String[] badWords = {"he", "for", "you", "I"};
	
	public static void man(String[] args) throws Exception{
		//System.out.println(splitMax(new int[]{0, -5, 2, 4, 5, 5}));
		//System.out.println(getMax(new int[]{3, 5, 3, 3, 3, 1}));
		
		/*
		int[] a = new int[10];
		int[] b = new int[10];
		
		for(int i : new int[]{90, 32, 42, 30, 20, 22}){
			if(!cuckooInsert(a, b, i)){
				UCore.log("\nWe have a loop-- INSERT FAILED!\n");
			}
			
			UCore.log("Table 1 result: " + Arrays.toString(a));
			UCore.log("Table 2 result: " + Arrays.toString(b));
			UCore.log("");
		}*/
		
		//[24, 18, 21, 6, 9, 2, 21, 4, 20, 25, 9, 19, 22, 24, 25, 4, 20, 20, 5, 10, 13, 15, 6, 17, 15]
		//[(24, 18, 21, 6, 9), (2, 21, 4, 20, 25), (9, 19, 22, 24, 25), (4, 20, 20, 5, 10), (13, 15, 6, 17, 15)]
		//[18, 20, 22, 10, 15]
		//->18
		
		//int pivot = getPivotIndex(new int[]{24, 18, 21, 6, 9, 2, 21, 4, 20, 25, 9, 19, 22, 24, 25, 4, 20, 20, 5, 10, 13, 15, 6, 17, 15},
		//		18);
		
		//[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24]
		
		//[2, 19, 24, 7, 8, 22, 13, 14, 17, 15, 23, 4, 1, 20, 0, 9, 11, 10, 16, 21, 5, 18, 3, 12, 6], 
		//-> [(2, 19, 24, 7, 8), (22, 13, 14, 17, 15), (23, 4, 1, 20, 0), (9, 11, 10, 16, 21), (5, 18, 3, 12, 6)], 
		//-> [8, 15, 4, 11, 6]
		//-> 8
		
		//UCore.log(pivot);
		
		//if(false)
		/*
		for(int i = 0; i < 100000; i ++){
			int[] array = null;
			int[] copy = null;
			IntArray ar = new IntArray();
			
			
			for(int j = 0; j < 25; j ++){
				ar.add(j);
			}
			
			ar.shuffle();
			array = ar.toArray();
			copy = ar.toArray();
			
			
			int index = findMedianofMedians(array);
			int pivot = getPivotIndex(array, index);
			
			if(pivot <= 8){
				
				UCore.log(pivot, array[index], Arrays.toString(copy), "\n", Arrays.toString(array));
				break;
			}
		}
		*/
		
		//four
		//1111
		//22
		//112
		//211
		//121
		//31
		//13
		
		//three
		//21
		//12
		//111
		//3
		
		int n = 3;
		int[] memo = new int[n];
		Arrays.fill(memo, -1);
		
		UCore.log(combinations(0, n, memo));
	}
	
	public static int combinations(int i, int n, int[] memo){
		if(i > n){ //out of range, no combinations
			return 0;
		}else if (i >= n - 1){ //one step left, so this is one combination
			return 1;
		}else if(memo[i] != -1){ // it's in memo
			return memo[i];
		}else{ //calculate it
			//get combinations of next three steps, possibly
			int comb = combinations(i + 1, n, memo) +
			combinations(i + 2, n, memo) +
			combinations(i + 3, n, memo);
			
			memo[i] = comb;
			return comb;
		}
	}
	
	static int partition(int[] array, int blackPivot, int from, int to){
		return 0;
	}
	
	static int getPivotIndex(int[] array, int number){
		Arrays.sort(array);
		
		for(int i = 0; i < array.length; i ++){
			if(array[i] == number)
				return i;
		}
		
		return -1;
	}
	
	static int getIndex(int[] array){
		int median = findMedianofMedians(array);
		int less = 0, bigger = 0;
		for(int i = 0; i < array.length; i ++){
			if(i < median)
				less ++;
			if(i > median)
				bigger ++;
		}
		return Math.min(less, bigger);
	}
	
	static int findMedian(int[] copy){
		Arrays.sort(copy);
		return copy[2];
	}
	
	static int findMedianofMedians(int[] array){
		int[] out = new int[5];
		
		for(int i = 0; i < 5; i ++){
			int[] copy = new int[5];
			System.arraycopy(array, i*5, copy, 0, 5);
			
			int median = findMedian(copy);
			out[i] = median;
		}
		
		return findMedian(out);
	}
	
	static void checkPairs(int[] black, int[] white){
		//for every black bottle, check every white bottle, and see if they're a pair
		for(int blackBottle : black){
			for(int whiteBottle : white){
				if(blackBottle == whiteBottle){
					//pair the bottles somehow
					//pairBottles(blackBottle, whiteBottle);
				}
			}
		}
	}
	
	static void checkPairsQuick(int[] black, int[] white){
		
	}
	
	static boolean checkIndex(int[] a, int low, int high){
		int mid = (high + low)/2;
		
		//the index has been found
		if(a[mid] == mid){
			return true;
		//so it doesn't recurse forever
		}else if(low >= high){
			return false;
		}else if(mid < a[mid]){
			//mid is greater than a[mid], so we should check lower, since all the numbers higher are greater than a[mid]
			return checkIndex(a, low, mid);
		}else{
			//else, mid must be < a[mid], so check higher
			return checkIndex(a, mid+1, high);
		}
	}
	
	static int firstDigit(int x) {
		if (x == 0) return 0;
		x = Math.abs(x);
		return (int) Math.floor(x / Math.pow(10, Math.floor(Math.log10(x))));
	}
	
	static int lastDigit(int i){
		return i % 10;
	}
	
	static boolean cuckooInsert(int[] a, int[] b, int value){
		//int hashA = lastDigit(value);
		//int hashB = firstDigit(value);
		UCore.log("Inserting value " + value);
		
		int currentValue = value;
		boolean isa = true;
		
		while(true){
			
			int[] currentTable = isa ? a : b;
			int currentHash = isa ? lastDigit(currentValue) : firstDigit(currentValue);
			UCore.log("(Hashing " + currentValue + " to " +  currentHash + ")");
			
			if(currentTable[currentHash] == 0){
				currentTable[currentHash] = currentValue;
				break;
			}else if(currentTable[currentHash] == value){
				return false;
			}else{
				int kicked = currentTable[currentHash];
				UCore.log("Kicking out " + kicked);
				currentTable[currentHash] = currentValue;
				
				currentValue = kicked;
				isa = !isa;
			}
		}
		
		return true;
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
	
	public static void main(String[] args) throws Exception{
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
