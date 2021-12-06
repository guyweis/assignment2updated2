package bgu.spl.mics.application.objects;

/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Data {




    /**
     * Enum representing the Data type.
     */
    public enum Type {
        Images, Text, Tabular
    }

    private Type type;
    private int processed;
    private int size;

    public Data (Type name1, int size1)
    {
        type = name1;
        processed = 0;
        size = size1;
    }

    public Data (int processed1, int size1)
    {
        type = Type.Images;
        processed = processed1;
        size = size1;
    }

    public Type getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

}


