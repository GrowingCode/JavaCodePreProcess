package util.visitor;

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
import org.eclipse.jdt.core.dom.Statement;

public class SkeletonVisitor extends ASTVisitor {

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
		if (root != node) {
			if (node instanceof Statement && !(node instanceof Block)) {
//				Assert.isTrue(node instanceof Block, "node class:" + node.getClass() + ";root:" + root + ";node:" + node);
				r = new Range(null, node.getStartPosition(), node.getStartPosition() + node.getLength() - 1,
						OperationKind.remove);
				if_ctn = if_ctn && false;
			} else {
				r = HandleNonStatement(node);
			}
		}
		if (r != null) {
			ranges.add(r);
		}
		return if_ctn;
	}
	
	protected Range HandleNonStatement(ASTNode node) {
		Range r = null;
		return r;
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
			for (int i = 0; i < ranges.size(); i++) {
				Range r = ranges.get(i);
				Range pr = null;
				String pre_r_cnt = null;
				if (i > 0) {
					pr = ranges.get(i-1);
					pre_r_cnt = cnt.substring(pr.buf_start, pr.buf_end + 1);
				}
				Assert.isTrue(start <= r.buf_start,
						"start:" + start + "#r.buf_start:" + r.buf_start + "#range_content:" + r.ei + "#range_cnt:"
								+ cnt.substring(r.buf_start, r.buf_end + 1) + "#previous r_cnt:" + pre_r_cnt + "#strange node:" + node.toString()
								+ "#parent strange node:" + node.getParent().toString());
				String pre = cnt.substring(start, r.buf_start);
				pre = pre.replaceAll("\\s+", " ");
				if (!pre.equals("")) {
					parts.add(pre);
				}
				switch (r.kind) {
				case replace:
					String nt = cnt.substring(r.buf_start, r.buf_end + 1);
					nt = nt.replaceAll("\\s+", " ");
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
				String post = cnt.substring(start, end).replaceAll("\\s+", " ");
//				System.out.println("post:" + post);
				parts.add(post);
			}
			String c = StringUtils.join(parts, "").replaceAll("\\s+", " ");
//			System.out.println("c:" + c);
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

}
