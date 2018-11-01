package com.senpure.io.gateway;

/**
 * AbsWaitTask
 *
 * @author senpure
 * @time 2018-11-01 18:06:21
 */
public abstract class AbstractWaitTask implements WaitTask {
    protected long startTime;
    protected Runnable runnable;

    protected Runnable cancelRunnable;

    protected Long token;
    protected long maxDelay = 5000;

    public AbstractWaitTask(Long token, Runnable runnable, Runnable cancelRunnable) {
        this.runnable = runnable;
        this.token = token;
        this.cancelRunnable = cancelRunnable;
        startTime = System.currentTimeMillis();
    }

    public AbstractWaitTask(Long token, Runnable runnable, Runnable cancelRunnable, long maxDelay) {
        this.runnable = runnable;
        this.token = token;
        this.cancelRunnable = cancelRunnable;
        this.maxDelay = maxDelay;
        startTime = System.currentTimeMillis();
    }

    @Override
    public Runnable runnable() {
        return runnable;
    }

    @Override
    public Long token() {
        return token;
    }


    @Override
    public Runnable afterCancel() {
        return cancelRunnable;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }


    public void setCancelRunnable(Runnable cancelRunnable) {
        this.cancelRunnable = cancelRunnable;
    }

    public Long getToken() {
        return token;
    }

    public void setToken(Long token) {
        this.token = token;
    }

    public long getMaxDelay() {
        return maxDelay;
    }

    public void setMaxDelay(long maxDelay) {
        this.maxDelay = maxDelay;
    }
}
