package com.couchbase.utils;

import com.couchbase.model.Node;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Util class to perform next calculations over graph
 * <p>
 * <ul>
 * <li>
 * Compute the maximum path length from the root node to the most
 * distant remote node
 * </li>
 * </ul>
 *
 * @author Aleks
 */
public class GraphUtil {

    /**
     * Compute the maximum path length from the root node to the most
     * distant remote node
     *
     * Uses depth-first search algorithm
     *
     * @param root root node to compute max path from
     * @return the maximum path length
     * @see <a href="https://en.wikipedia.org/wiki/Dijkstra's_algorithm">Dijkstra's algorithm</a>
     */
    public static int getMaxPathLengthFromNode(Node root) {
        checkNotNull(root, "Root node must not be null");

        Map<Node, NodeWrapper> nodeToWrapper = new HashMap<>();
        return getMaxPathLengthFromNode(root, nodeToWrapper).getMaxPathLength();
    }

    private static NodeWrapper getMaxPathLengthFromNode(Node node, Map<Node, NodeWrapper> nodeToWrapper) {

        NodeWrapper nodeWrapper = new NodeWrapper(node);
        for (Node child : node.getChildren()) {

            NodeWrapper childNodeWrapper = nodeToWrapper.get(child);

            //If node has not been visited yet
            if (childNodeWrapper == null) {
                childNodeWrapper = getMaxPathLengthFromNode(child, nodeToWrapper);
                nodeToWrapper.put(child, childNodeWrapper);
            }

            if (childNodeWrapper.getMaxPathLength() + 1 > nodeWrapper.getMaxPathLength()) {
                nodeWrapper.setNewMaxLength(childNodeWrapper);
            }
        }

        return nodeWrapper;
    }

    //TODO consider to replace with Freebuilder
    /**
     * Wrapper to help keep algorithm required data
     * Helps to avoid polluting of graph nodes, {@link Node}
     */
    private static class NodeWrapper {

        private final Node node;
        private int maxPathLength = 0;

        private NodeWrapper(Node node) {
            this.node = node;
        }

        private void setNewMaxLength(NodeWrapper nodeWrapper) {
            maxPathLength = nodeWrapper.maxPathLength + 1;
        }

        private int getMaxPathLength() {
            return maxPathLength;
        }
    }
}
