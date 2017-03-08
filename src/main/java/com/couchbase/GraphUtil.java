package com.couchbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
     * <p>
     * Throws {@link IllegalArgumentException} if graph has cycle
     * <p>
     * Uses dijkstra's algorithm with relaxation by MAX value
     *
     * @param node root node to compute max path from
     * @return the maximum path length
     * @see <a href="https://en.wikipedia.org/wiki/Dijkstra's_algorithm">Dijkstra's algorithm</a>
     */
    public static int getMaxPathLengthFromNode(Node node) {

        checkNotNull(node, "Root node must not be null");
        int cycleDetector = 0;

        Map<Node, NodePath> nodeToPath = new HashMap<>();
        NodePath maxPath = new NodePath(node);
        nodeToPath.put(node, maxPath);

        Queue<NodePath> pathsToProcess = new LinkedList<>();

        double maxOperationsAllowed = 1;
        NodePath currentPath = maxPath;
        while (currentPath != null) {

            if (cycleDetector++ > maxOperationsAllowed) {
                throw new IllegalArgumentException("Graph has a cycle");
            }

            for (Node child : currentPath.destination.getChildren()) {
                NodePath childNodePath = nodeToPath.get(child);
                if (childNodePath == null || childNodePath.length() < currentPath.length() + 1) {
                    childNodePath = currentPath.createPathToNode(child);
                    nodeToPath.put(child, childNodePath);
                    maxOperationsAllowed = Math.pow(nodeToPath.size(), 2);
                }
                pathsToProcess.add(childNodePath);
            }

            maxPath = maxPath.length() < currentPath.length() ? currentPath : maxPath;
            currentPath = pathsToProcess.poll();
        }

        return maxPath.length();
    }

    //TODO consider to replace with Freebuilder
    /**
     * Wrapper to help keep track on nodes visiting path and calculate length
     */
    private static class NodePath {

        private final Node destination;
        private final List<Node> path;

        private NodePath(Node destination) {
            this.destination = destination;
            path = new ArrayList<>();
        }

        private void addToPath(Node node) {
            path.add(node);
        }

        private NodePath createPathToNode(Node child) {
            NodePath childPath = new NodePath(child);
            childPath.path.addAll(path);
            childPath.addToPath(destination);

            return childPath;
        }

        private int length() {
            return path.size();
        }
    }
}
