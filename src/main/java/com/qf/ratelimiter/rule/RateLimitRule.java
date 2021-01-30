package com.qf.ratelimiter.rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RateLimitRule {
    private final RuleConfig ruleConfig;
    private final TrieTree<String> apiTrie;

    public RateLimitRule(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
        this.apiTrie = new TrieTree<>();
        buildTrie();
    }

    private void buildTrie() {
        List<String> insertList = new ArrayList<>();
        for (RuleConfig.AppRuleConfig config : this.ruleConfig.getConfigs()) {
            for (ApiLimit limit : config.getLimits()) {
                insertList.add(config.getAppId());
                insertList.addAll(Arrays.asList(limit.getApi().split("/")));
                insertList.add(limit.getLimit() + "");

                insertList.remove("");
                apiTrie.insert(insertList);
                insertList.clear();
            }
        }

    }

    public ApiLimit getLimit(String appId, String url) {
        TrieTree.TrieNode<String> searchNode = new TrieTree.TrieNode<>(null);
        TrieTree.TrieNode<String> p = apiTrie.root;

        for (String s : (appId + url).split("/")) {
            if (s.isEmpty()) continue;

            searchNode.value = s;
            //利用二分查找快速定位下一个子节点
            int ret = Collections.binarySearch(p.children, searchNode);
            if (ret <= -1) {
                return null;
            }
            p = p.children.get(ret);
        }
        return new ApiLimit(url, Integer.parseInt(p.children.get(0).value));
    }

    private static class TrieTree<E extends Comparable<E>> {
        private final TrieNode<E> root;

        private static class TrieNode<E extends Comparable<E>> implements Comparable<TrieNode<E>> {
            public E value;
            public List<TrieNode<E>> children;
            public boolean isEnding = false;

            public TrieNode(E value) {
                this.value = value;
                children = new ArrayList<>();
            }

            @Override
            public int compareTo(TrieNode<E> o) {
                return this.value.compareTo(o.value);
            }

            @Override
            public boolean equals(Object anObject) {
                if (this == anObject) {
                    return true;
                }
                return this.value.equals(anObject);
            }
        }

        public TrieTree() {
            root = new TrieNode<>(null);
        }

        public void insert(List<E> datas) {
            TrieNode<E> p = root;
            for (E e : datas) {
                TrieNode<E> node = p.children.stream().filter(x -> x.value.equals(e)).findFirst().orElse(null);
                if (node == null) {
                    node = new TrieNode<>(e);
                    p.children.add(node);
                    Collections.sort(p.children);
                }
                p = node;
            }
            p.isEnding = true;
        }
    }
}
