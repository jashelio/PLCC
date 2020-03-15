package numlist;

import edu.rit.gec8773.laps.annotation.GrammarRule;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;

@GrammarRule
public class Numbers implements Iterable<Number> {
	private Numbers nums;
	
	protected Numbers(Numbers nums) {
		this.nums = nums;
	}

	public Numbers(NonEmpty nums) { // Numbers.NonEmpty grammar rule
		this.nums = nums;
	}

	public Numbers() { // empty grammar rule
		this.nums = null;
	}

	@Override
	public Iterator<Number> iterator() { // how I chose to distribute values
		Numbers start = this;
		return new Iterator<>() {
			private Numbers current = start;
			public boolean hasNext() {
				return current != null && current.more() != null;
			}
			public Number next() {
				current = current.more();
				Number result = current.value();
				current = current.more();
				return result;
			}
		};
	}

	protected Number value() { // default for empty
		return nums != null ? nums.value() : null;
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
		public NonEmpty(String number, RestNumbers numsContainer) throws ParseException {
			val = formatter.parse(number); // construct number
//			System.out.println(val);
			more = numsContainer.more(); // save the rest
		}

		@Override
		protected Number value() {
			return val;
		}

		@Override
		protected Numbers more() {
			return more;
		}

		@GrammarRule
		public static class RestNumbers extends Numbers {
			public RestNumbers(String comma, Numbers nums) {
				super(nums);
			}

			public RestNumbers() {
				super();
			}
		}
	}
}
