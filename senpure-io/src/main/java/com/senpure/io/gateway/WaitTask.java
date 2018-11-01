package com.senpure.io.gateway;

/**
 * WaitTask
 *
 * @author senpure
 * @time 2018-11-01 13:54:27
 */
public interface WaitTask {

    /**
     * 检查是否执行
     *
     * @return
     */
    boolean check();

    /**
     * 需要执行的任务
     *
     * @return
     */
    Runnable runnable();

    /**
     * 唯一标示
     *
     * @return
     */
    Long token();

    /**
     * 是否取消该任务
     *
     * @return
     */
    boolean cancel();

    /**
     * 取消之后需要执行的任务，为空不执行
     *
     * @return
     */
    Runnable afterCancel();


}
