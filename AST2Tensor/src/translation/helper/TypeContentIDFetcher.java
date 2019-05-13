package translation.helper;

import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;

import eclipse.jdt.JDTASTHelper;
import eclipse.search.JDTSearchForChildrenOfASTNode;
import statistic.id.IDManager;
import statistic.id.PreProcessContentHelper;

public class TypeContentIDFetcher {
	
	public static TypeContentID FetchTypeID(ASTNode node, IDManager im) {
		// set node type and content
		String type_content = JDTASTHelper.GetTypeRepresentationForASTNode(node);
		type_content = PreProcessContentHelper.PreProcessTypeContent(type_content);
		int type_content_id = im.GetTypeContentID(type_content);
		return new TypeContentID(type_content, type_content_id);
	}
	
	public static TypeContentID FetchContentID(ASTNode node, IDManager im) { // List<ASTNode> children
		List<ASTNode> ncs = JDTSearchForChildrenOfASTNode.GetChildren(node);
		Assert.isTrue(ncs.size() == 0);
		Assert.isTrue(node instanceof SimpleName, "not SimpleName node class:" + node.getClass());
		// set node type and content
//		String type = null;
//		String content = null;
//		String type_content = null;
//		int type_id = -1;
//		int content_id = -1;
//		int type_content_id = -1;
//		if (!is_leaf_node) { // children.size() > 0
//			type = node.getClass().getSimpleName();
//			content = IDManager.Default;
//			type_content = node.getClass().getSimpleName() + "#" + IDManager.DefaultPart;
//			type_content_id = im.GetTypeContentID(type_content);
//			content_id = im.GetContentID(content);
//		} else {
//			type = node.getClass().getSimpleName();
//			content = node.toString().trim();
//			type_content = node.getClass().getSimpleName() + "#" + node.toString().trim();
//			type_id = im.GetTypeID(type);
//			content_id = im.GetContentID(content);
//			type_content_id = im.GetTypeContentID(type_content);
//		}
//		type_content = JDTASTHelper.GetRepresentationForASTNode(node);
//		type_content_id = im.GetTypeContentID(type_content);
		String type_content = node.toString();
		type_content = PreProcessContentHelper.PreProcessTypeContent(type_content);
		int type_content_id = im.GetTypeContentID(type_content);
		return new TypeContentID(type_content, type_content_id);
	}

}
