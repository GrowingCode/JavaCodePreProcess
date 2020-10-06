package eclipse.jdt;

import java.lang.reflect.Field;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.AST;

public class JDTHelper {
	
	public static int GetCurrentJavaVersion() {
		try {
			Class<AST> ast_clz = AST.class;
			String filed_name = "JLS" + System.getProperty("java.specification.version");
			Field f = ast_clz.getField(filed_name);
			int val = f.getInt(null);
			return val;
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Assert.isTrue(false);
		return -1;
	}
	
	public static void main(String[] args) {
		System.out.println(JDTHelper.GetCurrentJavaVersion());
	}
	
}
