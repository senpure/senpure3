package com.senpure.io.gateway;

import io.netty.channel.Channel;

/**
 * WaitAskTask
 *
 * @author senpure
 * @time 2018-11-01 18:03:18
 */
public class WaitAskTask extends AbstractWaitTask {

    private Channel firstChannel;
    private long firstTime;

    public WaitAskTask(Long token, Runnable runnable, Runnable cancelRunnable) {
        super(token, runnable, cancelRunnable);
    }

    public WaitAskTask(Long token, Runnable runnable, Runnable cancelRunnable, long maxDelay) {
        super(token, runnable, cancelRunnable, maxDelay);
    }

    @Override
    public boolean check() {
        if (firstChannel != null) {

        }
        return false;
    }


    @Override
    public boolean cancel() {

        return false;
    }


}
