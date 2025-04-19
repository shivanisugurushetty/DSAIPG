package com.phasmidsoftware.dsaipg.adt.Trie;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TrieBenchmark {

    public static void main(String[] args) throws IOException {
        List<String> words = Files.readAllLines(Paths.get("src/main/resources/dictionary.txt"));

        Trie trie = new Trie();
        long start = System.nanoTime();
        for (String word : words) trie.insert(word);
        long trieInsertTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<String> result = trie.autocomplete("ap");
        long trieSearchTime = System.nanoTime() - start;

        start = System.nanoTime();
        List<String> linearResults = new ArrayList<>();
        for (String word : words) if (word.startsWith("ap")) linearResults.add(word);
        long linearSearchTime = System.nanoTime() - start;

        start = System.nanoTime();
        for (String word : words) trie.delete(word);
        long trieDeleteTime = System.nanoTime() - start;

        System.out.printf("Trie insert time: %d ns%n", trieInsertTime);
        System.out.printf("Trie search time: %d ns%n", trieSearchTime);
        System.out.printf("Linear search time: %d ns%n", linearSearchTime);
        System.out.printf("Trie delete time: %d ns%n", trieDeleteTime);
    }
}

