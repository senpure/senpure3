package com.senpure.io.gateway;

/**
 * WaitRelationTask
 *
 * @author senpure
 * @time 2018-11-01 14:04:52
 */
public class WaitRelationTask extends  AbstractWaitTask {

    private boolean relation = false;

    private long relationTime;
    private long maxDelay = 5000;


    public WaitRelationTask(Long token, Runnable runnable,Runnable cancelRunnable) {

        super(token,runnable,cancelRunnable);
    }

    public WaitRelationTask(Long token, Runnable runnable,Runnable cancelRunnable, long maxDelay) {
        super(token,runnable,cancelRunnable,maxDelay);
    }

    @Override
    public boolean check() {
        if (relation) {
            return relationTime - startTime <= maxDelay;
        }
        return false;
    }



    public void setCancelRunnable(Runnable cancelRunnable) {
        this.cancelRunnable = cancelRunnable;
    }

    @Override
    public boolean cancel() {
        if (relation) {
            return relationTime - startTime > maxDelay;
        }
        return System.currentTimeMillis() - startTime > maxDelay;
    }


    public boolean isRelation() {
        return relation;
    }

    public void setRelation(boolean relation) {
        this.relation = relation;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public long getRelationTime() {
        return relationTime;
    }

    public void setRelationTime(long relationTime) {
        this.relationTime = relationTime;
    }
}
