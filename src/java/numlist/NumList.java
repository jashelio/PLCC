package numlist;

import edu.rit.gec8773.laps.annotation.GrammarRule;
import edu.rit.gec8773.laps.annotation.RunBeforeEachInit;
import edu.rit.gec8773.laps.annotation.RunBeforeFirstInit;
import edu.rit.gec8773.laps.annotation.SemanticEntryPoint;

@GrammarRule
public class NumList {
	private Numbers nums;

	@RunBeforeFirstInit // optional run before constructor
	public static void setupOnce() {
		System.out.print("Insert numbers in the form: ");
		System.out.println("( num1, num2, ..., numN )");
		System.out.println("Note: you can add any whitespace you want");
	}

	@RunBeforeEachInit // optional run before constructor
	public static void setupAll() {
		System.out.print("Input? ");
	}

	// the constructor parameters are the values collected from 
	// 	the grammar rule
	public NumList(String lparen, Numbers nums, String rparen) {
		this.nums = nums;
	}

	public int sum() {
		int result = 0;
//		for (Number num : nums) // java.lang.Number
//			result += num.intValue()
		System.out.print('(');
		boolean ran = false;
		for ( Number temp : nums ) {
			ran = true;
			result += temp.intValue();
			System.out.print(temp.intValue());
			System.out.print(", ");
		}
		if (ran)
			System.out.print("\b\b");
		System.out.println(')');
		return result;
	}

	@SemanticEntryPoint // optional run after constructor
	public void run() {
		System.out.println(sum());
	}
}
