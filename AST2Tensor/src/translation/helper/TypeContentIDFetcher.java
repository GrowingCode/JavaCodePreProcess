package translation.helper;

import org.eclipse.jdt.core.dom.ASTNode;

import statistic.id.IDManager;

public class TypeContentIDFetcher {

	public static TypeContentID FetchTypeContentID(ASTNode node, boolean is_leaf_node, IDManager im) { // List<ASTNode> children
		// set node type and content
		String type = null;
		String content = null;
		int type_id = -1;
		int content_id = -1;
		if (!is_leaf_node) { // children.size() > 0
			// IDManager.PrimordialNonLeafASTType
			type = node.getClass().getSimpleName();
			content = IDManager.Default;
			type_id = im.GetTypeID(type);
			content_id = im.GetContentID(content);// type,
		} else {
			// node.getParent().getClass().getSimpleName() + "#" +
			type = node.getClass().getSimpleName();
			content = node.toString().trim();
			type_id = im.GetTypeID(type);
			content_id = im.GetContentID(content);// type,
		}
		return new TypeContentID(type, content, type_id, content_id);
	}

}
