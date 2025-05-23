package utils;

public class Node<T> {
    private final T data;
    private Node<T> next;
    private Node<T> prev;

    public T getData() {
        return this.data;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getPrev() {
        return this.prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node(Node<T> prev, T data) {
        this.data = data;
        this.prev = prev;
        this.next = null;
    }

    public Node(T data) {
        this.data = data;
        this.prev = null;
        this.next = null;
    }
}
