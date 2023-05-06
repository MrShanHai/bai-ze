package com.shanhai.baize.match;

import cn.hutool.core.util.StrUtil;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 字符树，用来匹配Token
 *
 * @author BillDowney
 * @date 2021/3/24 9:54
 */
@ToString
public class CharTree<T> {

    /**
     * 根节点深度
     */
    public static final int ROOT_DEEP = -1;
    /**
     * 子节点数据
     */
    private final Map<Character, CharTree<T>> nextNode;
    /**
     * 当前字符串数据
     */
    private String string;
    /**
     * 字符深度，深度为-1则为根，也表示字符下标
     */
    private int deep;
    /**
     * 每个节点携带的数据
     */
    private T data;

    public CharTree() {
        this.nextNode = new HashMap<>();
        this.deep = ROOT_DEEP;
    }

    public CharTree(int deep, String string) {
        this();
        this.deep = deep;
        this.string = string.substring(0, this.deep + 1);
    }

    public Map<Character, CharTree<T>> getNextNode() {
        return nextNode;
    }

    public String getString() {
        return string;
    }

    public char getCurChar() {
        return string.charAt(this.deep);
    }

    public int getDeep() {
        return deep;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 将字符串挂载到树上
     *
     * @param str     需要挂载的字符串
     * @param data    该字符串所携带的数据
     * @param preNode 上一个节点
     */
    public void add(String str, T data, CharTree<T> preNode) {
        // 如果为空则为根节点
        if (StrUtil.isEmpty(str)) {
            this.data = data;
        } else if (str.length() > this.deep + 1) {
            // 如果长度大于深度，则字符串还没有遍历完
            char tmp = str.charAt(this.deep + 1);
            CharTree<T> node = null;
            if (nextNode.containsKey(tmp)) {
                node = nextNode.get(tmp);
            } else {
                int tmpDeep = preNode == null ? ROOT_DEEP : preNode.getDeep();
                node = new CharTree<T>(tmpDeep + 1, str);
                nextNode.put(tmp, node);
            }
            node.add(str, data, node);
        } else {
            this.data = data;
        }
    }

    /**
     * 将字符串挂载到树上
     *
     * @param str  需要挂载的字符串
     * @param data 该字符串所携带的数据
     */
    public void add(String str, T data) {
        this.add(str, data, null);
    }

    /**
     * 是否属于根节点
     *
     * @return true/false
     */
    public boolean isRoot() {
        return this.deep == ROOT_DEEP;
    }

    /**
     * 是否含有数据
     *
     * @return true/false
     */
    public boolean hasData() {
        return this.data != null;
    }

    /**
     * 是否含有下一个节点
     *
     * @return true/false
     */
    public boolean hasNext() {
        return !this.nextNode.isEmpty();
    }

    /**
     * 获取子节点的数据
     *
     * @param arg 字符
     * @return {@link CharTree}
     */
    public CharTree<T> get(char arg) {
        return this.get(this, arg);
    }

    /**
     * 获取子节点的数据
     *
     * @param node 节点数据
     * @param arg  字符
     * @return {@link CharTree}
     */
    public CharTree<T> get(CharTree<T> node, char arg) {
        return node.nextNode.get(arg);
    }

    /**
     * 获取子节点的数据
     *
     * @param arg 字符串
     * @return {@link CharTree}
     */
    public CharTree<T> get(String arg) {
        return this.get(this, arg);
    }

    /**
     * 获取子节点的数据
     *
     * @param node 节点数据
     * @param arg  字符串
     * @return {@link CharTree}
     */
    public CharTree<T> get(CharTree<T> node, String arg) {
        for (int i = 0, length = arg.length(); i < length; i++) {
            char tmp = arg.charAt(i);
            if (node.contains(tmp)) {
                node = node.get(tmp);
            } else {
                return null;
            }
        }
        return node;
    }

    /**
     * 是否含有指定字符的子节点
     *
     * @param arg 字符
     * @return true/false
     */
    public boolean contains(char arg) {
        return this.contains(this, arg);
    }

    /**
     * 是否含有指定字符的子节点
     *
     * @param node 节点数据
     * @param arg  字符
     * @return true/false
     */
    public boolean contains(CharTree<T> node, char arg) {
        return node.nextNode.containsKey(arg);
    }

    /**
     * 是否含有指定字符的子节点
     *
     * @param arg 字符串
     * @return true/false
     */
    public boolean contains(String arg) {
        return this.contains(this, arg);
    }

    /**
     * 是否含有指定字符的子节点
     *
     * @param node 节点数据
     * @param arg  字符串
     * @return true/false
     */
    public boolean contains(CharTree<T> node, String arg) {
        for (int i = 0, length = arg.length(); i < length; i++) {
            char tmp = arg.charAt(i);
            if (node.contains(tmp)) {
                node = node.get(tmp);
            } else {
                return false;
            }
        }
        return node.hasData();
    }

}
