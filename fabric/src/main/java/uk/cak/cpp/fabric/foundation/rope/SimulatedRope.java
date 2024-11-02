package uk.cak.cpp.fabric.foundation.rope;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SimulatedRope {
    
    List<RopeNode> nodes = new ArrayList<>();
    List<RopeConnection> connections = new ArrayList<>();
    
    public void buildRope(int count, Vec3 from, Vec3 to, float connectionWidth) {
        nodes = new ArrayList<>();
        connections = new ArrayList<>();
        RopeNode previous = null;
        RopeNode current;
        for (int i = 0; i < count; i++) {
            nodes.add(current = new RopeNode(from.lerp(to, (double) i / count)));
            if (previous != null)
                connections.add(new RopeConnection(previous, current, connectionWidth));
            previous = current;
        }
        nodes.get(0).setFixed(true);
        nodes.get(count - 1).setFixed(true);
    }
    
    public List<RopeConnection> getConnections() {
        return connections;
    }
    
    public void simulate() {
        for (RopeNode node : nodes) {
            node.simulatePhysics();
        }
        for (int i = 0; i < 5; i++) {
            for (RopeConnection connection : connections) {
                connection.iterateConstraint();
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
