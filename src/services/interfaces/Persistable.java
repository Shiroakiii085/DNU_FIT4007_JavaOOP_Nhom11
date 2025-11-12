package services.interfaces;

interface Persistable<T> {
    void save(String filename);
    void load(String filename);
}