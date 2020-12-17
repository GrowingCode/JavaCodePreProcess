package test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestIntStream {
	
	public static void main(String[] args) {
		IntStream int_stream = IntStream.rangeClosed(0, 10);
		Stream<Integer> integer_stream = int_stream.boxed();
		Integer[] trim_vs = (Integer[]) integer_stream.toArray(Integer[]::new);
		for (Integer v : trim_vs) {
			System.out.println(v);
		}
	}
	
}
