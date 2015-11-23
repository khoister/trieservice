package com.similarwords.app

class TrieNode(val c: Char) {
    var isWord : Boolean = false
    val children = new scala.collection.mutable.LinkedHashMap[Char,TrieNode]
}


class Trie {
    private val root: TrieNode = new TrieNode(0)

    def load(file: String) = {
        val source = scala.io.Source.fromFile(file)
        val it = source.getLines
        while (it.hasNext) {
            add(it.next)
        }
        source.close
    }

    private def add(word: String) = {
        var node: TrieNode = root
        for (c <- word) {
            if (!node.children.contains(c))
                node.children(c) = new TrieNode(c)

            node = node.children(c)
        }
        node.isWord = true
    }

    private def find(word: String) : Option[TrieNode] = {
        var node: TrieNode = root
        for (c <- word) {
            if (!node.children.contains(c))
                return None: Option[TrieNode]

            node = node.children(c)
        }
        Some(node)
    }

    def search(word: String) : Boolean = {
        val node: Option[TrieNode] = find(word)
        node.getOrElse(new TrieNode(0)).isWord
    }

    private def suffix(node: TrieNode, sb: scala.collection.mutable.StringBuilder, wordlist: scala.collection.mutable.Queue[String]) : Any = {
        if (node != null) {
            sb += node.c

            // Found a word
            if (node.isWord) {
                wordlist += sb.toString
            }

            for ((c, child) <- node.children) {
                suffix(child, sb, wordlist)
                // Backtrack
                sb.setLength(Math.max(sb.length - 1, 0))
            }
        }
    }

    def similar(prefix: String) : List[String] = {
        val wordlist = new scala.collection.mutable.Queue[String]
        val nodeOption: Option[TrieNode] = find(prefix)
        if (nodeOption == None)
            return wordlist.toList

        // The prefix itself is a valid word
        val suffixNode: TrieNode = nodeOption.get
        if (suffixNode.isWord)
            wordlist += prefix

        for ((c, child) <- suffixNode.children) {
            val sb = new scala.collection.mutable.StringBuilder(prefix)
            suffix(child, sb, wordlist)
        }
        wordlist.toList
    }
}
