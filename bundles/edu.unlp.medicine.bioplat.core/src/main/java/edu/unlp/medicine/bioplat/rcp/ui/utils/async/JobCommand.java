package edu.unlp.medicine.bioplat.rcp.ui.utils.async;

import java.util.concurrent.Future;

//TODO
public abstract class JobCommand<T> implements AsyncCommand<T> {

	@Override
	public Future<T> execute() {

		return null;
	}

}
