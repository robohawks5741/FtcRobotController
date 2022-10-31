package org.firstinspires.ftc.teamcode.ace;

import java.util.concurrent.Callable;

import kotlinx.coroutines.internal.ThreadSafeHeap;

/**
 * The Ace class is a small obfuscation of Java's native Threads.
 * @implNote I don't know how to use Javadoc. It seems like JSDoc though, so it shouldn't be that hard.
 */
public class Ace<T> extends Thread {
//    IF T IS VOID, CALLABLE HAS TO RETURN NULL
    public Ace(Callable<T> target) {
        super();
    }
}