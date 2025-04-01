/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

/**
 * Class to represent a Monte Carlo Tree Search for TicTacToe.
 */
public class MCTS {

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        Node<TicTacToe> root = mcts.root;

        State<TicTacToe> bestState = mcts.runMCTS(root.state());
        System.out.println("Best state found:\n" + bestState);
    }

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;
    private static final int SIMULATIONS = 1000;

    public State<TicTacToe> runMCTS(State<TicTacToe> rootState){
        Node<TicTacToe> rootNode = new TicTacToeNode(rootState);
        for(int i=0; i<SIMULATIONS; i++){
            Node<TicTacToe> selected = select(rootNode);
            Node<TicTacToe> expanded = expand(selected);
            double result = simulate(expanded);
            backpropagate(expanded, result);
        }
        return bestChild(rootNode).state();
    }
    private Node<TicTacToe> select(Node<TicTacToe> node){
        while (!node.isLeaf() && !node.children().isEmpty()){
            node = bestUCT(node);


        }
        return node;

    }
    private Node<TicTacToe> expand(Node<TicTacToe> node){
        if(node.isLeaf())
            return node;
        for(Move<TicTacToe> move : node.state().moves(node.state().player())){
            State<TicTacToe> newState = node.state().next(move);
            TicTacToeNode newNode = new TicTacToeNode(newState);
            node.addChild(newState);
            return newNode;
        }
        return node;
    }
    private double simulate(Node<TicTacToe> node){
        State<TicTacToe> state = node.state();
        final int intialPlayer = state.player();
        int currentPlayer = intialPlayer;
        while(!state.isTerminal()){
            Collection<Move<TicTacToe>> moves = state.moves(currentPlayer);
            if(moves.isEmpty()) break;
            Move<TicTacToe> move = moves.iterator().next();
            state = state.next(move);
            currentPlayer = 1- currentPlayer;

        }
        Optional<Integer> winner = state.winner();
        return winner.map(w->w == intialPlayer ? 1.0 : 0.0). orElse(0.5);
    }
    private void backpropagate(Node<TicTacToe> node, double result){
        while(node!=null){
            node.backPropagate();
            break;
        }
    }
    private Node<TicTacToe> bestUCT(Node<TicTacToe> node){
        return node.children().stream().max(Comparator.comparingDouble(
                child->(child.wins()/(double) child.playouts())+Math.sqrt(2*Math.log(node.playouts()+1)/(child.playouts()+1))
                )).orElseThrow();
    }
    private Node<TicTacToe> bestChild(Node<TicTacToe> node){
        return node.children().stream().max(Comparator.comparingInt(Node::wins)).orElseThrow();

    }
}