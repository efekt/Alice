package it.efekt.alice.core;

public class ShutdownThread extends Thread{
    private Alice alice;

    public ShutdownThread(Alice alice){
        this.alice = alice;
    }

    @Override
    public void run() {
        System.out.println("Byeee Eugeo!");
    }
}
