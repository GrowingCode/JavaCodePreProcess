package translation.helper;

import org.eclipse.jdt.core.dom.ASTNode;

import eclipse.jdt.JDTASTHelper;
import statistic.id.IDManager;

public class TypeContentIDFetcher {

	public static TypeContentID FetchTypeContentID(ASTNode node, boolean is_leaf_node, IDManager im) { // List<ASTNode> children
		// set node type and content
//		String type = null;
//		String content = null;
		String type_content = null;
//		int type_id = -1;
//		int content_id = -1;
		int type_content_id = -1;
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
		type_content = JDTASTHelper.GetRepresentationForASTNode(node);
		type_content_id = im.GetTypeContentID(type_content);
		return new TypeContentID(type_content, type_content_id);
	}

}
