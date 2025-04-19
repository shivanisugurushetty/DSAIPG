/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

    /**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }

    /**
     * @return the State of the Game G that this Node represents.
     */
    public State<TicTacToe> state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that X goes first so is "white."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<Node<TicTacToe>> children() {
        return children;
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new child.
     */
    @Override
    public void addChild(State<TicTacToe> state) {
        children.add(new TicTacToeNode(state, this)); // ✅ Updated: pass parent
    }

    public TicTacToeNode addChildAndReturn(State<TicTacToe> state) {
        TicTacToeNode child = new TicTacToeNode(state, this); // ✅ Updated: pass parent
        children.add(child);
        return child;
    }

    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate(double result) {
        visits++;
        playouts++;
        if (result == 1.0) {
            wins += 2;
        } else if (result == 0.5) {
            wins += 1;
        }
    }

    @Override
    public void backPropagate() {
        backPropagate(0.5); // default behavior
    }

    public int wins() {
        return wins;
    }

    public int playouts() {
        return playouts;
    }

    public int getVisits() {
        return visits;
    }

    // ✅ Constructor for root node (no parent)
    public TicTacToeNode(State<TicTacToe> state) {
        this(state, null);
    }

    // ✅ Constructor for all nodes, with parent
    public TicTacToeNode(State<TicTacToe> state, TicTacToeNode parent) {
        this.state = state;
        this.parent = parent; // ✅ Store parent
        this.children = new ArrayList<>();
        this.visits = 0;
        this.playouts = 0;
        this.wins = 0;
        initializeNodeData();
    }

    private void initializeNodeData() {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            if (winner.isPresent()) {
                int player = state.player();
                if (winner.get() == 1 - player)
                    wins = 2;
                else
                    wins = 0;
            } else {
                wins = 1;
            }
        }
    }



    public Node<TicTacToe> getParent() {
        return parent;
    }

    // === Fields ===
    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;
    private final TicTacToeNode parent; // ✅ Added parent reference
    private int wins;
    private int playouts;
    private int visits;
}
