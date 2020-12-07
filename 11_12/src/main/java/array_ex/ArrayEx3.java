package array_ex;

import java.util.Arrays;

public class ArrayEx3 {

	public static void main(String[] args) {
		int[] arr = {1,2,3,4,5};
		
		int[] copied = new int[10];
		
		copied = Arrays.copyOf(arr, 10);
		System.out.println(Arrays.toString(copied));
		
		copied = Arrays.copyOf(arr,3);
		System.out.println(Arrays.toString(copied));		

	}

}
