package edu.unlp.medicine.bioplat.rcp.ui.entities;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.unlp.medicine.entity.generic.Context;

@Deprecated
public class PooledSWTUIContext implements Context {

	private static ExecutorService execs = Executors.newFixedThreadPool(5);

	private SWTUIContext target = new SWTUIContext();

	@Override
	public <T> Future<T> delayedRun(final Callable<T> arg0) {
		return execs.submit(new Callable<T>() {

			@Override
			public T call() throws Exception {
				return target.delayedRun(arg0).get();
			}

		});
	}

	@Override
	public <T> T run(Callable<T> arg0) {
		T t = null;
		try {
			t = (T) delayedRun(arg0).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
}
