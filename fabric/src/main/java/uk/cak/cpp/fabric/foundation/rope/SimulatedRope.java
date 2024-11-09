package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SimulatedRope {
    
    List<RopeNode> nodes = new ArrayList<>();
    List<RopeConstraint> constraints = new ArrayList<>();
    
    public void buildRope(int count, Vec3 from, Vec3 fromPivot, Vec3 to, Vec3 toPivot, float connectionWidth) {
        nodes = new ArrayList<>();
        constraints = new ArrayList<>();
        RopeNode previous = null;
        RopeNode current;
        
        RopeDistanceConnection previousConnectionConstraint = null;
        RopeDistanceConnection currentConnectionConstraint;
        for (int i = 0; i < count; i++) {
            nodes.add(current = new RopeNode(from.lerp(to, (double) i / count)));
            if (previous != null) {
                constraints.add(currentConnectionConstraint = new RopeDistanceConnection(previous, current, connectionWidth));
                if (previousConnectionConstraint != null) {
                    constraints.add(new RopeBendConstraint(
                        previousConnectionConstraint.getFrom(),
                        currentConnectionConstraint.getFrom(),
                        currentConnectionConstraint.getTo(),
                        connectionWidth
                    ));
                }
                previousConnectionConstraint = currentConnectionConstraint;
            }
            previous = current;
        }
        
        constraints.add(new RopeSingleBendConstraint(
            fromPivot, nodes.get(0), nodes.get(1), connectionWidth
        ));
        constraints.add(new RopeSingleBendConstraint(
            toPivot, nodes.get(count - 1), nodes.get(count - 2), connectionWidth
        ));
        nodes.get(0).setFixed(true);
        nodes.get(count - 1).setFixed(true);
    }
    
    public List<RopeDistanceConnection> getConnections() {
        return constraints.stream()
            .filter(ropeConstraint -> ropeConstraint instanceof RopeDistanceConnection)
            .map(ropeConstraint -> (RopeDistanceConnection) ropeConstraint).toList();
    }
    
    public void simulate() {
        for (RopeNode node : nodes) {
            node.simulatePhysics();
        }
        for (int i = 0; i < 20; i++) {
            for (RopeConstraint connection : constraints) {
                connection.iterateConstraint();
            }
            for (RopeNode node : nodes) {
                node.applyNextPositions();
            }
        }
    }
    
    public RopeNode getNode(int index) {
        return nodes.get(index);
    }
    
    public RopeNode getLastNode() {
        return nodes.getLast();
    }
    
}
