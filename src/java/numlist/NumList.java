package numlist;

import plcc.annotation.*;

@GrammarRule
public class NumList {
	private Numbers nums;

// TODO	@RunBeforeInitOnce // optional run before constructor
	public static void setupOnce() {}

// TODO	@RunBeforeInitAll // optional run before constructor
	public static void setupAll() {}

	// the constructor parameters are the values collected from 
	// 	the grammar rule
	public NumList(String lparen, Numbers nums, String rparen) {
		this.nums = nums;
	}

	public int sum() {
		int result = 0;
//		for (Number num : nums) // java.lang.Number
//			result += num.intValue()
		Numbers current = nums.more();
		if (current == null)
			current = new Numbers();
		Number temp;
		while ((temp = current.value()) != null) {
			result += temp.intValue();
			current = current.more();
		}
		return result;
	}

// TODO	@RunAfterInit // optional run after constructor
	public void run() {
		System.out.println(sum());
	}
}
