package translation.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;

public class StatementUtil {

	public static boolean IsStatement(Class<?> clazz) {// node.GetClazz()
		if (Statement.class.isAssignableFrom(clazz) && !(Block.class.isAssignableFrom(clazz))) {
			return true;
		}
		return false;
	}

	public static boolean IsMethodDeclaration(Class<?> clazz) {// node.GetClazz()
		if (MethodDeclaration.class.isAssignableFrom(clazz)) {
			return true;
		}
		return false;
	}

	public static ArrayList<String> ProcessSkeleton(ICompilationUnit icu, ASTNode node) {
		SkeletonVisitor sv = new SkeletonVisitor(icu);
		node.accept(sv);
		ArrayList<String> result = sv.GetResult();
//		PrintUtil.PrintList(result, "skeleton of statement:" + node);
		return result;
	}

}

class SkeletonVisitor extends ASTVisitor {

	Map<String, ElementInfo> record = new TreeMap<String, ElementInfo>();
	Map<ElementInfo, String> content = new TreeMap<ElementInfo, String>();
	ArrayList<Range> ranges = new ArrayList<Range>();

	ASTNode root = null;

	ICompilationUnit icu = null;

	ArrayList<String> result = new ArrayList<String>();

	public SkeletonVisitor(ICompilationUnit icu) {
		this.icu = icu;
	}

	@Override
	public boolean preVisit2(ASTNode node) {
		boolean if_ctn = super.preVisit2(node);
		if (root == null) {
			root = node;
		}
		Range r = null;
		if (node instanceof Block) {
			r = new Range(null, node.getStartPosition(), node.getStartPosition() + node.getLength() - 1,
					OperationKind.remove);
			if_ctn = if_ctn && false;
		} else {
			if (node instanceof SimpleName || node instanceof StringLiteral || node instanceof CharacterLiteral
					|| node instanceof NumberLiteral || node instanceof TypeLiteral) {
				String cnt = node.toString();
				if (node instanceof TypeLiteral) {
					TypeLiteral tl = (TypeLiteral) node;
					cnt = tl.getType().toString();
				}
				ElementInfo ei = record.get(cnt);
				if (ei == null) {
					int index = record.size();
					ei = new ElementInfo(index, cnt);
					content.put(ei, cnt);
					record.put(cnt, ei);
				}
				r = new Range(ei, node.getStartPosition(), node.getStartPosition() + node.getLength() - 1,
						OperationKind.replace);
			}
		}
		if (r != null) {
			ranges.add(r);
		}
		return if_ctn;
	}

	@Override
	public void postVisit(ASTNode node) {
		if (root == node) {
			ArrayList<String> parts = new ArrayList<String>();
			String cnt = null;
			try {
				cnt = icu.getBuffer().getContents();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			int start = node.getStartPosition();
			for (Range r : ranges) {
				Assert.isTrue(start < r.buf_start, "start:" + start + "#r.buf_start:" + "strange node:" + node.toString());
				String pre = cnt.substring(start, r.buf_start);
				if (!pre.equals("")) {
					parts.add(pre);
				}
				switch (r.kind) {
				case replace:
					String nt = cnt.substring(r.buf_start, r.buf_end + 1);
//					System.out.println("nt:" + nt);
					String r_nt = nt.replace(r.ei.content, "#" + r.ei.index);
					parts.add(r_nt);
					break;
				case remove:
					// do nothing.
					break;
				default:
					break;
				}
				start = r.buf_end + 1;
			}
			int end = node.getStartPosition() + node.getLength();
			if (start < end) {
				parts.add(cnt.substring(start, end));
			}
			String c = StringUtils.join(parts, "");

			result.add(c);
			Set<ElementInfo> cs = content.keySet();
			Iterator<ElementInfo> c_itr = cs.iterator();
			while (c_itr.hasNext()) {
				ElementInfo ei = c_itr.next();
				result.add(ei.content);
			}
		}
		super.postVisit(node);
	}

	public ArrayList<String> GetResult() {
		return result;
	}

	class ElementInfo implements Comparable<ElementInfo> {

		int index = -1;
		String content = null;

		public ElementInfo(int index, String content) {
			this.index = index;
			this.content = content;
		}

		@Override
		public int compareTo(ElementInfo o) {
			return new Integer(index).compareTo(new Integer(o.index));
		}

	}

	class Range {

		ElementInfo ei = null;
		int buf_start = 0;
		int buf_end = -1;
		OperationKind kind = null;

		public Range(ElementInfo ei, int buf_start, int buf_end, OperationKind kind) {
			this.ei = ei;
			this.buf_start = buf_start;
			this.buf_end = buf_end;
			this.kind = kind;
		}

	}

	enum OperationKind {

		replace, remove

	}

}
