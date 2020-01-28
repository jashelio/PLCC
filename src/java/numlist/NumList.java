package numlist;

import plcc.*;

@GrammarRule(TokenEnum.LPAREN, Numbers.class, TokenClass.RPAREN)
public class NumList {
	private Numbers nums;

	@RunBeforeInitOnce // optional run before constructor
	public static void setupOnce() {}

	@RunBeforeInitAll // optional run before constructor
	public static void setupAll() {}

	// the constructor parameters are the values collected from 
	// 	the grammar rule
	public NumList(Numbers nums) {
		this.nums = nums;
	}

	public int sum() {
		int result = 0;
		for (Number num : nums) // java.lang.Number
			result += num.intValue();
	}

	@RunAfterInit // optional run after constructor
	public void run() {
		System.out.println(sum());
	}
}
