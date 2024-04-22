class Buffer {

    private boolean ready;

    private String data;

    private int activeWriters;

    public Buffer(int writerCount) {
        this.activeWriters = writerCount;
    }

    public synchronized void put(String data) throws InterruptedException {
        while(ready) {
            this.wait();
        }
        this.data = data;
        ready = true;
        this.notifyAll();
    }

    public synchronized String get() throws InterruptedException {
        while (activeWriters > 0 && !ready) {
            this.wait();
        }
        if (activeWriters <= 0) {
            return null;
        }
        ready = false;
        this.notifyAll();
        return data;
    }

    public synchronized void finish() throws InterruptedException {
        while(activeWriters == 1 && ready) {
            this.wait();
        }
        activeWriters--;
        this.notifyAll();
    }
}
