package net.AzureWare.utils.astar;

import java.util.ArrayList;

import net.minecraft.util.BlockPos;

public class AStarPath {
    private BlockPos blockPosFrom;
    private BlockPos blockPosTo;
    private ArrayList<AStarNode> path = new ArrayList();
    private ArrayList<AStarNode> openList = new ArrayList();
    private ArrayList<AStarNode> closedList = new ArrayList();
    private AStarUtil aStarUtil = new AStarUtil();
    private AStarNode lastNode;
    private long timeLeft;

    public AStarPath(BlockPos from, BlockPos to) {
        this.blockPosFrom = from;
        this.blockPosTo = to;
    }

    private void prepare() {
        AStarNode startField = new AStarNode(this.blockPosFrom.getX(), this.blockPosFrom.getY(), this.blockPosFrom.getZ());
        startField.setHeuristic(-1.0);
        this.openList.add(startField);
        this.timeLeft = System.currentTimeMillis();
    }

    private boolean step() {
        if (this.blockPosFrom.getX() == this.blockPosTo.getX() && this.blockPosFrom.getY() == this.blockPosTo.getY() && this.blockPosFrom.getZ() == this.blockPosTo.getZ()) {
            return true;
        }
        if (System.currentTimeMillis() - this.timeLeft >= 3000) {
            return true;
        }
        int nodeId = -1;
        int i = 0;
        while (i < this.openList.size()) {
            if (nodeId == -1 || this.openList.get(i).getHeuristic() < this.openList.get(nodeId).getHeuristic()) {
                nodeId = i;
            }
            ++i;
        }
        this.closedList.add(this.openList.get(nodeId));
        this.openList.remove(nodeId);
        AStarNode node = this.closedList.get(this.closedList.size() - 1);
        double x = node.getX() - 1.0;
        while (x <= node.getX() + 1.0) {
            double z = node.getZ() - 1.0;
            while (z <= node.getZ() + 1.0) {
                double y = node.getY() - 1.0;
                while (y <= node.getY() + 1.0) {
                    AStarNode currentNode = new AStarNode(x, y, z);
                    if (this.aStarUtil.isWalkableBlock(new BlockPos(node.getX(), node.getY(), node.getZ()), new BlockPos(x, y, z)) && !this.isInClosedList(currentNode)) {
                        currentNode.setHeuristic(this.aStarUtil.getCost(new BlockPos(this.blockPosTo.getX(), this.blockPosTo.getY(), this.blockPosTo.getZ()), new BlockPos(x, y, z)));
                        currentNode.setParent(node);
                        this.openList.add(currentNode);
                        if ((double)this.blockPosTo.getX() == currentNode.getX() && (double)this.blockPosTo.getY() == currentNode.getY() && (double)this.blockPosTo.getZ() == currentNode.getZ()) {
                            this.lastNode = currentNode;
                            return true;
                        }
                    }
                    y += 1.0;
                }
                z += 1.0;
            }
            x += 1.0;
        }
        return false;
    }

    public void doAstar() {
        this.prepare();
        while (!this.openList.isEmpty()) {
            if (this.step()) break;
        }
        this.fillPathList();
    }

    private void fillPathList() {
        if (this.lastNode != null) {
            AStarNode startPoint = this.lastNode;
            this.path.add(startPoint);
            while ((startPoint = startPoint.getParent()) != null) {
                this.path.add(startPoint);
            }
        }
    }

    private boolean isInClosedList(AStarNode node) {
        for (AStarNode iNode : this.closedList) {
            if (node.getX() != iNode.getX() || node.getY() != iNode.getY() || node.getZ() != iNode.getZ()) continue;
            return true;
        }
        return false;
    }

    public ArrayList<AStarNode> getPath() {
        return this.path;
    }

    public BlockPos getBlockPosFrom() {
        return this.blockPosFrom;
    }

    public void setBlockPosFrom(BlockPos blockPosFrom) {
        this.blockPosFrom = blockPosFrom;
    }

    public BlockPos getBlockPosTo() {
        return this.blockPosTo;
    }

    public void setBlockPosTo(BlockPos blockPosTo) {
        this.blockPosTo = blockPosTo;
    }
}

