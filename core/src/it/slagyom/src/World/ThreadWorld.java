package it.slagyom.src.World;

import java.util.concurrent.Semaphore;

public class ThreadWorld extends Thread {
	World world;
	Semaphore semaphore;

	public ThreadWorld(World world, Semaphore semaphore) {
		this.world = world;
		this.semaphore = semaphore;
	//	run();
	}

	@Override
	public void run() {
		super.run();
			long start = System.currentTimeMillis();
			while (true) {
				long attuale = System.currentTimeMillis();
				float dt = (float) (attuale - start);
				//System.out.println("fuori");
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("dentro");
				world.update((float) dt / 1000);
				semaphore.release();
				start = attuale;
			}
	}
	
}
