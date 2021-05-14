package test.link;

public class TMain {
	
	public static void Hello(Object obj) {
		TInterface ti1 = (TInterface) obj;
		ti1.HaHaHello(null, 100);
	}
	
	public static void main(String[] args) {
		TImplement ti1 = new TImplement();
//		ElementInfo ei = new ElementInfo(0, "null", 1, 1);
//		Range r = new Range(ei, 0, 1, OperationKind.replace);
//		r
		ti1.HaHaHello(null, 100);
	}
	
}
