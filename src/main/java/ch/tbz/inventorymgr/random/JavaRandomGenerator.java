package ch.tbz.inventorymgr.random;

import java.util.concurrent.ThreadLocalRandom;

public class JavaRandomGenerator implements RandomGenerator {

    @Override
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
