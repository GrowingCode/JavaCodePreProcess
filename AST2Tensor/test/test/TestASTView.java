package test;

public class TestASTView {
	
	public void test() {
		boolean a = true;
		boolean b = true;
		boolean c = true;
		boolean d = true;
		if (a && b && (c||d) && d) {
			System.out.println("haha");
		}
//		Object groupId = null;
//		Object artifactId = null;
//		Object version = null;
//		Set message = null;
//		if (message != null && (groupId == null || message.contains(groupId)) && (artifactId == null || message.contains(artifactId)) && (version == null || message.contains(version))) {
//			
//		}
	}
	
}

class MyA {
}
 
interface MyB {
}

interface MyC {
}

class MyD<T extends MyA & MyB & MyC> {
 
}




