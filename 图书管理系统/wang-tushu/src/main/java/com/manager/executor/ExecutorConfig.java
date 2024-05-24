package com.manager.executor;

import lombok.Data;

@Data
public class ExecutorConfig {

    // 配置线程池参数
    public static int corePoolSize = 3; // 核心线程数
    public static int maximumPoolSize = 5; // 最大线程数
    public static long keepAliveTime = 10; // 线程空闲时间
}
