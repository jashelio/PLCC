package numlist;

import plcc.annotation.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;

@GrammarRule
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
		return new Iterator<Number>() {
			private Numbers current = nums;
			public boolean hasNext() {
				boolean result = current.value() != null;
				return result;
			}
			public Number next() {
				Number result = current.value();
				current = current.more();
				return result;
			}
		};
	}

	protected Number value() { // default for empty
		return null;
	}

	protected Numbers more() { // default for empty
		return nums;
//		return new Numbers(); // avoid null pointers in iterator
	}

	@GrammarRule
	public static class NonEmpty extends Numbers {
		private static final NumberFormat formatter = NumberFormat.getInstance();

		private Number val;
		private Numbers more;

		// token.NUMBER, Numbers grammar rule
		public NonEmpty(String number, String comma, Numbers nums) throws ParseException {
			val = formatter.parse(number); // construct number
			System.out.println(val);
			more = nums; // save the rest
		}

		@Override
		protected Number value() {
			return val;
		}

		@Override
		protected Numbers more() {
			return more;
		}
	}
}
