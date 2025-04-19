package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

public class MCTS {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> initialState = game.start();
        MCTS mcts = new MCTS(initialState);
        State<TicTacToe> bestState = mcts.runMCTS();
        System.out.println("Best state found:\n" + bestState);
    }

    public MCTS(State<TicTacToe> rootState) {
        this.root = new TicTacToeNode(rootState, null); // root has no parent
    }

    private Node<TicTacToe> root;
    private static final int SIMULATIONS = 100000;

    public State<TicTacToe> runMCTS(State<TicTacToe> rootState) {
        Node<TicTacToe> rootNode = new TicTacToeNode(rootState, null);
        for (int i = 0; i < SIMULATIONS; i++) {
            Node<TicTacToe> selected = select(rootNode);
            Node<TicTacToe> expanded = expand(selected);
            double result = simulate(expanded);
            backpropagate(expanded, result);
        }
        return bestChild(rootNode).state();
    }

    public State<TicTacToe> runMCTS() {
        for (int i = 0; i < SIMULATIONS; i++) {
            Node<TicTacToe> selected = select(root);
            Node<TicTacToe> expanded = expand(selected);
            double result = simulate(expanded);
            backpropagate(expanded, result);
        }
        Node<TicTacToe> best = bestChild(root);
        root = best;
        return best.state();
    }

    public void updateRoot(State<TicTacToe> newState) {
        for (Node<TicTacToe> child : root.children()) {
            if (child.state().equals(newState)) {
                root = child;
                return;
            }
        }
        root = new TicTacToeNode(newState, null);
    }

    private Node<TicTacToe> select(Node<TicTacToe> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = bestUCT(node);
        }
        return node;
    }

    private Node<TicTacToe> expand(Node<TicTacToe> node) {
        if (node.isLeaf()) return node;
        for (Move<TicTacToe> move : node.state().moves(node.state().player())) {
            State<TicTacToe> newState = node.state().next(move);
            boolean exists = node.children().stream().anyMatch(child -> child.state().equals(newState));
            if (!exists) {
                return ((TicTacToeNode) node).addChildAndReturn(newState); // parent tracked internally
            }
        }
        return node;
    }

    private double simulate(Node<TicTacToe> node) {
        State<TicTacToe> state = node.state();
        final int initialPlayer = state.player();
        int currentPlayer = initialPlayer;
        while (!state.isTerminal()) {
            List<Move<TicTacToe>> moves = new ArrayList<>(state.moves(currentPlayer));
            if (moves.isEmpty()) break;
            Collections.shuffle(moves);
            state = state.next(moves.get(0));
            currentPlayer = 1 - currentPlayer;
        }
        Optional<Integer> winner = state.winner();
        return winner.map(w -> w == initialPlayer ? 1.0 : 0.0).orElse(0.5);
    }

    private void backpropagate(Node<TicTacToe> node, double result) {
        int rootPlayer = root.state().player();

        while (node != null) {
            TicTacToeNode tNode = (TicTacToeNode) node;
            int currentNodePlayer = tNode.state().player();

            // Flip result if the current node is from the opponent's perspective
            double actualResult = (currentNodePlayer == rootPlayer) ? result : (1.0 - result);

            tNode.backPropagate(actualResult);
            node = ((TicTacToeNode) node).getParent();
        }
    }


    private Node<TicTacToe> bestUCT(Node<TicTacToe> node) {
        TicTacToeNode parent = (TicTacToeNode) node;
        return node.children().stream().max(Comparator.comparingDouble(
                child -> {
                    TicTacToeNode c = (TicTacToeNode) child;
                    double exploitation = c.wins() / (double) (c.getVisits() + 1e-6);
                    double exploration = Math.sqrt(2 * Math.log(parent.getVisits() + 1) / (c.getVisits() + 1e-6));
                    return exploitation + exploration;
                }
        )).orElseThrow();
    }

    private Node<TicTacToe> bestChild(Node<TicTacToe> node) {
        return node.children().stream().max(Comparator.comparingDouble(
                c -> ((TicTacToeNode) c).wins() / (double) (((TicTacToeNode) c).playouts() + 1e-6)
        )).orElseThrow();
    }

}