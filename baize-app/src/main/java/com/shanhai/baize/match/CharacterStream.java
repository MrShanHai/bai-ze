package com.shanhai.baize.match;

/**
 * 输入字符串，进行流式处理
 *
 * @author BillDowney
 * @date 2021/3/22 13:49
 */
public class CharacterStream {
    /**
     * 源码内容
     */
    private final String source;
    /**
     * 字符长度
     */
    private final int end;
    /**
     * 当前指针指向的下标
     */
    private int index = 0;
    /**
     * 记录获取文本的开始位置
     */
    private int spanStart = 0;
    /**
     * 标记当前是否有span正在扫描
     */
    private boolean spanFlag = false;

    public CharacterStream(String source) {
        this(source, 0, source.length());
    }

    public CharacterStream(String source, int start, int end) {
        if (source == null) {
            throw new NullPointerException("Source must be not null.");
        }
        if (start > end) {
            throw new IllegalArgumentException("Start must be <= end.");
        }
        if (start < 0) {
            throw new IndexOutOfBoundsException("Start must be >= 0.");
        }
        if (start > Math.max(0, source.length() - 1)) {
            throw new IndexOutOfBoundsException("Start outside of string.");
        }
        if (end > source.length()) {
            throw new IndexOutOfBoundsException("End outside of string.");
        }
        this.source = source;
        this.index = start;
        this.end = end;
    }

    /**
     * 下标是否到了临界值
     *
     * @return true/false
     */
    public boolean hasMore() {
        return index < end;
    }

    /**
     * 获取下一个字符，并且偏移下标
     *
     * @return char
     */
    public char consume() {
        if (!hasMore()) {
            throw new RuntimeException("No more characters in stream.");
        }
        return source.charAt(index++);
    }

    /**
     * 获取当前字符
     *
     * @return char
     */
    public char current() {
        if (!hasMore()) {
            throw new RuntimeException("No more characters in stream.");
        }
        return source.charAt(index);
    }

    /**
     * 获取下一个字符
     *
     * @return char
     */
    public char next() {
        if (!hasMore()) {
            throw new RuntimeException("No more characters in stream.");
        }
        return source.charAt(index + 1);
    }

    /**
     * 下标加1
     */
    public void offsetIndex() {
        this.index++;
    }

    /**
     * 返回当前下标偏移量
     *
     * @return {@link CharacterStream#index}
     */
    public int getPosition() {
        return index;
    }

    /**
     * 重置偏移量
     */
    public void resetPosition() {
        this.resetPosition(0);
    }

    /**
     * 重置偏移量
     *
     * @param position 偏移的下标值
     */
    public void resetPosition(int position) {
        if (position >= this.end) {
            throw new IndexOutOfBoundsException(String.format("Position must be <= %s.", this.end));
        }
        this.index = position;
    }

}
