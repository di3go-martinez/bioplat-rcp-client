package edu.unlp.medicine.bioplat.rcp.ui.utils.async;

import java.util.concurrent.Future;

public interface AsyncCommand<T> {

	Future<T> execute();
}
