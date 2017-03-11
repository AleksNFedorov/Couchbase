package com.couchbase;

import com.couchbase.model.Node;
import com.couchbase.utils.GraphUtil;

import static com.couchbase.utils.GraphUtil.getMaxPathLengthFromNode;

/**
 * Hello world app!
 */
public class App {
    public static void main(String... args) {

        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");

        nodeA.addChild(nodeB);
        nodeA.addChild(nodeC);
        nodeA.addChild(nodeD);

        nodeB.addChild(nodeD);

        nodeC.addChild(nodeD);
        nodeC.addChild(nodeE);

        nodeD.addChild(nodeE);

        System.out.println(getMaxPathLengthFromNode(nodeA));

        int count = 10000;

        Node[] nodes = new Node[count];
        for (int i = 0; i < count; ++i) {
            nodes[i] = new Node("node:" + i);
            for (int j = 0; j < i; ++j) {
                nodes[i].addChild(nodes[j]);
            }
        }
        System.out.println(GraphUtil.getMaxPathLengthFromNode(nodes[count - 1]));


    }

}
