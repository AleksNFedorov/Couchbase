package com.couchbase.utils;

import com.couchbase.model.Node;

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
     * Throws {@link IllegalArgumentException} if graph has loop
     * <p>
     * Uses dijkstra's algorithm with relaxation by MAX value
     *
     * @param node root node to compute max path from
     * @return the maximum path length
     * @see <a href="https://en.wikipedia.org/wiki/Dijkstra's_algorithm">Dijkstra's algorithm</a>
     */
    public static int getMaxPathLengthFromNode(Node node) {
        checkNotNull(node, "Root node must not be null");
        int loopDetector = 0;

        Map<Node, PathToNode> nodeToPath = new HashMap<>();
        PathToNode maxPath = new PathToNode(node);
        nodeToPath.put(node, maxPath);

        Queue<PathToNode> pathsToProcess = new LinkedList<>();

        double maxOperationsAllowed = 1;
        PathToNode currentPath = maxPath;
        while (currentPath != null) {

            if (loopDetector++ > maxOperationsAllowed) {
                throw new IllegalArgumentException("Graph has a loop");
            }

            for (Node child : currentPath.destination.getChildren()) {
                PathToNode childNodePath = nodeToPath.get(child);

                //If path does not exists or better (longer) path available
                if (childNodePath == null || childNodePath.length() < currentPath.length() + 1) {
                    childNodePath = currentPath.createPathToNode(child);
                    nodeToPath.put(child, childNodePath);

                    //Max edges in a non-cyclic graph can't exceed nodes^2
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
     * Helps to avoid polluting of graph nodes, {@link Node}
     */
    private static class PathToNode {

        private final Node destination;
        private final List<Node> path;

        private PathToNode(Node destination) {
            this.destination = destination;
            path = new ArrayList<>();
        }

        private void addToPath(Node node) {
            path.add(node);
        }

        private PathToNode createPathToNode(Node child) {
            PathToNode childPath = new PathToNode(child);
            childPath.path.addAll(path);
            childPath.addToPath(destination);

            return childPath;
        }

        private int length() {
            return path.size();
        }
    }
}
