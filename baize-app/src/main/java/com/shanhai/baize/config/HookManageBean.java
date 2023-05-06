package com.shanhai.baize.config;

import com.shanhai.baize.match.CharTree;
import lombok.Data;

/**
 * hook管理类使用bean
 *
 * @author BillDowney
 * @date 2021/12/25 14:42
 */
@Data
public class HookManageBean {

    /**
     * 字符开始命令
     */
    private CharTree<Integer> cmdStart;
    /**
     * 字符全等命令
     */
    private CharTree<Integer> cmdEqual;

    public HookManageBean() {
        super();
        this.cmdStart = new CharTree<>();
        this.cmdEqual = new CharTree<>();
    }

    public void addCmdStart(String str, Integer data) {
        this.cmdStart.add(str, data);
    }

    public void addCmdEqual(String str, Integer data) {
        this.cmdEqual.add(str, data);
    }
}
