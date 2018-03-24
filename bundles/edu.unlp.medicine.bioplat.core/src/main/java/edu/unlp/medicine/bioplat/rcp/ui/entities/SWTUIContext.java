package edu.unlp.medicine.bioplat.rcp.ui.entities;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.eclipse.swt.widgets.Display;

import edu.unlp.medicine.bioplat.rcp.utils.PlatformUIUtils;
import edu.unlp.medicine.entity.generic.Context;

public class SWTUIContext implements Context {

	private Display d;

	public SWTUIContext() {
		d = PlatformUIUtils.findDisplay();
	}

	@Override
	public <T> T run(final Callable<T> caller) {

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
	public <T> Future<T> delayedRun(final Callable<T> caller) {
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
