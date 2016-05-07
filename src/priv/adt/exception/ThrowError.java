package priv.adt.exception;

public class ThrowError extends RuntimeException {

    static final long serialVersionUID = -7708801314520L;

    public ThrowError(String str) {
        super(str);
    }
}
