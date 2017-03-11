package com.couchbase.utils;


import com.couchbase.model.Node;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link GraphUtil}
 */
public class GraphUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void test_getMaxPathLengthFromNode_nullNode_throwsException() {
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Root node must not be null");

        GraphUtil.getMaxPathLengthFromNode(null);
    }

    @Test
    public void test_getMaxPathLengthFromNode_1K_nodesGraph_returnsZeroLength() {
        int count = 1000;

        Node[] nodes = new Node[count];
        for (int i = 0; i < count; ++i) {
            nodes[i] = new Node("node:" + i);
            for (int j = 0; j < i; ++j) {
                nodes[i].addChild(nodes[j]);
            }
        }
        Assert.assertEquals(999, GraphUtil.getMaxPathLengthFromNode(nodes[count - 1]));
    }

    @Test
    public void test_getMaxPathLengthFromNode_singleNodeGraph_returnsZeroLength() {
        Assert.assertEquals(0, GraphUtil.getMaxPathLengthFromNode(new Node("A")));
    }

    @Test
    public void test_getMaxPathLengthFromNode_singleMaxPath_nodeFromMiddle_returnsValidLength() {
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

        Assert.assertEquals(2, GraphUtil.getMaxPathLengthFromNode(nodeB));
    }

    @Test
    public void test_getMaxPathLengthFromNode_singleMaxPath_returnsValidLength() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");

        nodeA.addChild(nodeB);
        nodeB.addChild(nodeC);
        nodeA.addChild(nodeD);

        Assert.assertEquals(2, GraphUtil.getMaxPathLengthFromNode(nodeA));
    }

    @Test
    public void test_getMaxPathLengthFromNode_multipleMaxPaths_returnsValidLength() {
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");

        nodeA.addChild(nodeB);
        nodeB.addChild(nodeC);
        nodeB.addChild(nodeD);

        Assert.assertEquals(2, GraphUtil.getMaxPathLengthFromNode(nodeA));
    }
}