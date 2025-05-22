package management;

import task.Task;
import utils.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> historyMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    private void linkLast(Task element) {
        // проверить на наличие элемента в списке через HashMap
        Integer id = element.getId();
        if (historyMap.containsKey(id)) {
            Node<Task> oldNode = historyMap.get(id);
            removeNode(oldNode);
            historyMap.remove(id);
        }

        Node<Task> newNode;
        if (head == null && tail == null) { // добавление в пустой список
            newNode = new Node<>(element);
            head = newNode;
        } else { // остальные кейсы
            newNode = new Node<>(tail, element);
            tail.setNext(newNode);
        }
        tail = newNode;
        historyMap.put(id, newNode);
    }

    private void removeNode(Node<Task> node) {
        if (head == null) { // empty list
            return;
        }

        if (head == node && tail == node) { // only one element in list
            head = null;
            tail = null;
            return;
        }

        if (head == node) { // first element of list
            Node<Task> next = node.getNext();
            head = next;
            next.setPrev(null);
            return;
        }

        if (tail == node) { // last element of list
            Node<Task> prev = node.getPrev();
            tail = prev;
            prev.setNext(null);
            return;
        }

        // mid element -> change refs for next and prev elements
        Node<Task> prev = node.getPrev();
        Node<Task> next = node.getNext();
        prev.setNext(next);
        next.setPrev(prev);
        return;
    }

    private ArrayList<Task> getTasks() {
        Node<Task> curNode = head;
        ArrayList<Task> tempHistoryList = new ArrayList<>();
        while (curNode != null) {
            tempHistoryList.add(curNode.getData());
            curNode = curNode.getNext();
        }
        return tempHistoryList;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (historyMap.containsKey(id)) {
            Node<Task> oldNode = historyMap.get(id);
            removeNode(oldNode);
            historyMap.remove(id);
        }
    }

}
