package it.slagyom.src.World;

import java.util.concurrent.Semaphore;

public class ThreadWorld extends Thread {
	World world;
	Semaphore semaphore;

	public ThreadWorld(World world, Semaphore semaphore) {
		this.world = world;
		this.semaphore = semaphore;
	}

	@Override
	public void run() {
		super.run();
			long start = System.currentTimeMillis();
			while (true) {
				long attuale = System.currentTimeMillis();
				float dt = (float) (attuale - start);
			
				world.update((float) dt / 1000);
			
				start = attuale;
			}
	}
	
}
