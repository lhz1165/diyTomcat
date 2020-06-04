package sortnumbers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Programming for Industry - Introduction to Java
 * Sorting Numbers
 * 
 * Note: Put your answers in between the "// Answer here //" comments. Do not modify other parts of the class.
 */
public class SortNumbers {
	
	/**
	 * Sorting numbers in order.
	 */
	public void sortNumberByAscending(int number1, int number2, int number3, int number4) {
		int first = 0;
		int second = 0;
		int third = 0;
		int fourth = 0;
		// Answer here
		List<Integer> nums = new ArrayList<>(Arrays.asList(number1,number2,number3,number4));
		nums.sort(Integer::compareTo);
		first = nums.get(0);
		second = nums.get(1);
		third = nums.get(2);
		fourth = nums.get(3);
		System.out.println("The numbers are: " + first + ", " + second + ", " + third + ", " + fourth);
	}

	/**
	 * Don't edit this - but read/use this for testing if you like.
	 */
	public static void main(String[] args) {
		SortNumbers cr = new SortNumbers();
		
		cr.sortNumberByAscending(35, -4, 7, 6); // The numbers are: -4, 6, 7, 35
		cr.sortNumberByAscending(-1, 0, 18, -10); // The numbers are: -10, -1, 0, 18
		cr.sortNumberByAscending(1, 2, 3, 4); // The numbers are: 1, 2, 3, 4
	}

}
