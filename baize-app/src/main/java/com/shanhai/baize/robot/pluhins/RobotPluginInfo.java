package com.shanhai.baize.robot.pluhins;

import lombok.Data;

@Data
public class RobotPluginInfo {
    /**
     * 默认排序号
     */
    public final static int DEFAULT_ORDER = 10000;
    //"名称")
    private String name;
    //@ApiModelProperty(value = "描述")
    private String desc;
    //@ApiModelProperty(value = "是否启动", notes = "默认为false，可自行确定是否需要更改默认值")
    private boolean start;
    //@ApiModelProperty(value = "排序", notes = "数字越大越靠后，默认为10000")
    private int order;

    public RobotPluginInfo(String name) {
        this(name, null);
    }

    public RobotPluginInfo(String name, String desc) {
        this(name, desc, false, DEFAULT_ORDER);
    }

    public RobotPluginInfo(String name, boolean start) {
        this(name, null, start, DEFAULT_ORDER);
    }

    public RobotPluginInfo(String name, boolean start, int order) {
        this(name, null, start, order);
    }

    public RobotPluginInfo(String name, String desc, boolean start) {
        this(name, desc, start, DEFAULT_ORDER);
    }

    public RobotPluginInfo(String name, String desc, boolean start, int order) {
        super();
        this.order = order;
        this.name = name;
        this.desc = desc == null ? "" : desc;
        this.start = start;
    }

    public static RobotPluginInfo create(String name) {
        return new RobotPluginInfo(name);
    }

    public static RobotPluginInfo create(String name, String desc) {
        return new RobotPluginInfo(name, desc);
    }

    public static RobotPluginInfo create(String name, boolean start) {
        return new RobotPluginInfo(name, start);
    }

    public static RobotPluginInfo create(String name, boolean start, int order) {
        return new RobotPluginInfo(name, start, order);
    }

    public static RobotPluginInfo create(String name, String desc, boolean start) {
        return new RobotPluginInfo(name, desc, start);
    }

    /**
     * 添加描述信息，并末尾增加换行符
     *
     * @param desc 数据
     * @return 当前对象
     */
    public RobotPluginInfo addDescLn(String desc) {
        this.desc += desc + '\n';
        return this;
    }

    /**
     * 添加描述信息
     *
     * @param desc 数据
     * @return 当前对象
     */
    public RobotPluginInfo addDesc(String desc) {
        this.desc += desc;
        return this;
    }

}

