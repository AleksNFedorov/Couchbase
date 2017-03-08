package com.couchbase;

import static com.couchbase.GraphUtil.getMaxPathLengthFromNode;

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
    }

}
