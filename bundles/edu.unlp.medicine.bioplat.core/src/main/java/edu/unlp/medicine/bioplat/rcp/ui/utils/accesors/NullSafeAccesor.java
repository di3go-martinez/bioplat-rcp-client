package edu.unlp.medicine.bioplat.rcp.ui.utils.accesors;

public class NullSafeAccesor implements Accesor {

    private Accesor target;
    private Object defaultValue = "";

    public NullSafeAccesor(Accesor target) {
        this.target = target;
    }

    public NullSafeAccesor(Accesor target, Object defaultValue) {
        this(target);
        this.defaultValue = defaultValue;
    }

    @Override
    public Object get(Object element) {
        Object result = target.get(element);
        if (result == null) result = defaultValue;
        return result;
    }

    @Override
    public void set(Object element, Object value) {
        target.set(element, value);
    }

}
