import java.util.HashSet;
import java.lang.*;
public class HSetTest {
	Thread t;
	public static void main(String[] args) {
		
		HashSet <Integer> owned = new HashSet<Integer>();
		int[] initial_array = {5,5,5,5};
		for (int i : initial_array) {
			owned.add(i);
		}
		System.out.println(owned);
		int[] count = new int[0];
		if (count.length == 0) {
			System.out.println("works");
		}
		int[] jack = remove_move(initial_array, 5);

		for (int i : jack) {
			System.out.println(i);
		}
		

	}
	public static int[] remove_move(int[] original, int used) { //not destructive way to remove a used move or an impossible move
		int[] new_list = new int[original.length - 1];
		int count_o = 0;
		int count_n = 0;
		boolean flag = true;  //just in case used appears twice
		while (count_o < original.length) {
			if (original[count_o] == used && flag) { 
				flag = false;
			}
			else {
				new_list[count_n] = original[count_o];
				count_n = count_n + 1;
			}
			count_o = count_o + 1;
		}
		return new_list;
	}
	
}