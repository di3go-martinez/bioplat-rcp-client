package edu.unlp.medicine.bioplat.rcp.snippets.guava;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class EventBusExample {

	static EventBus eb;
	static int i = 0;

	public static void main(String[] args) {
		eb = new EventBus();

		// somewhere during initialization
		eb.register(new EventBusChangeRecorder());

		// much later
		new EventBusExample().change();
	}

	public void change() {
		eb.post(new ChangeEvent(i++));
	}

	// Class is typically registered by the container.
	public static class EventBusChangeRecorder {
		@Subscribe
		public void recordCustomerChange(ChangeEvent e) {
			System.out.println(e.getChange());
		}

	}

	public static class ChangeEvent {
		private int i;

		public ChangeEvent(int i) {
			this.i = i;
		}

		public int getChange() {
			return 1;
		}
	}
}
