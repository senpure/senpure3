package com.senpure.io.support.consumer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * ConsumerInvoker
 *
 * @author senpure
 * @time 2019-03-06 13:53:27
 */
public class ConsumerInvoker {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Future<String> future = new FutureTask(() -> System.out.println("-----")
                , "789");

        ((FutureTask<String>) future).run();
        System.out.println(future.get());
    }
}
