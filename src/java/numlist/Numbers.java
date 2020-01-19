package numlist;

import plcc.*;

import java.text.NumberFormat;

@GrammarRule(Grammar.or(Numbers.NonEmpty.class, Grammar.EMPTY))
public class Numbers implements Iterable<Number> {
	private Numbers nums;
	
	public Numbers(NonEmpty nums) { // Numbers.NonEmpty grammar rule
		this.nums = nums;
	}

	public Numbers() { // empty grammar rule
		this.nums = new Numbers(null);
	}

	@Override
	public Iterator<Number> iterator() { // how I chose to distribute values
		return new Iterator<Number> {
			private Numbers current = nums;
			public boolean hasNext() {
				return current.value() != null;
			}
			public Number next() {
				Number result = current.value();
				current = current.more();
				return result;
			}
		};
	}

	private Number value() { // default for empty
		return null;
	}

	private Numbers more() { // default for empty
		return new Numbers(); // avoid null pointers in iterator
	}

	@GrammarRule(TokenEnum.NUMBER, Numbers.class)
	public static class NonEmpty extends Numbers {
		private static final NumberFormat formatter = NumberFormat.getInstance();

		private Number val;
		private Numbers more;

		// token.NUMBER, Numbers grammar rule
		public NonEmpty(String numVal, Numbers nums) {
			val = formatter.parse(numVal); // construct number
			more = nums; // save the rest
		}

		@Override
		private Number value() {
			return val;
		}

		@Override
		private Numbers more() {
			return more;
		}
	}
}
