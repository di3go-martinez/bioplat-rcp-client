package edu.unlp.medicine.bioplat.rcp.ui.entities;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.eclipse.swt.widgets.Display;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.generic.Context;

//FIXME el tipo T, no va acá, en la definición de la clase
public class SWTUIContext<T> implements Context<T> {

	private Display d;

	public SWTUIContext() {
		d = PlatformUIUtils.findDisplay();
	}

	@Override
	public T run(final Callable<T> caller) {

		d.syncExec(new Runnable() {

			@Override
			public void run() {
				try {
					caller.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return null;
	}

	@Override
	public Future<T> delayedRun(final Callable<T> caller) {
		d.asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					caller.call();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return null;
	}

}
