package jtg.generator;

public abstract class TypeUnknownGenerator<T> extends RandomGenerator<T> {

    @Override
    public T generate(Object... params) { return null; }

    @Override
    protected final Object generate() {
        return null;
    }
}
